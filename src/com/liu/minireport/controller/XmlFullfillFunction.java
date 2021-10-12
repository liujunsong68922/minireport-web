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
 * ���XML��ʽ��������书�ܣ���ԭ���ֱ������<Head><Detail>�м��Ԫ�ؽ�������
 * �����е�XMLԪ��ȡ����ι�ϵ����ƽ��һ����һ�Ĳ�η���
 * @author liujunsong
 *
 */
public class XmlFullfillFunction {
	/**
	 * ��䲢ת��DOM����
	 * �㷨��1.<Head>���ֵ�ԭ������
	 * 2.<Detail>���ֵİ���<Data>����ȥȡ�������������ת��
	 * @param inDoc
	 * @return
	 */
	public Document fullfillXMLBySQL(Document inDoc) {
		//step1:��������
		List<Map<String,Object>> datalist = this.getDataList(inDoc);
		if(datalist == null) {
			//δ��ȡ������ʱ�����ÿ�ֵ
			datalist = new ArrayList<Map<String,Object>>();
		}
		System.out.println("datalist.size():"+datalist.size());
		
		//step2:����������
		Document outDoc = this.fullfillByData(inDoc, datalist);
		return outDoc;
	}
	
	/**
	 * ���������Document���󣬴����м�����SQL��䲢ִ�У�δ��������չSQL��ȡ����ʽ��
	 * @param inDoc
	 * @return
	 */
	private List<Map<String,Object>> getDataList(Document inDoc){
		List<Element> dataelements = inDoc.selectNodes("/Root/Data");
		Element dataelement;
		if(dataelements!=null && dataelements.size()>0) {
			//ֻȡ��һ��Data�ڵ�
			dataelement = dataelements.get(0);
			String strsql = dataelement.getText();
			//���strsql����Ч��
			if(strsql !=null && strsql.length()>0) {
				//ִ��SQL��䣬���ز�ѯ�б�
				//TODO: ��һ�����Ǹ��ݴ���������ж�̬SQL��ƴװ
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
		//step1:����һ������õ�Document
		Document outDoc = DocumentHelper.createDocument();
		Element root = outDoc.addElement("Root");
		
		//step2.���<Head>����Ԫ��
		List<Element> heads = inDoc.selectNodes("/Root/Head");
		if(heads !=null && heads.size()>0) {
			Element head = heads.get(0);
			List<Element> headelements = head.elements();
			for(int i=0;i<headelements.size();i++) {
				//��ͷ����Ԫ����䵽�����document����ȥ
				Element srcelement = headelements.get(i); 

				
				//����һ����Ԫ��
				Element newelement = root.addElement(srcelement.getName());

				List<Attribute> attrs = srcelement.attributes();
	                //�������е����ԣ�д�뵽jobj1����ȥ
	                for (int j = 0; j < attrs.size(); j++) {
	                    //ͨ��item(index)������ȡbook�ڵ��ĳһ������
	                	Attribute attr = attrs.get(j);
	                    //��ȡ������
	                    System.out.print("��������" + attr.getName());
	                    //��ȡ����ֵ
	                    System.out.println("--����ֵ" + attr.getText());
	                    //jobj1.put(attr.getName(), attr.getText());
	                    newelement.setAttributeValue(attr.getName(), attr.getText());
	                }
				
			}
		}
		//step3.���<Detail>����Ԫ�أ���Ҫѭ������
		List<Element> details = inDoc.selectNodes("/Root/Detail");
		if(details !=null && details.size()>0) {
			//�õ�detailԪ�أ����Ԫ�ص�top���Ժ�height�����ǻ�ͼʱ��Ҫ��
			Element detail = details.get(0);
			int detail_top = 0;
			detail_top  = Integer.parseInt(detail.attributeValue("top"));
			int detail_height = 100;
			detail_height = Integer.parseInt(detail.attributeValue("height"));
		
			//����dataList����ѭ������
			for(int row=0;row<datalist.size();row++) {
				List<Element> detailelements = detail.elements();
				for(int i=0;i<detailelements.size();i++) {
					//��detail�����Ԫ�ؽ���ѭ������
					Element srcelement = detailelements.get(i); 
					
					//����һ����Ԫ��
					Element newelement = root.addElement(srcelement.getName());

					List<Attribute> attrs = srcelement.attributes();
		                //�������е����ԣ�д�뵽jobj1����ȥ
		                for (int j = 0; j < attrs.size(); j++) {
		                    //ͨ��item(index)������ȡbook�ڵ��ĳһ������
		                	Attribute attr = attrs.get(j);
		                    //��ȡ������
		                    System.out.print("��������" + attr.getName());
		                    //��ȡ����ֵ
		                    System.out.println("--����ֵ" + attr.getText());
		                    //jobj1.put(attr.getName(), attr.getText());
		                    //�ж��Ƿ���topԪ�أ���topԪ�ؽ��д���
		                    if(attr.getName().equals("top")) {
		                    	int oldtop = Integer.parseInt(attr.getText());
		                    	int newtop = oldtop + detail_top + row * detail_height;
		                    	//����top������ƫ����
		                    	newelement.setAttributeValue("top",""+newtop);
		                    }else if(attr.getName().equals("text")){
		                    	//��textԪ�ؽ����滻
		                    	if(attr.getText()!=null && attr.getText().startsWith("$")) {
		                    		//���TextԪ�ز�Ϊ�գ�������$��ͷ������ʹ�ú���ı����������滻
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
		                    		//TODO:δ�������������ݵĸ�ʽ���������
		                    		newelement.setAttributeValue(attr.getName(), varvalue);
		                    	}else {
		                    		//����$��ͷ�����滻
		                        	newelement.setAttributeValue(attr.getName(), attr.getText());
		                    	}
       		                }else {
	                        	newelement.setAttributeValue(attr.getName(), attr.getText());
		                    }
		                 }
				}
			}
		
		}
		
		//step4���outDoc
		return outDoc;
	}
	
	/**
	 * ��ȡ���ݿ����Ӷ���
	 * @return
	 */
	private DataSource getDataSource() {
		SpringContextHelper springhelper = new SpringContextHelper();
		
		return (DataSource)springhelper.getBean("dataSource");
	}
}
