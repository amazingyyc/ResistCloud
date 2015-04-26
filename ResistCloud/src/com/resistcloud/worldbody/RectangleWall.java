package com.resistcloud.worldbody;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import com.resistcloud.dropbody.GLBody;

/*
 * 矩形的墙壁
 */
public class RectangleWall extends Wall
{
	float halfWidth;
	float halfHeight;
	
	public RectangleWall(float x, float y, float halfWidth, float halfHeight, int texture)
	{
		this.point      = new Vec2(x, y);
		this.halfWidth  = halfWidth;
		this.halfHeight = halfHeight;
		this.texture    = texture;
		
		setVertexTexCoord();
		
		addBody();
	}
	
	public void addBody()
	{
		PolygonDef shape  = new PolygonDef(); 
					   
		shape.friction    = 0.5f;  
		shape.restitution = 0;  
		
		shape.setAsBox(this.halfWidth, this.halfHeight);	
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(this.point); 
		
		
		Body body = GLWorld.physicsWorld.world.createBody(bodyDef); 
		body.setUserData("Wall");
		body.createShape(shape);   
		body.setMassFromShapes();
	}
	
	//设置顶点和纹理坐标
	public void setVertexTexCoord()
	{		
		float newFloatVertex[] = {
				-halfWidth, halfHeight,-halfWidth,-halfHeight, halfWidth,-halfHeight,
				-halfWidth, halfHeight, halfWidth,-halfHeight, halfWidth, halfHeight,
		}; 
		vertexPointer = GLBody.setPointer(newFloatVertex);
					
		/*float newFloatTexCoord[] = {
				0,0,0,1,1,1,
				0,0,1,1,1,0,
		};*/
		float newFloatTexCoord[] = {
				0,0,0,2*this.halfHeight,2*this.halfWidth,2*this.halfHeight,
				0,0,2*this.halfWidth,2*this.halfHeight,2*this.halfWidth,0,
		};
		
		texCoordPointer = GLBody.setPointer(newFloatTexCoord);
	}
	
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glTranslatef(point.x, point.y, 0);
				
		gl.glVertexPointer  (2, GL10.GL_FLOAT, 0, vertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture    (GL10.GL_TEXTURE_2D, texture);
		gl.glDrawArrays     (GL10.GL_TRIANGLES, 0, 6);
				
		gl.glPopMatrix();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
