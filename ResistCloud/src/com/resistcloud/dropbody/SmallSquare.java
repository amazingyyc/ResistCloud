package com.resistcloud.dropbody;

import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

import com.resistcloud.worldbody.GLWorld;


/*
 * 投掷小正方体
 */

public class SmallSquare extends GLBody
{
	//半宽
	float halfWidth;

	public SmallSquare(int texture)
	{
		this.angle      = 0;
		this.halfLength = 0.6f;
		this.texture    = texture;
		this.body       = null;
		this.ifBody     = false;
		this.point      = new Vec2(0.5f, 7.25f);
		this.style      = GLBody.GLBODY_SMALLSQUARE;
		
		this.halfWidth = 0.5f;
	
		addBody();
		setVertexPointer();
		setTexCoordPointer();
	}
	
	@Override
	public boolean inGLBody(Vec2 v) 
	{	
		Vec2 newV = v.sub(this.point);
		
		if(newV.x>=-halfWidth&&newV.x<=halfWidth&&newV.y>=-halfWidth&&newV.y<=halfWidth)
		{
			this.halfWidth = 0.6f;
			setVertexPointer();
			
			return true;
		}
		else
			return false;
	}

	@Override
	public void addBody() 
	{
		PolygonDef shape  = new PolygonDef(); 
		   
		shape.density     = 0.1f;   //密度
		shape.friction    = 0.5f;   //摩擦
		shape.restitution = 0;  
		
		shape.setAsBox(0.6f, 0.6f);	
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.angle   = 0;
		bodyDef.position.set(new Vec2(-10, 10)); 
		
		this.body = GLWorld.physicsWorld.world.createBody(bodyDef);
		this.body.setUserData("GLBody");
		this.body.createShape(shape);
		this.body.setMassFromShapes();
		
		this.body.putToSleep();
	}

	
	public void setVertexPointer()
	{
		float newFloatVertex[] = {
				-halfWidth, halfWidth,-halfWidth,-halfWidth, halfWidth,-halfWidth,
				-halfWidth, halfWidth, halfWidth,-halfWidth, halfWidth, halfWidth,
		}; 
		vertexPointer = GLBody.setPointer(newFloatVertex);
	}
	
	public void setTexCoordPointer()
	{
		float newFloatTexCoord[] = {
				0,0,0,1,1,1,
				0,0,1,1,1,0,
		};
		texCoordPointer = GLBody.setPointer(newFloatTexCoord);
	}

	
	@Override
	public void drawSelf(GL10 gl) 
	{
		if(this.ifBody)
		{
			this.angle = this.body.getAngle();
			this.point = this.body.getPosition();
		}
		
		gl.glPushMatrix();
		gl.glTranslatef(point.x, point.y, 0);
		gl.glRotatef((float) Math.toDegrees(angle), 0, 0, 1);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
		gl.glEnable(GL10.GL_BLEND); // 打开混合
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
		gl.glDisable(GL10.GL_DEPTH_TEST); // 关闭深度测试
		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		
		gl.glDisable(GL10.GL_BLEND); // 关闭混合
		gl.glEnable(GL10.GL_DEPTH_TEST); // 打开深度测试
		
		gl.glPopMatrix();
	}

}


























