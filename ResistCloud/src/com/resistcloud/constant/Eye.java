package com.resistcloud.constant;


/*
 * �����ӽǵ�λ�� �������ӽǵ�λ��
 */
public class Eye 
{
	//�۾����ڵ�λ��
	public static float eyeX;
	public static float eyeY;
	public static float eyeZ;
		
	//���ĵ㼴˵���õ�
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
