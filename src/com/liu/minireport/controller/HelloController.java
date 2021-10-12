package com.liu.minireport.controller;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liu.minireport.ui.XMLReader;
import com.liu.minireport.ui.draw.AwtUIDraw;
import com.liu.minireport.ui.draw.GraphicUIDraw;
import com.liu.minireport.ui.draw.ReadPicFiles;
import com.liu.minireport.ui.draw.SwingUIDraw;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


@Controller
@RequestMapping("/hello")
public class HelloController {
	
	@RequestMapping(value = "/test")
	@ResponseBody
	public String test(HttpServletRequest request) {
		System.out.println("HelloController.test:");
		return "OK";
	}
	
	@RequestMapping(value = "/drawbygraphics")
	@ResponseBody
	public String drawByGraphics(HttpServletRequest request) throws DocumentException {
		System.out.println("HelloController.drawByGraphics:");
		
		//读取XML文件，转换成Json对象
		//TODO:do something.
		XMLReader xmlreader = new XMLReader();
		String xmlstring = xmlreader.getXmlString("/com/liu/minireport/ui/1.xml");
		
		System.out.println("xmlstring:"+xmlstring);
		
		//从string里面获取Doc1
		Document doc1 = DocumentHelper.parseText(xmlstring);
		//自动填充doc1
		XmlFullfillFunction xf = new XmlFullfillFunction();
		Document doc2 = xf.fullfillXMLBySQL(doc1);
		
		xmlstring = doc2.asXML();
		System.out.println("doc2:"+xmlstring);
		
		//转换成xml对象
		JSONObject jobj = this.fromXml(xmlstring);
		
		XmlWinVO xmlwinvo = new XmlWinVO();
		xmlwinvo.setXmlwinstr(xmlstring);
		xmlwinvo.setJsonobj(jobj);
		
		//利用graphics绘图
		GraphicUIDraw uidraw = new GraphicUIDraw();
		String spic="";
		try {
			spic = uidraw.drawUI(xmlwinvo);
			System.out.println("spic:"+spic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "<img src=data:image/gif;base64,"+spic+"></img>";
	}	
	
	@RequestMapping(value = "/drawbycanvas")
	@ResponseBody
	public XmlWinVO drawByCanvas(HttpServletRequest request) throws DocumentException {
		System.out.println("HelloController.drawByCanvas:");
		
		//读取XML文件，转换成Json对象
		//TODO:do something.
		XMLReader xmlreader = new XMLReader();
		String xmlstring = xmlreader.getXmlString("/com/liu/minireport/ui/2.xml");
		
		System.out.println("xmlstring:"+xmlstring);
		
		//将xmlstring转换成Dom对象，并进行数据填充处理
		//从string里面获取Doc1
		Document doc1 = DocumentHelper.parseText(xmlstring);
		//自动填充doc1
		XmlFullfillFunction xf = new XmlFullfillFunction();
		Document doc2 = xf.fullfillXMLBySQL(doc1);
		
		xmlstring = doc2.asXML();
		
		
		
		//转换成xml对象
		JSONObject jobj = this.fromXml(xmlstring);
		
		XmlWinVO xmlwinvo = new XmlWinVO();
		xmlwinvo.setXmlwinstr(xmlstring);
		xmlwinvo.setJsonobj(jobj);
		
		//直接返回xmlwinvo对象，供前台canvas绘图
		return xmlwinvo;
	}	

	@RequestMapping(value = "/drawbysvg")
	@ResponseBody
	public XmlWinVO drawBySVG(HttpServletRequest request) throws DocumentException {
		System.out.println("HelloController.drawBySVG:");
		
		//读取XML文件，转换成Json对象
		//TODO:do something.
		XMLReader xmlreader = new XMLReader();
		String xmlstring = xmlreader.getXmlString("/com/liu/minireport/ui/3.xml");
		
		System.out.println("xmlstring:"+xmlstring);
		
		//将xmlstring转换成Dom对象，并进行数据填充处理
		//从string里面获取Doc1
		Document doc1 = DocumentHelper.parseText(xmlstring);
		//自动填充doc1
		XmlFullfillFunction xf = new XmlFullfillFunction();
		Document doc2 = xf.fullfillXMLBySQL(doc1);
		
		xmlstring = doc2.asXML();		
		
		//转换成xml对象
		JSONObject jobj = this.fromXml(xmlstring);
		
		XmlWinVO xmlwinvo = new XmlWinVO();
		xmlwinvo.setXmlwinstr(xmlstring);
		xmlwinvo.setJsonobj(jobj);
		
		//直接返回xmlwinvo对象，供前台canvas绘图
		return xmlwinvo;
	}	
	
	/**
	 * 将XML格式的字符串转换成JSONObject返回
	 * @param xmlstr
	 * @return
	 */
	private JSONObject fromXml(String xmlstr) {
		JSONObject jobj = new JSONObject();
		JSONArray jchild = new JSONArray();
		jobj.put("children", jchild);
		//读取xml格式的字符串
	
		org.dom4j.Document document = null;
		try {
			document = DocumentHelper.parseText(xmlstr);
			Element root = document.getRootElement();// 指向根节点
			//得到所有的子节点
			List<Element> bookList = root.elements();
			System.out.println("一共有" + bookList.size() + "个节点");
			for (int i = 0; i < bookList.size(); i++) {
				Element book = bookList.get(i);
				
				JSONObject jobj1 = new JSONObject();
				jobj1.put("uitype", book.getName());
				//获取book节点的所有属性集合
                List<Attribute> attrs = book.attributes();
                //遍历所有的属性，写入到jobj1里面去
                for (int j = 0; j < attrs.size(); j++) {
                    //通过item(index)方法获取book节点的某一个属性
                	Attribute attr = attrs.get(j);
                    //获取属性名
                    System.out.print("属性名：" + attr.getName());
                    //获取属性值
                    System.out.println("--属性值" + attr.getText());
                    jobj1.put(attr.getName(), attr.getText());
                }
                //写入子节点里面去
                jchild.add(jobj1);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jobj;
	}
}
	