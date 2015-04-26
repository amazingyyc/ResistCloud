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
 * ���ƶ��µ��ӵ���
 */
public class Bullet
{
	//ͼ���еİ������
	private float halfWidth;
	private float halfHeight;
	
	Vec2 point;
	Vec2 newPoint;
	
	float radius;
	float angle;

	Body body;

	int texture;

	// �Ƿ񱻶���
	boolean ifDrop;

	// �������� ��������
	FloatBuffer vertexPointer;
	FloatBuffer texCoordPointer;

	int number;

	public Bullet(int texture)
	{
		//ͼ���еİ������
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
		shape.density = 0.1f; // �ܶ�
		shape.friction = 0.1f; // Ħ��
		shape.restitution = 0.5f; // ����
		shape.radius = this.radius; // �뾶

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

	// ���ö������������
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
			gl.glEnable(GL10.GL_BLEND); // �򿪻��
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
			gl.glDisable(GL10.GL_DEPTH_TEST); // �ر���Ȳ���
			
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
			
			gl.glDisable(GL10.GL_BLEND); // �رջ��
			gl.glEnable(GL10.GL_DEPTH_TEST); // ����Ȳ���
	
			gl.glPopMatrix();
		}
	}

}
