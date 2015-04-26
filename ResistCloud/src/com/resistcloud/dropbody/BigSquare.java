package com.resistcloud.dropbody;

/*
 * Ͷ���Ĵ�������
 */
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import com.resistcloud.worldbody.GLWorld;

public class BigSquare extends GLBody
{
	//���
	float halfWidth;

	public BigSquare(int texture)
	{
		this.angle   = 0;
		this.halfLength = 1; 
		this.texture = texture;
		this.body    = null;
		this.ifBody  = false;
		this.halfWidth = 1;
		this.point   = new Vec2(2.0f, 7.25f);
		this.style   = GLBody.GLBODY_BIGSQUARE;
		
		this.halfWidth = 0.5f;
		
		addBody();
		setVertexPointer();
		setTexCoordPointer();
	}
		
	//���ö������������
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
	public boolean inGLBody(Vec2 v) 
	{
		Vec2 newV = v.sub(this.point);
		
		if(newV.x>=-halfWidth&&newV.x<=halfWidth&&newV.y>=-halfWidth&&newV.y<=halfWidth)
		{
			this.halfWidth = 1;
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
		
		shape.density     = 1.0f;   //�ܶ�
		shape.friction    = 0.5f;   //Ħ��
		shape.restitution = 0;
		
		shape.setAsBox(1, 1);	
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.angle   = 0;
		bodyDef.position.set(new Vec2(-10, 10)); 
		
		
		this.body = GLWorld.physicsWorld.world.createBody(bodyDef);
		this.body.setUserData("GLBody");
		this.body.createShape(shape);   
		this.body.setMassFromShapes();
		
		this.body.putToSleep();
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
		
		gl.glEnable(GL10.GL_BLEND); // �򿪻��
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
		gl.glDisable(GL10.GL_DEPTH_TEST); // �ر���Ȳ���
		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		
		gl.glDisable(GL10.GL_BLEND); // �رջ��
		gl.glEnable(GL10.GL_DEPTH_TEST); // ����Ȳ���
			
		gl.glPopMatrix();
	}

	
}

