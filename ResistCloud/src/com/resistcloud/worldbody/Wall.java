package com.resistcloud.worldbody;

/*
 * “墙” 父类
 */
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.common.Vec2;

public abstract class Wall 
{
	Vec2 point;
	
	int texture;
	
	//顶点坐标 纹理坐标
	FloatBuffer vertexPointer;
	FloatBuffer texCoordPointer;
	
	//向真实的物理世界添加"物体"
	public abstract void addBody();
	
	//绘制函数
	public abstract void drawSelf(GL10 gl);
}
