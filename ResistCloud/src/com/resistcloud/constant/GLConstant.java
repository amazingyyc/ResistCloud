package com.resistcloud.constant;

import org.jbox2d.common.Vec2;

import com.resistcloud.main.MainActivity;

import android.view.Display;
import android.view.WindowManager;

/*
 * ��������Ļ�ĳ��� ӳ�䵽GL����ĳ���ȳ���
 */
public class GLConstant 
{
	//��Ļ�����
	static float screenWidth;
	static float screenHeight;
		
	//ͶӰ��openGL ES �к�Ŀ���� 
	//�߶ȹ̶�
	public static float GLWidth;
	public static float GLHeight;
	
	public static void set(MainActivity activity)
	{
		WindowManager windowManager = activity.getWindowManager();   
		Display display = windowManager.getDefaultDisplay();
		
		screenWidth  = display.getWidth();   
		screenHeight = display.getHeight();  
		
		GLHeight = 8;
		GLWidth  = GLHeight*screenWidth/screenHeight;
	}
	
	//�������ص�����õ�OpenGL������
	static public Vec2 getVByXY(float x, float y)
	{
		y = screenHeight - y;
		
		float xGL = x/screenWidth  * GLWidth;
		float yGL = y/screenHeight * GLHeight;
				
		return new Vec2(xGL, yGL);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
