package com.liu.minireport.ui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class XMLReader {

	/**
	 * ���ݸ����Ĵ�·�����ļ�������ȡһ��XML�ַ���
	 * 
	 * @param filename
	 * @return
	 */
	public String getXmlString(String filename) {
		try {
			StringBuffer buffer = new StringBuffer();
			System.out.println("filename:"+filename);
			InputStream is =this.getClass().getResourceAsStream(filename); 
			String line; // ��������ÿ�ж�ȡ������
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			line = reader.readLine(); // ��ȡ��һ��
			while (line != null) { // ��� line Ϊ��˵��������
				buffer.append(line); // ��������������ӵ� buffer ��
				buffer.append("\n"); // ��ӻ��з�
				line = reader.readLine(); // ��ȡ��һ��
			}
			reader.close();
			is.close();
			
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}
}
