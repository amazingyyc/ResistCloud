package com.resistcloud.worldbody;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import com.resistcloud.dropbody.GLBody;

/*
 * 笑脸类 被砸的
 */
public class Face
{
	//图形中的半宽与半高
	private float halfWidth;
	private float halfHeight;
	
	//物理世界的半径
	public float radius;
	float angle;
	int   texture;
	
	Body body;
	public Vec2 point;
	
	//顶点坐标 纹理坐标
	FloatBuffer vertexPointer;
	FloatBuffer texCoordPointer;
	
	int number;
	
	public Face(float x, float y, int texture)
	{
		//图形中的半宽与半高
		this.halfWidth  = 0.582f;
		this.halfHeight = 0.582f;
		
		this.angle   = 0;
		this.radius  = 0.5f;
		this.texture = texture;
		this.body    = null;
		this.point   = new Vec2(x, y);
		
		setPointer();
		addBody();
	}

	public void addBody() 
	{
		CircleDef shape   = new CircleDef();   
		shape.density     = 0.1f;   //密度
		shape.friction    = 0.1f;   //摩擦
		shape.restitution = 0.1f;   //弹性
		shape.radius      = this.radius; //半径
		  
		BodyDef bodyDef = new BodyDef();  
		bodyDef.angle   = this.angle;
		bodyDef.position.set(this.point);   
		
		this.body = GLWorld.physicsWorld.world.createBody(bodyDef);  
		this.body.setUserData("Face");
		this.body.createShape(shape);   
		this.body.setMassFromShapes();
	}

	//设置顶点和纹理坐标
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
		this.angle = this.body.getAngle();
		this.point = this.body.getPosition();
		
		/*if(point.x<-radius || point.x>GLConstant.GLWidth+radius || point.y<-radius)
		{
			MainActivity.pool.playLose();
			
			CloudThread.ifCloud = false;
			GLWorld.updateThread.ifUpdateThread = false;
			
			MainActivity.handler.sendEmptyMessage(MainActivity.GAME_ONLOSE);
		}*/
		
		gl.glPushMatrix();
		gl.glTranslatef(point.x, point.y, 0);
		gl.glRotatef((float) Math.toDegrees(angle), 0, 0, 1);
				
		
		gl.glVertexPointer  (2, GL10.GL_FLOAT, 0, vertexPointer);
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
