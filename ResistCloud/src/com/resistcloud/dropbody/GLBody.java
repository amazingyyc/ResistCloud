package com.resistcloud.dropbody;

/*
 * 投掷类和墙壁类的父类
 * 
 */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import com.resistcloud.worldbody.GLWorld;

public abstract class GLBody 
{
	//中心点
	public Vec2 point;
	
	public Vec2 getPoint()
	{
		return this.point;
	}
	
	public void setPoint(Vec2 point)
	{
		this.point = point;
	}
	
	//物体的半长
	float halfLength;
	
	public float getHalfLength()
	{
		return this.halfLength;
	}
	
	//旋转的角度
	float angle;
	
	//是否可以移动标志
	//boolean ifMove;

	//纹理
	int texture;
	
	//类型标识
	int style;
	
	public int getStyle()
	{
		return this.style;
	}
	
	//对应的真实物体
	public Body body;
	
	//是否加入了真实的物理世界
	public boolean ifBody;
	
	//顶点坐标
	FloatBuffer vertexPointer;
	
	//纹理坐标
	FloatBuffer texCoordPointer;
	
	//判断点是否在“物体”内,即判断是否可以移动
	public abstract boolean inGLBody(Vec2 v);
	
	//向真实的物理世界添加"物体"
	public abstract void addBody();
	
	//开始模拟
	//public abstract void wakeUp();
	//开始模拟 唤醒物体
	public void wakeUp()
	{
		GLWorld.needWakeUp.add(this);
	}
	
	//绘制函数
	public abstract void drawSelf(GL10 gl);
	
	//分别代表小正方形 大正方形 圆 三角形 长方形 笑脸 墙
	public static final int GLBODY_SMALLSQUARE = 0;
	public static final int GLBODY_BIGSQUARE   = 1;
	public static final int GLBODY_CIRCLE      = 2;
	public static final int GLBODY_TRIANGLR    = 3;
	public static final int GLBODY_RECTANGLE   = 4;
	//static final int GLBODY_FACE        = 5;
	//static final int GLBODY_WALL        = 6;
	
	
	//两点的距离
	static public float getLong(Vec2 v1, Vec2 v2)
	{
		return (float) Math.sqrt((v1.x-v2.x)*(v1.x-v2.x) + (v1.y-v2.y)*(v1.y-v2.y));
	}
	
	static public FloatBuffer setPointer(float[] f)
	{
		FloatBuffer verPointer = null;
		
		ByteBuffer vbb= ByteBuffer.allocateDirect(f.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		verPointer = vbb.asFloatBuffer();
		verPointer.put(f);
		verPointer.position(0);
				
		return verPointer;
	}
}



















