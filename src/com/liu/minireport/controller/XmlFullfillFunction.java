package com.liu.minireport.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.jdbc.core.JdbcTemplate;

import com.liu.helper.SpringContextHelper;

/**
 * 针对XML格式的数据填充功能，将原来分别放置在<Head><Detail>中间的元素进行整合
 * 将所有的XML元素取消层次关系，打平到一个单一的层次返回
 * @author liujunsong
 *
 */
public class XmlFullfillFunction {
	/**
	 * 填充并转换DOM对象
	 * 算法：1.<Head>部分的原样保留
	 * 2.<Detail>部分的按照<Data>部分去取数，并进行填充转换
	 * @param inDoc
	 * @return
	 */
	public Document fullfillXMLBySQL(Document inDoc) {
		//step1:检索数据
		List<Map<String,Object>> datalist = this.getDataList(inDoc);
		if(datalist == null) {
			//未获取到数据时，设置空值
			datalist = new ArrayList<Map<String,Object>>();
		}
		System.out.println("datalist.size():"+datalist.size());
		
		//step2:数据填充输出
		Document outDoc = this.fullfillByData(inDoc, datalist);
		return outDoc;
	}
	
	/**
	 * 根据输入的Document对象，从其中检索出SQL语句并执行（未来可以扩展SQL的取数方式）
	 * @param inDoc
	 * @return
	 */
	private List<Map<String,Object>> getDataList(Document inDoc){
		List<Element> dataelements = inDoc.selectNodes("/Root/Data");
		Element dataelement;
		if(dataelements!=null && dataelements.size()>0) {
			//只取第一个Data节点
			dataelement = dataelements.get(0);
			String strsql = dataelement.getText();
			//检查strsql的有效性
			if(strsql !=null && strsql.length()>0) {
				//执行SQL语句，返回查询列表
				//TODO: 下一步考虑根据传入参数进行动态SQL的拼装
				DataSource ds = this.getDataSource();
				if(ds == null) {
					System.out.println("DataSource is null");
					return null;
				}else {
					JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
					return jdbcTemplate.queryForList(strsql);
				}
			}else {
				System.out.println("strsql is null or empty.");
				return null;
			}
			
		}else {
			System.out.println("Cannot find Data element in Document.");
			return null;
		}
	}
	
	private Document fullfillByData(Document inDoc,List<Map<String,Object>> datalist) {
		//step1:创建一个输出用的Document
		Document outDoc = DocumentHelper.createDocument();
		Element root = outDoc.addElement("Root");
		
		//step2.输出<Head>部分元素
		List<Element> heads = inDoc.selectNodes("/Root/Head");
		if(heads !=null && heads.size()>0) {
			Element head = heads.get(0);
			List<Element> headelements = head.elements();
			for(int i=0;i<headelements.size();i++) {
				//把头部的元素填充到输出的document里面去
				Element srcelement = headelements.get(i); 

				
				//复制一个新元素
				Element newelement = root.addElement(srcelement.getName());

				List<Attribute> attrs = srcelement.attributes();
	                //遍历所有的属性，写入到jobj1里面去
	                for (int j = 0; j < attrs.size(); j++) {
	                    //通过item(index)方法获取book节点的某一个属性
	                	Attribute attr = attrs.get(j);
	                    //获取属性名
	                    System.out.print("属性名：" + attr.getName());
	                    //获取属性值
	                    System.out.println("--属性值" + attr.getText());
	                    //jobj1.put(attr.getName(), attr.getText());
	                    newelement.setAttributeValue(attr.getName(), attr.getText());
	                }
				
			}
		}
		//step3.输出<Detail>部分元素，需要循环处理
		List<Element> details = inDoc.selectNodes("/Root/Detail");
		if(details !=null && details.size()>0) {
			//得到detail元素，这个元素的top属性和height属性是绘图时需要的
			Element detail = details.get(0);
			int detail_top = 0;
			detail_top  = Integer.parseInt(detail.attributeValue("top"));
			int detail_height = 100;
			detail_height = Integer.parseInt(detail.attributeValue("height"));
		
			//根据dataList来做循环处理
			for(int row=0;row<datalist.size();row++) {
				List<Element> detailelements = detail.elements();
				for(int i=0;i<detailelements.size();i++) {
					//对detail区域的元素进行循环处理
					Element srcelement = detailelements.get(i); 
					
					//复制一个新元素
					Element newelement = root.addElement(srcelement.getName());

					List<Attribute> attrs = srcelement.attributes();
		                //遍历所有的属性，写入到jobj1里面去
		                for (int j = 0; j < attrs.size(); j++) {
		                    //通过item(index)方法获取book节点的某一个属性
		                	Attribute attr = attrs.get(j);
		                    //获取属性名
		                    System.out.print("属性名：" + attr.getName());
		                    //获取属性值
		                    System.out.println("--属性值" + attr.getText());
		                    //jobj1.put(attr.getName(), attr.getText());
		                    //判断是否是top元素，对top元素进行处理
		                    if(attr.getName().equals("top")) {
		                    	int oldtop = Integer.parseInt(attr.getText());
		                    	int newtop = oldtop + detail_top + row * detail_height;
		                    	//根据top来计算偏移量
		                    	newelement.setAttributeValue("top",""+newtop);
		                    }else if(attr.getName().equals("text")){
		                    	//对text元素进行替换
		                    	if(attr.getText()!=null && attr.getText().startsWith("$")) {
		                    		//如果Text元素不为空，而且以$开头，则尝试使用后面的变量名进行替换
		                    		String varname = attr.getText().substring(1);
		                    		System.out.println("val name:"+varname);
		                    		String varvalue ="";
		                    		if(datalist.get(row).containsKey(varname)) {
		                    			if(datalist.get(row).get(varname)!=null) {
		                    				varvalue = datalist.get(row).get(varname).toString();
		                    			}else {
		                    				varvalue =""; 
		                    			}
		                    		}
		                    		//TODO:未来考虑增加数据的格式化输出功能
		                    		newelement.setAttributeValue(attr.getName(), varvalue);
		                    	}else {
		                    		//不以$开头，不替换
		                        	newelement.setAttributeValue(attr.getName(), attr.getText());
		                    	}
       		                }else {
	                        	newelement.setAttributeValue(attr.getName(), attr.getText());
		                    }
		                 }
				}
			}
		
		}
		
		//step4输出outDoc
		return outDoc;
	}
	
	/**
	 * 获取数据库连接对象
	 * @return
	 */
	private DataSource getDataSource() {
		SpringContextHelper springhelper = new SpringContextHelper();
		
		return (DataSource)springhelper.getBean("dataSource");
	}
}
