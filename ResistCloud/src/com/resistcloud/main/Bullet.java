package com.resistcloud.main;

import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import com.resistcloud.dropbody.GLBody;
import com.resistcloud.worldbody.GLWorld;

/*
 * 乌云丢下的子弹类
 */
public class Bullet
{
	//图形中的半宽与半高
	private float halfWidth;
	private float halfHeight;
	
	Vec2 point;
	Vec2 newPoint;
	
	float radius;
	float angle;

	Body body;

	int texture;

	// 是否被丢下
	boolean ifDrop;

	// 顶点坐标 纹理坐标
	FloatBuffer vertexPointer;
	FloatBuffer texCoordPointer;

	int number;

	public Bullet(int texture)
	{
		//图形中的半宽与半高
		this.halfWidth  = 0.1f;
		this.halfHeight = 0.1f;
		
		this.point = new Vec2();
		this.radius = 0.1f;
		this.angle = 0;
		this.ifDrop = false;
		this.texture = texture;
		this.body = null;

		addBody();
		setPointer();
	}

	public void addBody()
	{
		CircleDef shape = new CircleDef();
		shape.density = 0.1f; // 密度
		shape.friction = 0.1f; // 摩擦
		shape.restitution = 0.5f; // 弹性
		shape.radius = this.radius; // 半径

		BodyDef bodyDef = new BodyDef();
		bodyDef.angle = 0;
		///////////////////////////////////
		bodyDef.position.set(new Vec2(-10, 10));

		this.body = GLWorld.physicsWorld.world.createBody(bodyDef);
		this.body.setUserData("Bullet");
		this.body.createShape(shape);
		this.body.setMassFromShapes();

		this.body.putToSleep();
	}

	
	public void wakeUp(Vec2 v)
	{
		this.newPoint = v;
		
		GLWorld.bulletWakeUp.add(this);
	}

	// 设置顶点和纹理坐标
	public void setPointer()
	{
		float newFloatVertex[] = {
				-halfWidth, halfHeight,-halfWidth,-halfHeight, halfWidth,-halfHeight,
				-halfWidth, halfHeight, halfWidth,-halfHeight, halfWidth, halfHeight,
		}; 
		
		float newFloatTexCoord[] = {
				0,0,0,1,1,1,
				0,0,1,1,1,0,
		};
		
		vertexPointer   = GLBody.setPointer(newFloatVertex);
		texCoordPointer = GLBody.setPointer(newFloatTexCoord);
	}

	public void drawSelf(GL10 gl)
	{
		if (ifDrop)
		{
			this.angle = this.body.getAngle();
			this.point = this.body.getPosition();

			gl.glPushMatrix();
			gl.glTranslatef(point.x, point.y, 0);
			gl.glRotatef((float) Math.toDegrees(angle), 0, 0, 1);
	
			gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexPointer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
			
			//gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, number);
			gl.glEnable(GL10.GL_BLEND); // 打开混合
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
			gl.glDisable(GL10.GL_DEPTH_TEST); // 关闭深度测试
			
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
			
			gl.glDisable(GL10.GL_BLEND); // 关闭混合
			gl.glEnable(GL10.GL_DEPTH_TEST); // 打开深度测试
	
			gl.glPopMatrix();
		}
	}

}
