package com.liu.minireport.controller;

import com.alibaba.fastjson.JSONObject;

public class XmlWinVO {
	private String xmlwinid;
	private String xmlwinname;
	private String xmlwinstr;
	//base64��ʽ�������ͼƬ�ַ���
	private String base64str;
	/**
	 * ���jsonobj�����ž���xml�����Ժ�õ���json����
	 */
	private JSONObject jsonobj;
	
	public String getXmlwinid() {
		return xmlwinid;
	}
	public void setXmlwinid(String xmlwinid) {
		this.xmlwinid = xmlwinid;
	}
	public String getXmlwinname() {
		return xmlwinname;
	}
	public void setXmlwinname(String xmlwinname) {
		this.xmlwinname = xmlwinname;
	}
	public String getXmlwinstr() {
		return xmlwinstr;
	}
	public void setXmlwinstr(String xmlwinstr) {
		this.xmlwinstr = xmlwinstr;
	}
	public JSONObject getJsonobj() {
		return jsonobj;
	}
	public void setJsonobj(JSONObject jsonobj) {
		this.jsonobj = jsonobj;
	}
	public String getBase64str() {
		return base64str;
	}
	public void setBase64str(String base64str) {
		this.base64str = base64str;
	}
	
	
}
