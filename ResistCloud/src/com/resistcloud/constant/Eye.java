package com.resistcloud.constant;


/*
 * 设置视角的位置 包含了视角的位置
 */
public class Eye 
{
	//眼睛所在的位置
	public static float eyeX;
	public static float eyeY;
	public static float eyeZ;
		
	//中心点即说看得点
	public static float centerX;
	public static float centerY;
	public static float centerZ;
	
	public static void set()
	{
		eyeX    =  GLConstant.GLWidth /2;
		eyeY    =  GLConstant.GLHeight/2;
		eyeZ    =  GLConstant.GLHeight/2;
		
		centerX = GLConstant.GLWidth /2;
		centerY = GLConstant.GLHeight/2;
		centerZ = 0;
	}
}
