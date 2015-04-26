package com.resistcloud.worldbody;

import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import com.resistcloud.dropbody.GLBody;

/*
 * 三角形墙
 */
public class TriangleWall extends Wall
{
	float h;
	float angle;
	
	public TriangleWall(float x, float y, float h, float angle, int texture)
	{
		this.point   = new Vec2(x, y);
		this.h       = h;
		this.angle   = angle;
		this.texture = texture;
		
		setVertexTexCoord();
		addBody();
	}
	
	public void addBody() 
	{
		PolygonDef shape  = new PolygonDef(); 
							   
		shape.friction    = 0.5f;  
		shape.restitution = 0;  
		
		shape.clearVertices();
		shape.addVertex(new Vec2(h, 0));
		shape.addVertex(new Vec2(0, h));
		shape.addVertex(new Vec2(0, 0));
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.angle   = (float) Math.toRadians(angle);
		bodyDef.position.set(point); 
		
		
		Body body= GLWorld.physicsWorld.world.createBody(bodyDef);
		body.setUserData("Wall");
		body.createShape(shape);   
		body.setMassFromShapes();
	}
	
	//设置顶点和纹理坐标
	public void setVertexTexCoord()
	{
		float newFloatVertex[] = {
				0, h, 0, 0, h, 0,
		}; 
		vertexPointer = GLBody.setPointer(newFloatVertex);

		float newFloatTexCoord[] = {
				0,0,0,h,h,h,
		};
		texCoordPointer = GLBody.setPointer(newFloatTexCoord);
	}
		
	public void drawSelf(GL10 gl) 
	{
		gl.glPushMatrix();
		gl.glTranslatef(point.x, point.y, 0);
		gl.glRotatef(angle, 0, 0, 1);
				
		gl.glVertexPointer  (2, GL10.GL_FLOAT, 0, vertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture    (GL10.GL_TEXTURE_2D, texture);
		gl.glDrawArrays     (GL10.GL_TRIANGLES, 0, 3);
				
		gl.glPopMatrix();
	}
}
