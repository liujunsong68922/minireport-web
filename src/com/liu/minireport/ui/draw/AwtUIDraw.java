package com.liu.minireport.ui.draw;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liu.minireport.controller.XmlWinVO;

/**
 * ʹ��AWT�����������ȡ�ļ�����������ͼ
 * @author liujunsong
 *
 */
public class AwtUIDraw {
	
	
	/**
	 * ����ͼƬ��������֮����ʱ�����Ƕ��̵߳�����
	 * @param xmlwinvo
	 * @return
	 */
	public String drawUI(com.liu.minireport.controller.XmlWinVO xmlwinvo) {
		MyFrame myframe = new MyFrame("AwtUIDraw");
		//����ͼ������frame
		//frame��print�¼��ص�ʱ���л�ͼ
		myframe.setDatavo(xmlwinvo);
		myframe.setLocation(0, 0);
		myframe.setSize(810, 610);
		//������ǰ̨����
		myframe.setAlwaysOnTop(true);
		myframe.setVisible(true);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s1 = myframe.drawUI(xmlwinvo);
		
		//myframe.setVisible(false);
		
		return s1 ;
	}
}

 class MyFrame extends Frame{
	 static MyFrame theframe ;
	 private XmlWinVO datavo;
	 
	 public XmlWinVO getDatavo() {
		return datavo;
	}

	public void setDatavo(XmlWinVO datavo) {
		this.datavo = datavo;
	}

	public MyFrame(String str1) {
		// TODO Auto-generated constructor stub
		 super(str1);
		 theframe = this;
		 
		 this.addWindowListener(new WindowAdapter() {
	          @Override
	          public void windowClosing(WindowEvent e)
	          {
	        	  if(theframe !=null) {
	        		  //��ָ����������Ϊ���ɼ�
	        		  theframe.setVisible(false);
	        	  }
	          }
	      });
	}

	@Override
	 public void paint(Graphics g) {
		 super.paint(g);
		 int width = 800, height = 600;
		 
		 System.out.println("call myframe.paint");
		 Graphics graphics = g;
			// �Ȼ���һ������
			//graphics.setBackground(Color.WHITE);
			graphics.drawRect(0, 0, width, height);
			//graphics.fillRect(0, 0, width, height);
			
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
						//���ı�����
						graphics.drawString(labelvo.text, labelvo.left, labelvo.top);
					}
					
				}
				
			}
			
			// ��ͼ�߼� END
			// �����ͼ
			graphics.dispose();		 
	 }
	
	/**
	 * ��������Java Robot����ͼ��ȡ�������ݣ������ظ�ǰ̨ʹ��
	 * @param datavo
	 * @return
	 * @throws IOException
	 */
	public String drawUI(com.liu.minireport.controller.XmlWinVO datavo)  {
		//����Robot��ȡͼƬ
		this.RobotSavePic();
		
		ReadPicFiles rf = new ReadPicFiles();
		String strPicdata = rf.getPicFile("abc.jpg");
		return strPicdata;
	}
	
	private void RobotSavePic() {
		Robot robot;
  		try {
  			robot = new Robot();
   			Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
   			BufferedImage bufferedimage=robot.createScreenCapture(new Rectangle(0,0,805,605));
   			File f=new File("abc.jpg");
   			OutputStream os;
   			try {
    				os = new FileOutputStream(f);
    				try {
     					ImageIO.write(bufferedimage, "jpg", os);
    				} catch (IOException e) {
     					e.printStackTrace();
    				}
   			} catch (FileNotFoundException e) {
   				e.printStackTrace();
   			}
  		} catch (AWTException e) {
   			e.printStackTrace();
  		}
	}
 }


