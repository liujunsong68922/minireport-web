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
 * ֱ�ӵ���Graphic�������ͼ
 * @author liujunsong
 *
 */
public class GraphicUIDraw {
	
	/**
	 * ���ݸ�����UI����VO����������JAVA��ͼ
	 * 
	 * @param datavo
	 * @return
	 * @throws IOException
	 */
	public String drawUI(XmlWinVO datavo) throws IOException {
		return drawUI(datavo,805,605);
	}

	
	/**
	 * ���ݸ�����UI����VO����������JAVA��ͼ
	 * 
	 * @param datavo
	 * @return
	 * @throws IOException
	 */
	public String drawUI(XmlWinVO datavo,int _width,int _height) throws IOException {

		int width = _width, height = _height;
		
		// ����ͼƬ����
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		// ����ͼƬ����򿪻�ͼ
		Graphics2D graphics = image.createGraphics();
		// ��ͼ�߼� START ������ҵ���߼����л�ͼ��������

		// �Ȼ���һ������
		graphics.setBackground(Color.WHITE);
		graphics.drawRect(0, 0, width, height);
		graphics.fillRect(0, 0, width, height);
		
		graphics.setColor(Color.BLACK);
		
		// ���������json���������л�ͼ
		// ��ֻ�����һ��Ķ�����
		JSONArray jchildren = datavo.getJsonobj().getJSONArray("children");
		if(jchildren !=null && jchildren.size()>0) {
			for(int i=0;i<jchildren.size();i++) {
				JSONObject jobj = jchildren.getJSONObject(i);
				if(jobj == null || jobj.getString("uitype")==null) {
					//uitypeδ���壬����ѭ��
					continue;
				}
				//���ߵ�֧��
				if(jobj!=null && jobj.getString("uitype").equals("Line")) {
					LineVO linevo = new LineVO();
					linevo.x1 = jobj.getIntValue("x1");
					linevo.y1 = jobj.getIntValue("y1");
					linevo.x2 = jobj.getIntValue("x2");
					linevo.y2 = jobj.getIntValue("y2");
					//����
					graphics.drawLine(linevo.x1,linevo.y1, linevo.x2, linevo.y2 );
				}
				//���ı���ǩ��֧��
				if(jobj!=null && jobj.getString("uitype").equals("Label")) {
					LabelVO labelvo = new LabelVO();
					labelvo.left = jobj.getIntValue("left");
					labelvo.top = jobj.getIntValue("top");
					labelvo.text = jobj.getString("text");
					
					Font font=new Font("����",Font.PLAIN, 14);
					//��������
					graphics.setFont(font);
					//���ı�����
					graphics.drawString(labelvo.text, labelvo.left, labelvo.top);
				}
				
			}
			
		}
		
		// ��ͼ�߼� END
		// �����ͼ
		graphics.dispose();
		// �����ƺõ�ͼƬд�뵽ͼƬ����һ���ٌ��DƬ�ļ����a�����BASE64���ػ�ȥ
		ImageIO.write(image, "jpeg", new File("abc.jpg"));
		ReadPicFiles rf = new ReadPicFiles();
		String strPicdata = rf.getPicFile("abc.jpg");
		return strPicdata;
	}

}


