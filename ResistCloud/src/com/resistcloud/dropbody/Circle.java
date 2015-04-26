package com.resistcloud.dropbody;

/*
 * Ͷ����Բ
 */


import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

import com.resistcloud.worldbody.GLWorld;

public class Circle extends GLBody 
{
	//ͼ���еİ������
	private float halfWidth;
	private float halfHeight;
	
	float radius;
	
	int number;
	
	public Circle(int texture)
	{
		//ͼ���еİ������
		this.halfWidth  = 0.6f;
		this.halfHeight = 0.6f;
		
		
		this.halfLength = 0.6f;
		
		this.angle   = 0;
		this.texture = texture;
		this.body    = null;
		this.ifBody  = false;
		this.point   = new Vec2(3.5f, 7.25f);
		this.style   = GLBody.GLBODY_CIRCLE;
		
		this.radius     = 0.5f;
		
		addBody();
		setPointer();
	}
	
	@Override
	public boolean inGLBody(Vec2 v) 
	{
		if(GLBody.getLong(v, this.point) <= this.radius)
		{
			this.radius = 0.6f;
			setPointer();
			
			return true;
		}
		else
			return false;
	}

	@Override
	public void addBody() 
	{
		CircleDef shape   = new CircleDef();   
		shape.density     = 0.1f;   //�ܶ�
		shape.friction    = 0.5f;   //Ħ��
		shape.restitution = 0.0f;   //����
		shape.radius      = 0.6f;   //�뾶
		  
		BodyDef bodyDef = new BodyDef();  
		bodyDef.angle   = 0;
		bodyDef.position.set(new Vec2(-10, 10));   
		
		this.body = GLWorld.physicsWorld.world.createBody(bodyDef);  
		this.body.setUserData("Circle");
		this.body.createShape(shape);   
		this.body.setMassFromShapes();  
		
		this.body.putToSleep();
	}
	
	
	//���ö������������
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
				
		gl.glVertexPointer  (2, GL10.GL_FLOAT, 0, vertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);

		gl.glEnable(GL10.GL_BLEND); // �򿪻��
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
		gl.glDisable(GL10.GL_DEPTH_TEST); // �ر���Ȳ���
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
		
		gl.glDisable(GL10.GL_BLEND); // �رջ��
		gl.glEnable(GL10.GL_DEPTH_TEST); // ����Ȳ���

		gl.glPopMatrix();
	}


}
