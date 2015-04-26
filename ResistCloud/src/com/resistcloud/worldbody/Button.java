package com.resistcloud.worldbody;

import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.common.Vec2;

import com.resistcloud.constant.GLConstant;
import com.resistcloud.dropbody.GLBody;

/*
 * 所需的三个按钮
 * 开始
 * 重新开始
 * 回菜单
 * 
 * 在游戏运行时的“菜单” 不是弹出菜单
 */
public class Button 
{
	//中心点
	Vec2 startGamePoint;
	Vec2 refrashPoint;
	Vec2 backmenuPoint;
	
	//半宽
	float halfWidth;
	
	//纹理
	int startGametexture;
	int startGameRedTexture;
	int startGameGrayTexture;
	int refrashTexture;
	int backmenuTexture;
	
	//backm顶点坐标
	FloatBuffer vertexPointer;
	
	//纹理坐标
	FloatBuffer texCoordPointer;
	
	public Button(int startGameRedTexture, int startGameGrayTexture, int refrashTexture, int backmenuTexture)
	{
		this.startGameRedTexture    = startGameRedTexture;
		this.startGametexture       = startGameRedTexture;
		this.startGameGrayTexture   = startGameGrayTexture;
		this.refrashTexture         = refrashTexture;
		this.backmenuTexture        = backmenuTexture;
		
		this.halfWidth = 0.4f;
		
		this.startGamePoint    = new Vec2(GLConstant.GLWidth-2.3f, 7.25f);
		this.refrashPoint      = new Vec2(GLConstant.GLWidth-1.4f, 7.25f);
		this.backmenuPoint     = new Vec2(GLConstant.GLWidth-0.5f, 7.25f);
		
		setPointer();
	}
	
	public boolean ifInStartGame(Vec2 v)
	{
		if(v.x>=startGamePoint.x-halfWidth&&v.x<=startGamePoint.x+halfWidth
				&&v.y>=startGamePoint.y-halfWidth&&v.y<=startGamePoint.y+halfWidth)
		{
			this.startGametexture = this.startGameGrayTexture;
			
			return true;
		}
		else
			return false;
	}
	
	public boolean ifInRefrash(Vec2 v)
	{
		if(v.x>=refrashPoint.x-halfWidth&&v.x<=refrashPoint.x+halfWidth
				&&v.y>=refrashPoint.y-halfWidth&&v.y<=refrashPoint.y+halfWidth)
			return true;
		else
			return false;
	}
	public boolean ifInBackmenu(Vec2 v)
	{
		if(v.x>=backmenuPoint.x-halfWidth&&v.x<=backmenuPoint.x+halfWidth
				&&v.y>=backmenuPoint.y-halfWidth&&v.y<=backmenuPoint.y+halfWidth)
			return true;
		else
			return false;
	}
	
	public void setPointer()
	{
		float vertexFloatVertex[] = {
				-halfWidth, halfWidth,-halfWidth,-halfWidth, halfWidth,-halfWidth,
				-halfWidth, halfWidth, halfWidth,-halfWidth, halfWidth, halfWidth,
		}; 
		vertexPointer = GLBody.setPointer(vertexFloatVertex);
		
		float texCoordFloatTexCoord[] = {
				0,0,0,1,1,1,
				0,0,1,1,1,0,
		};
		texCoordPointer = GLBody.setPointer(texCoordFloatTexCoord);
	}
	
	public void drawSelf(GL10 gl)
	{
		gl.glVertexPointer  (2, GL10.GL_FLOAT, 0, vertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		
		gl.glPushMatrix();
		gl.glTranslatef(startGamePoint.x, startGamePoint.y, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, startGametexture);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(refrashPoint.x, refrashPoint.y, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, refrashTexture);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(backmenuPoint.x, backmenuPoint.y, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, backmenuTexture);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		gl.glPopMatrix();
	}
}
























