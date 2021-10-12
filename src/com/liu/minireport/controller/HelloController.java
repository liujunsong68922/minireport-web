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
		
		//��ȡXML�ļ���ת����Json����
		//TODO:do something.
		XMLReader xmlreader = new XMLReader();
		String xmlstring = xmlreader.getXmlString("/com/liu/minireport/ui/1.xml");
		
		System.out.println("xmlstring:"+xmlstring);
		
		//��string�����ȡDoc1
		Document doc1 = DocumentHelper.parseText(xmlstring);
		//�Զ����doc1
		XmlFullfillFunction xf = new XmlFullfillFunction();
		Document doc2 = xf.fullfillXMLBySQL(doc1);
		
		xmlstring = doc2.asXML();
		System.out.println("doc2:"+xmlstring);
		
		//ת����xml����
		JSONObject jobj = this.fromXml(xmlstring);
		
		XmlWinVO xmlwinvo = new XmlWinVO();
		xmlwinvo.setXmlwinstr(xmlstring);
		xmlwinvo.setJsonobj(jobj);
		
		//����graphics��ͼ
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
		
		//��ȡXML�ļ���ת����Json����
		//TODO:do something.
		XMLReader xmlreader = new XMLReader();
		String xmlstring = xmlreader.getXmlString("/com/liu/minireport/ui/2.xml");
		
		System.out.println("xmlstring:"+xmlstring);
		
		//��xmlstringת����Dom���󣬲�����������䴦��
		//��string�����ȡDoc1
		Document doc1 = DocumentHelper.parseText(xmlstring);
		//�Զ����doc1
		XmlFullfillFunction xf = new XmlFullfillFunction();
		Document doc2 = xf.fullfillXMLBySQL(doc1);
		
		xmlstring = doc2.asXML();
		
		
		
		//ת����xml����
		JSONObject jobj = this.fromXml(xmlstring);
		
		XmlWinVO xmlwinvo = new XmlWinVO();
		xmlwinvo.setXmlwinstr(xmlstring);
		xmlwinvo.setJsonobj(jobj);
		
		//ֱ�ӷ���xmlwinvo���󣬹�ǰ̨canvas��ͼ
		return xmlwinvo;
	}	

	@RequestMapping(value = "/drawbysvg")
	@ResponseBody
	public XmlWinVO drawBySVG(HttpServletRequest request) throws DocumentException {
		System.out.println("HelloController.drawBySVG:");
		
		//��ȡXML�ļ���ת����Json����
		//TODO:do something.
		XMLReader xmlreader = new XMLReader();
		String xmlstring = xmlreader.getXmlString("/com/liu/minireport/ui/3.xml");
		
		System.out.println("xmlstring:"+xmlstring);
		
		//��xmlstringת����Dom���󣬲�����������䴦��
		//��string�����ȡDoc1
		Document doc1 = DocumentHelper.parseText(xmlstring);
		//�Զ����doc1
		XmlFullfillFunction xf = new XmlFullfillFunction();
		Document doc2 = xf.fullfillXMLBySQL(doc1);
		
		xmlstring = doc2.asXML();		
		
		//ת����xml����
		JSONObject jobj = this.fromXml(xmlstring);
		
		XmlWinVO xmlwinvo = new XmlWinVO();
		xmlwinvo.setXmlwinstr(xmlstring);
		xmlwinvo.setJsonobj(jobj);
		
		//ֱ�ӷ���xmlwinvo���󣬹�ǰ̨canvas��ͼ
		return xmlwinvo;
	}	
	
	/**
	 * ��XML��ʽ���ַ���ת����JSONObject����
	 * @param xmlstr
	 * @return
	 */
	private JSONObject fromXml(String xmlstr) {
		JSONObject jobj = new JSONObject();
		JSONArray jchild = new JSONArray();
		jobj.put("children", jchild);
		//��ȡxml��ʽ���ַ���
	
		org.dom4j.Document document = null;
		try {
			document = DocumentHelper.parseText(xmlstr);
			Element root = document.getRootElement();// ָ����ڵ�
			//�õ����е��ӽڵ�
			List<Element> bookList = root.elements();
			System.out.println("һ����" + bookList.size() + "���ڵ�");
			for (int i = 0; i < bookList.size(); i++) {
				Element book = bookList.get(i);
				
				JSONObject jobj1 = new JSONObject();
				jobj1.put("uitype", book.getName());
				//��ȡbook�ڵ���������Լ���
                List<Attribute> attrs = book.attributes();
                //�������е����ԣ�д�뵽jobj1����ȥ
                for (int j = 0; j < attrs.size(); j++) {
                    //ͨ��item(index)������ȡbook�ڵ��ĳһ������
                	Attribute attr = attrs.get(j);
                    //��ȡ������
                    System.out.print("��������" + attr.getName());
                    //��ȡ����ֵ
                    System.out.println("--����ֵ" + attr.getText());
                    jobj1.put(attr.getName(), attr.getText());
                }
                //д���ӽڵ�����ȥ
                jchild.add(jobj1);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jobj;
	}
}
	