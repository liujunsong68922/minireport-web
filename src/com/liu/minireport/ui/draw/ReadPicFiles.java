package com.liu.minireport.ui.draw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ReadPicFiles{
	private static String filepath ="./";
	public static String getPicFile(String filename) {
		File file = new File(filepath+filename);
		if(! file.exists()) {
			return null;
		}
		
		//把这个图片文件转换成BASE64编码返回
		FileInputStream inputFile;
		try {
			inputFile = new FileInputStream(file);
	        byte[] buffer = new byte[(int)file.length()];
	        inputFile.read(buffer);
	        inputFile.close();
	        return new String(Base64.getEncoder().encode(buffer));			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	


}