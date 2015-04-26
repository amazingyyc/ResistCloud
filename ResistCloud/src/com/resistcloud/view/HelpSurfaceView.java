package com.resistcloud.view;

/*
 * 帮助界面
 */
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.common.Vec2;

import com.resistcloud.constant.Eye;
import com.resistcloud.constant.GLConstant;
import com.resistcloud.dropbody.GLBody;
import com.resistcloud.main.MainActivity;

import com.resistcloud.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

public class HelpSurfaceView extends GLSurfaceView 
{
	HelpRenderer mHelpRenderer = null;
	
	public HelpSurfaceView(MainActivity activity)
	{
		super(activity);
		
		mHelpRenderer = new HelpRenderer(activity);
		setRenderer(mHelpRenderer);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		return mHelpRenderer.onTouchDown(event);
	}
}

class HelpRenderer implements Renderer
{
	MainActivity activity;
	
	//纹理
	int[] texture = new int[3];
	
	float backHalfWidth;
	float backHalfHeight;
	
	float backX;
	float backY;
	
	//backm顶点坐标
	FloatBuffer backVertexPointer;
	//纹理坐标
	FloatBuffer texCoordPointer;
	
	float menuHalfWidth;
	float menuHalfHeight;
	
	float menuX;
	float menuY;
	
	float smallWidth;
	
	//backmenu顶点坐标
	FloatBuffer menuVertexPointer;
	//纹理坐标
	FloatBuffer menuTexCoordPointer;
	
	float helpTextWidth;
	float helpTextHeight;
	
	float helpTextX;
	float helpTextY;
	
	FloatBuffer helpVertexPointer;
	
	public HelpRenderer(MainActivity activity)
	{
		this.activity = activity;
		
		setFloat();
		setPointer();
	}
	
	public boolean onTouchDown(MotionEvent event)
	{
		if(event.getAction() ==  MotionEvent.ACTION_DOWN)
		{
			Vec2 v = GLConstant.getVByXY(event.getX(), event.getY());
			
			//点击返回
			if(v.x>=menuX-menuHalfWidth&&v.x<=menuX+menuHalfWidth
					&&v.y>=menuY-menuHalfHeight&&v.y<=menuY+menuHalfHeight)
			{
				//返回菜单
				MainActivity.handler.sendEmptyMessage(MainActivity.GAME_MENU);
				
				return true;
			}
		}
		
		return true;
	}
	
	public void setFloat()
	{
		backHalfWidth  = 9.0f;
		backHalfHeight = 4.5f;
		
		backX = GLConstant.GLWidth/2;
		backY = GLConstant.GLHeight/2;
		
		menuHalfWidth  = 0.75f;
		menuHalfHeight = 0.375f;
		
		smallWidth = 0.5f;
		
		//helpTextWidth  = 5.0f;
		helpTextWidth  = 5.0f;
		helpTextHeight = 5.0f;
		
		menuX = (GLConstant.GLWidth-2*helpTextWidth)/2 + menuHalfWidth;
		menuY = 7.125f;
		
		helpTextX = GLConstant.GLWidth/2;
		helpTextY = 3.25f;
	}

	public void setPointer()
	{
		float backFloatVertex[] = {
				-backHalfWidth, backHalfHeight,-0.01f,-backHalfWidth,-backHalfHeight,-0.01f, backHalfWidth,-backHalfHeight,-0.01f,
				-backHalfWidth, backHalfHeight,-0.01f, backHalfWidth,-backHalfHeight,-0.01f, backHalfWidth, backHalfHeight,-0.01f,
		}; 
		backVertexPointer = GLBody.setPointer(backFloatVertex);
		
		float helpFloatVertex[] = {
				-helpTextWidth, helpTextHeight,-helpTextWidth,-helpTextHeight, helpTextWidth,-helpTextHeight,
				-helpTextWidth, helpTextHeight, helpTextWidth,-helpTextHeight, helpTextWidth, helpTextHeight,
		}; 
		helpVertexPointer = GLBody.setPointer(helpFloatVertex);
		
		float floatTexCoord[] = {
				0,0,0,1,1,1,
				0,0,1,1,1,0,
		};
		texCoordPointer = GLBody.setPointer(floatTexCoord);
		
		float menuFloatVertex[] = {
				-smallWidth,menuHalfHeight,-menuHalfWidth,0,-smallWidth,-menuHalfHeight,
				-smallWidth,menuHalfHeight,-smallWidth,-menuHalfHeight,menuHalfWidth,-menuHalfHeight,
				-smallWidth,menuHalfHeight,menuHalfWidth,-menuHalfHeight,menuHalfWidth,menuHalfHeight,
		}; 
		menuVertexPointer = GLBody.setPointer(menuFloatVertex);
		
		float menuFloatTexCoord[] = {
				(menuHalfWidth-smallWidth)/(2*menuHalfWidth),0,0,0.5f,(menuHalfWidth-smallWidth)/(2*menuHalfWidth),1,
				(menuHalfWidth-smallWidth)/(2*menuHalfWidth),0,(menuHalfWidth-smallWidth)/(2*menuHalfWidth),1,1,1,
				(menuHalfWidth-smallWidth)/(2*menuHalfWidth),0,1,1,1,0,
		};
		menuTexCoordPointer = GLBody.setPointer(menuFloatTexCoord);
	}
	
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glTranslatef(backX, backY, 0);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, backVertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(menuX, menuY, 0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, menuVertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, menuTexCoordPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 9);
		gl.glPopMatrix();
		
		//gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, number);
		gl.glEnable(GL10.GL_BLEND); // 打开混合
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
		gl.glDisable(GL10.GL_DEPTH_TEST); // 关闭深度测试
				
		gl.glPushMatrix();
		gl.glTranslatef(helpTextX, helpTextY, 0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, helpVertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		gl.glPopMatrix();
		
		gl.glDisable(GL10.GL_BLEND); // 关闭混合
		gl.glEnable(GL10.GL_DEPTH_TEST); // 打开深度测试
	}
	
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		GLU.gluLookAt(gl,Eye.eyeX,Eye.eyeY,Eye.eyeZ,Eye.centerX,Eye.centerY,Eye.centerZ,0,1,0);
		
		drawSelf(gl);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	public void onSurfaceChanged(GL10 gl, int w, int h) 
	{
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 90.0f,(float)w/(float)h,0.1f,100.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);	
		gl.glLoadIdentity();
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE);					
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);				
		gl.glClearDepthf(1.0f);									
		gl.glDepthFunc(GL10.GL_LESS);								
		gl.glEnable(GL10.GL_DEPTH_TEST);							
		gl.glShadeModel(GL10.GL_SMOOTH);							
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		loadGLTextures(gl);
	}
	
	public void loadGLTextures(GL10 gl) 
	{
		Bitmap[] mBitmap = new Bitmap[3];
		
		mBitmap[0] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.menuback);
		mBitmap[1] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.backtomenu);
		mBitmap[2] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.helptext);
		
		IntBuffer textureBuffer = IntBuffer.allocate(3);
		gl.glGenTextures(3, textureBuffer);
		
		texture[0] = textureBuffer.get();
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[0]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[0], 0);
		
		texture[1] = textureBuffer.get(1);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[1]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[1], 0);
		
		texture[2] = textureBuffer.get(2);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[2]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[2], 0);
	}
}













