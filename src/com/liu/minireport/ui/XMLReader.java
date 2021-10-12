package com.liu.minireport.ui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class XMLReader {

	/**
	 * 根据给定的带路径的文件名，读取一个XML字符串
	 * 
	 * @param filename
	 * @return
	 */
	public String getXmlString(String filename) {
		try {
			StringBuffer buffer = new StringBuffer();
			System.out.println("filename:"+filename);
			InputStream is =this.getClass().getResourceAsStream(filename); 
			String line; // 用来保存每行读取的内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			line = reader.readLine(); // 读取第一行
			while (line != null) { // 如果 line 为空说明读完了
				buffer.append(line); // 将读到的内容添加到 buffer 中
				buffer.append("\n"); // 添加换行符
				line = reader.readLine(); // 读取下一行
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
