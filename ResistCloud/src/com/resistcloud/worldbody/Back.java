package com.resistcloud.worldbody;

import java.nio.FloatBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.common.Vec2;


import com.resistcloud.constant.GLConstant;
import com.resistcloud.dropbody.GLBody;

/*
 * 界面的背景和阴影
 */
public class Back 
{
	Vec2 backPoint;
	Vec2 shadePoint;
	Vec2 smallSquarePoint;
	Vec2 bigSquarePoint;
	Vec2 circlePoint;
	Vec2 trianglePoint;
	Vec2 rectanglePoint;
	
	float backHalfWidth;
	float backHalfHeight;
	float shadeHalfWidth;
	float shadeHalfHeight;
	
	int backTexture;
	int shadeTexture;
	int oneTexture;
	int twoTexture;
	int threeTexture;
	
	Vector<GLBody> sleepGlBody;
	
	//各个组件的个数
	int smallSquareSum = 0;
	int   bigSquareSum = 0;
	int      circleSum = 0;
	int    triangleSum = 0;
	int   rectangleSum = 0;
	
	//顶点坐标 纹理坐标
	FloatBuffer backVertexPointer;
	FloatBuffer shadeVertexPointer;
	FloatBuffer sumVertexPointer;
	FloatBuffer texCoordPointer;
	
	public Back(int backTexture, int shadeTexture, int oneTexture, 
			int twoTexture, int threeTexture, Vector<GLBody> sleepGlBody)
	{
		this.backPoint        = new Vec2(8, 4);
		this.shadePoint       = new Vec2(GLConstant.GLWidth/2, 7.25f);
		this.smallSquarePoint = new Vec2(1.25f, 7.65f);
		this.bigSquarePoint   = new Vec2(2.75f, 7.65f);
		this.circlePoint      = new Vec2(4.25f, 7.65f);
		this.trianglePoint    = new Vec2(5.55f, 7.65f);
		this.rectanglePoint   = new Vec2(9.05f, 7.65f);
		
		this.backHalfWidth   = 10;
		this.backHalfHeight  = 5;
		this.shadeHalfWidth  = GLConstant.GLWidth/2;
		this.shadeHalfHeight = 0.75f;
		
		this.backTexture  = backTexture;
		this.shadeTexture = shadeTexture;
		this.oneTexture   = oneTexture;
		this.twoTexture   = twoTexture;
		this.threeTexture = threeTexture;
		
		this.sleepGlBody = sleepGlBody;
		
		setVertexTexCoord();
	}
	
	private int getTexture(int sum)
	{
		switch(sum)
		{
		case 1:
			return oneTexture;
		case 2:
			return twoTexture;
		case 3:
			return threeTexture;
		default:
			return 0;
		}
	}
	
	//设置顶点和纹理坐标
	public void setVertexTexCoord()
	{	
		float backFloatVertex[] = {
				-backHalfWidth, backHalfHeight,-0.01f,
				-backHalfWidth,-backHalfHeight,-0.01f,
				 backHalfWidth,-backHalfHeight,-0.01f,
				-backHalfWidth, backHalfHeight,-0.01f,
				 backHalfWidth,-backHalfHeight,-0.01f,
				 backHalfWidth, backHalfHeight,-0.01f,
		}; 
		backVertexPointer = GLBody.setPointer(backFloatVertex);
		
		float shadeFloatVertex[] = {
				-shadeHalfWidth, shadeHalfHeight,-0.008f,
				-shadeHalfWidth,-shadeHalfHeight,-0.008f,
				 shadeHalfWidth,-shadeHalfHeight,-0.008f,
				-shadeHalfWidth, shadeHalfHeight,-0.008f,
				 shadeHalfWidth,-shadeHalfHeight,-0.008f,
				 shadeHalfWidth, shadeHalfHeight,-0.008f,
		}; 
		shadeVertexPointer = GLBody.setPointer(shadeFloatVertex);
		
		float sumFloatVertex[] = {
				-0.1f, 0.1f,-0.005f,
				-0.1f,-0.1f,-0.005f,
				 0.1f,-0.1f,-0.005f,
				-0.1f, 0.1f,-0.005f,
				 0.1f,-0.1f,-0.005f,
				 0.1f, 0.1f,-0.005f,
		}; 
		sumVertexPointer = GLBody.setPointer(sumFloatVertex);
					
		float newFloatTexCoord[] = {
				0,0,0,1,1,1,
				0,0,1,1,1,0,
		};
		texCoordPointer = GLBody.setPointer(newFloatTexCoord);
	}
	
	public void getSum()
	{	
		int s = 0;
		int b = 0;
		int c = 0;
		int t = 0;
		int r = 0;
		
		for(int i=0;i<sleepGlBody.size();i++)
		{
			GLBody g = sleepGlBody.get(i);
			
			switch(g.getStyle())
			{
			case GLBody.GLBODY_SMALLSQUARE:
					s++;
				break;
			case GLBody.GLBODY_BIGSQUARE:
					b++;
				break;
			case GLBody.GLBODY_CIRCLE:
					c++;
				break;
			case GLBody.GLBODY_TRIANGLR:
					t++;
				break;
			case GLBody.GLBODY_RECTANGLE:
					r++;
				break;
			}
		}
		
		smallSquareSum = s;
	    bigSquareSum   = b;
		circleSum      = c;
		triangleSum    = t;
		rectangleSum   = r;
	}
	
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glTranslatef     (backPoint.x, backPoint.y, 0);
		gl.glVertexPointer  (3, GL10.GL_FLOAT, 0, backVertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture    (GL10.GL_TEXTURE_2D , backTexture);
		gl.glDrawArrays     (GL10.GL_TRIANGLES  , 0, 6);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef     (shadePoint.x, shadePoint.y, 0);
		gl.glVertexPointer  (3, GL10.GL_FLOAT, 0, shadeVertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture    (GL10.GL_TEXTURE_2D , shadeTexture);
		gl.glDrawArrays     (GL10.GL_TRIANGLES  , 0, 6);
		gl.glPopMatrix();
		
		//得到个数
		getSum();
		
		gl.glVertexPointer  (3, GL10.GL_FLOAT, 0, sumVertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		
		if(smallSquareSum>=1 && smallSquareSum<=3)
		{
			gl.glPushMatrix();
			gl.glTranslatef     (smallSquarePoint.x, smallSquarePoint.y, 0);
			gl.glBindTexture    (GL10.GL_TEXTURE_2D , getTexture(smallSquareSum));
			gl.glDrawArrays     (GL10.GL_TRIANGLES  , 0, 6);
			gl.glPopMatrix();
		}
		
		if(bigSquareSum>=1 && bigSquareSum<=3)
		{
			gl.glPushMatrix();
			gl.glTranslatef     (bigSquarePoint.x, bigSquarePoint.y, 0);
			//gl.glVertexPointer  (3, GL10.GL_FLOAT, 0, sumVertexPointer);
			//gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
			gl.glBindTexture    (GL10.GL_TEXTURE_2D , getTexture(bigSquareSum));
			gl.glDrawArrays     (GL10.GL_TRIANGLES  , 0, 6);
			gl.glPopMatrix();
		}
		
		if(circleSum>=1 && circleSum<=3)
		{
			gl.glPushMatrix();
			gl.glTranslatef     (circlePoint.x, circlePoint.y, 0);
			//gl.glVertexPointer  (3, GL10.GL_FLOAT, 0, sumVertexPointer);
			//gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
			gl.glBindTexture    (GL10.GL_TEXTURE_2D , getTexture(circleSum));
			gl.glDrawArrays     (GL10.GL_TRIANGLES  , 0, 6);
			gl.glPopMatrix();
		}
		
		if(triangleSum>=1 && triangleSum<=3)
		{
			gl.glPushMatrix();
			gl.glTranslatef     (trianglePoint.x, trianglePoint.y, 0);
			//gl.glVertexPointer  (3, GL10.GL_FLOAT, 0, sumVertexPointer);
			//gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
			gl.glBindTexture    (GL10.GL_TEXTURE_2D , getTexture(triangleSum));
			gl.glDrawArrays     (GL10.GL_TRIANGLES  , 0, 6);
			gl.glPopMatrix();
		}
		
		if(rectangleSum>=1 && rectangleSum<=3)
		{
			gl.glPushMatrix();
			gl.glTranslatef     (rectanglePoint.x, rectanglePoint.y, 0);
			//gl.glVertexPointer  (3, GL10.GL_FLOAT, 0, sumVertexPointer);
			//gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
			gl.glBindTexture    (GL10.GL_TEXTURE_2D , getTexture(rectangleSum));
			gl.glDrawArrays     (GL10.GL_TRIANGLES  , 0, 6);
			gl.glPopMatrix();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
