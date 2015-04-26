package com.resistcloud.view;

/*
 * 菜单选择界面
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

public class MenuSurfaceView extends GLSurfaceView 
{
	MenuRenderer mMenuRenderer = null;
	
	public MenuSurfaceView(MainActivity activity)
	{
		super(activity);
		
		mMenuRenderer = new MenuRenderer(activity);
		setRenderer(mMenuRenderer);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		return mMenuRenderer.onTouchDown(event);
	}
}

class MenuRenderer implements Renderer
{
	MainActivity activity;
	
	//背景的半宽半高
	float backHalfWidth;
	float backHalfHeight;
	
	//背景坐标
	float backX;
	float backY;
	
	//标题
	float titleHalfWidth;
	float titleHalfHeight;
	
	float titleX;
	float titleY;
	
	//菜单的半宽半高
	float menuHalfWidth;
	float menuHalfHeight;
	
	//menu与左下角的距离
	float gapHeight;
	float gapWidth;
	
	//纹理
	int[] texture = new int[6];
	
	//back顶点坐标
	FloatBuffer backVertexPointer;
	
	//title顶点坐标
	FloatBuffer titleVertexPointer;
	
	//menu顶点坐标
	FloatBuffer menuVertexPointer;
		
	//纹理坐标
	FloatBuffer texCoordPointer;
	
	public MenuRenderer(MainActivity activity)
	{
		this.activity = activity;
		
		setFloat();
		setPointer();
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
		
		gl.glEnable(GL10.GL_BLEND); // 打开混合
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
		gl.glDisable(GL10.GL_DEPTH_TEST); // 关闭深度测试
		gl.glPushMatrix();
		gl.glTranslatef(titleX, titleY, 0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, titleVertexPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_BLEND); // 关闭混合
		gl.glEnable(GL10.GL_DEPTH_TEST); // 打开深度测试
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, menuVertexPointer);
		
		for(int k=0;k<4;k++)
		{
			gl.glPushMatrix();
			gl.glTranslatef((k+1)*gapWidth+(2*k+1)*menuHalfWidth, gapHeight+menuHalfHeight, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[k+2]);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
			gl.glPopMatrix();
		}
	}
	
	public void setPointer()
	{
		float backFloatVertex[] = {
				-backHalfWidth, backHalfHeight,-0.01f,-backHalfWidth,-backHalfHeight,-0.01f, backHalfWidth,-backHalfHeight,-0.01f,
				-backHalfWidth, backHalfHeight,-0.01f, backHalfWidth,-backHalfHeight,-0.01f, backHalfWidth, backHalfHeight,-0.01f,
		}; 
		backVertexPointer = GLBody.setPointer(backFloatVertex);
		
		float titleFloatVertex[] = {
				-titleHalfWidth, titleHalfHeight,-titleHalfWidth,-titleHalfHeight, titleHalfWidth,-titleHalfHeight,
				-titleHalfWidth, titleHalfHeight, titleHalfWidth,-titleHalfHeight, titleHalfWidth, titleHalfHeight,
		}; 
		titleVertexPointer = GLBody.setPointer(titleFloatVertex);
		
		float menuFloatVertex[] = {
				-menuHalfWidth, menuHalfHeight,-menuHalfWidth,-menuHalfHeight, menuHalfWidth,-menuHalfHeight,
				-menuHalfWidth, menuHalfHeight, menuHalfWidth,-menuHalfHeight, menuHalfWidth, menuHalfHeight,
		}; 
		menuVertexPointer = GLBody.setPointer(menuFloatVertex);
		
		float floatTexCoord[] = {
				0,0,0,1,1,1,
				0,0,1,1,1,0,
		};
		texCoordPointer = GLBody.setPointer(floatTexCoord);
	}
	
	//设置各个变量的值
	public void setFloat()
	{
		backHalfWidth  = 9.0f;
		backHalfHeight = 4.5f;
		
		backX = GLConstant.GLWidth/2;
		backY = GLConstant.GLHeight/2;
		
		/*titleHalfWidth  = 3.1f;
		titleHalfHeight = 0.8f;*/
		titleHalfWidth  = 3.1f;
		titleHalfHeight = 3.1f;
		
		titleX = GLConstant.GLWidth/2;
		titleY = 5.4f;
		
		menuHalfWidth  = 1.0f;
		menuHalfHeight = 0.4f;
		
		gapHeight = 1.9f;
		gapWidth  = (GLConstant.GLWidth-8*menuHalfWidth)/5;
	}
	
	public boolean onTouchDown(MotionEvent event)
	{
		if(event.getAction() ==  MotionEvent.ACTION_DOWN)
		{
			Vec2 v = GLConstant.getVByXY(event.getX(), event.getY());
			
			for(int k=0;k<4;k++)
			{
				float x = (k+1)*gapWidth+2*k*menuHalfWidth;
				float y = gapHeight;
				
				if(v.x>=x&&v.x<=x+2*menuHalfWidth&&v.y>=y&&v.y<=y+2*menuHalfHeight)
				{
					switch(k)
					{
					case 0:
						MainActivity.handler.sendEmptyMessage(MainActivity.GAME_SELECT);
						return true;
					case 1:
						MainActivity.handler.sendEmptyMessage(MainActivity.GAME_SET);
						return true;
					case 2:
						MainActivity.handler.sendEmptyMessage(MainActivity.GAME_HELP);
						return true;
					case 3:
						System.exit(0);
						return true;
					}
				}
			}
			
			return true;
		}
		
		return true;
	}
	
	public void loadGLTextures(GL10 gl) 
	{
		Bitmap[] mBitmap = new Bitmap[6];
		
	    mBitmap[0] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.menuback);
	    mBitmap[1] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.title);
	    mBitmap[2] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.start);
	    mBitmap[3] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.set);
	    mBitmap[4] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.help);
	    mBitmap[5] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.exit);
		
		IntBuffer textureBuffer = IntBuffer.allocate(6);
		gl.glGenTextures(6, textureBuffer);
			
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
		
		texture[3] = textureBuffer.get(3);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[3]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[3], 0);
		
		texture[4] = textureBuffer.get(4);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[4]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[4], 0);
		
		texture[5] = textureBuffer.get(5);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[5]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[5], 0);
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
}
