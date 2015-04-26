package com.resistcloud.dropbody;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

import com.resistcloud.worldbody.GLWorld;

/*
 * 投掷的长方体
 */
public class Rectangle extends GLBody
{
	//半宽 半高
	float halfWidth;
	float halfHeight;

	public Rectangle(int texture)
	{
		this.angle   = 0;
		this.texture = texture;
		this.body    = null;
		this.ifBody  = false;
		this.halfLength = 0.4f; 
		this.point   = new Vec2(7.3f, 7.25f);
		this.style   = GLBody.GLBODY_RECTANGLE;
		
		this.halfWidth  = 1.5f;
		this.halfHeight = 0.24f;
		
		addBody();
		setVertexPointer();
		setTexCoordPointer();
	}
			
	//设置顶点和纹理坐标
	public void setVertexPointer()
	{
		float newFloatVertex[] = {
				-halfWidth, halfHeight,-halfWidth,-halfHeight, halfWidth,-halfHeight,
				-halfWidth, halfHeight, halfWidth,-halfHeight, halfWidth, halfHeight,
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
		
		if(newV.x>=-halfWidth&&newV.x<=halfWidth&&newV.y>=-halfHeight&&newV.y<=halfHeight)
		{
			this.halfWidth  = 2.5f;
			this.halfHeight = 0.4f;
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
		
		shape.setAsBox(2.5f, 0.4f);	
		
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
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
				
		gl.glPopMatrix();
	}

}
