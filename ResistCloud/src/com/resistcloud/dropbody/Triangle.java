package com.resistcloud.dropbody;

/*
 * 投掷的三角形
 */
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

import com.resistcloud.worldbody.GLWorld;


public class Triangle extends GLBody
{
	//半宽
	float a;

	public Triangle(int texture)
	{
		this.angle   = 0;
		this.texture = texture;
		this.body    = null;
		this.ifBody  = false;
		this.point   = new Vec2(4.8f, 7.25f);
		this.style   = GLBody.GLBODY_TRIANGLR;
		this.halfLength = 0.5f;
		
		
		this.a = 0.3f;
		
		addBody();
		setVertexPointer();
		setTexCoordPointer();
	}

	
	//设置顶点和纹理坐标
	public void setVertexPointer()
	{
		float newFloatVertex[] = {
				-a,2*a,-a,-a,2*a,-a,
		}; 
		vertexPointer = GLBody.setPointer(newFloatVertex);
	}
			
	public void setTexCoordPointer()
	{
		float newFloatTexCoord[] = {
				0,0,0,1,1,1,
		};
		texCoordPointer = GLBody.setPointer(newFloatTexCoord);
	}
			
	@Override
	public boolean inGLBody(Vec2 v) 
	{
		Vec2 newV = v.sub(this.point);
		
		if(newV.x>=-a&&newV.y>=-a&&a-newV.x>=newV.y)
		{
			this.a = 0.5f;
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
		
		shape.clearVertices();
		shape.addVertex(new Vec2(1, -0.5f));
		shape.addVertex(new Vec2(-0.5f, 1));
		shape.addVertex(new Vec2(-0.5f, -0.5f));
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.angle   = 0;
		bodyDef.position.set(new Vec2(-10, 10)); 
		
		
		this.body= GLWorld.physicsWorld.world.createBody(bodyDef);
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
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
				
		gl.glPopMatrix();
	}

}
