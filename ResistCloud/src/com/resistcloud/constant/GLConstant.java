package com.resistcloud.constant;

import org.jbox2d.common.Vec2;

import com.resistcloud.main.MainActivity;

import android.view.Display;
import android.view.WindowManager;

/*
 * 包含了屏幕的长宽 映射到GL坐标的长宽等常量
 */
public class GLConstant 
{
	//屏幕宽与高
	static float screenWidth;
	static float screenHeight;
		
	//投影到openGL ES 中后的宽与高 
	//高度固定
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
	
	//根据像素的坐标得到OpenGL的坐标
	static public Vec2 getVByXY(float x, float y)
	{
		y = screenHeight - y;
		
		float xGL = x/screenWidth  * GLWidth;
		float yGL = y/screenHeight * GLHeight;
				
		return new Vec2(xGL, yGL);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
