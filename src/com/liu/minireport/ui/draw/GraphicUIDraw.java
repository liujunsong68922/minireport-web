package com.liu.minireport.ui.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liu.minireport.controller.XmlWinVO;

//import com.liu.dbui.controller.ui.ReadPicFiles;

/**
 * 直接调用Graphic组件来绘图
 * @author liujunsong
 *
 */
public class GraphicUIDraw {
	
	/**
	 * 根据给定的UI定义VO对象来进行JAVA绘图
	 * 
	 * @param datavo
	 * @return
	 * @throws IOException
	 */
	public String drawUI(XmlWinVO datavo) throws IOException {
		return drawUI(datavo,805,605);
	}

	
	/**
	 * 根据给定的UI定义VO对象来进行JAVA绘图
	 * 
	 * @param datavo
	 * @return
	 * @throws IOException
	 */
	public String drawUI(XmlWinVO datavo,int _width,int _height) throws IOException {

		int width = _width, height = _height;
		
		// 创建图片对象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		// 基于图片对象打开绘图
		Graphics2D graphics = image.createGraphics();
		// 绘图逻辑 START （基于业务逻辑进行绘图处理）……

		// 先绘制一个矩形
		graphics.setBackground(Color.WHITE);
		graphics.drawRect(0, 0, width, height);
		graphics.fillRect(0, 0, width, height);
		
		graphics.setColor(Color.BLACK);
		
		// 根据输入的json对象来进行绘图
		// 先只处理第一层的对象定义
		JSONArray jchildren = datavo.getJsonobj().getJSONArray("children");
		if(jchildren !=null && jchildren.size()>0) {
			for(int i=0;i<jchildren.size();i++) {
				JSONObject jobj = jchildren.getJSONObject(i);
				if(jobj == null || jobj.getString("uitype")==null) {
					//uitype未定义，继续循环
					continue;
				}
				//画线的支持
				if(jobj!=null && jobj.getString("uitype").equals("Line")) {
					LineVO linevo = new LineVO();
					linevo.x1 = jobj.getIntValue("x1");
					linevo.y1 = jobj.getIntValue("y1");
					linevo.x2 = jobj.getIntValue("x2");
					linevo.y2 = jobj.getIntValue("y2");
					//画线
					graphics.drawLine(linevo.x1,linevo.y1, linevo.x2, linevo.y2 );
				}
				//画文本标签的支持
				if(jobj!=null && jobj.getString("uitype").equals("Label")) {
					LabelVO labelvo = new LabelVO();
					labelvo.left = jobj.getIntValue("left");
					labelvo.top = jobj.getIntValue("top");
					labelvo.text = jobj.getString("text");
					
					Font font=new Font("宋体",Font.PLAIN, 14);
					//设置字体
					graphics.setFont(font);
					//画文本出来
					graphics.drawString(labelvo.text, labelvo.left, labelvo.top);
				}
				
			}
			
		}
		
		// 绘图逻辑 END
		// 处理绘图
		graphics.dispose();
		// 将绘制好的图片写入到图片，下一步再將圖片文件編碼编码成BASE64返回回去
		ImageIO.write(image, "jpeg", new File("abc.jpg"));
		ReadPicFiles rf = new ReadPicFiles();
		String strPicdata = rf.getPicFile("abc.jpg");
		return strPicdata;
	}

}


