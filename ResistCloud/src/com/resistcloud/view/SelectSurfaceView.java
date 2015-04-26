package com.resistcloud.view;

/*
 * 选择关卡
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
import com.resistcloud.worldbody.GLWorld;

import com.resistcloud.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

public class SelectSurfaceView extends GLSurfaceView 
{
	SelectRenderer mSelectRenderer = null;
	
	public SelectSurfaceView(MainActivity activity)
	{
		super(activity);
		
		mSelectRenderer = new SelectRenderer(activity);
		setRenderer(mSelectRenderer);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		return mSelectRenderer.onTouchDown(event);
	}
}

class SelectRenderer implements Renderer
{
	MainActivity activity;
	
	//纹理
	int[] texture = new int[12];
	
	float roundHalfWidth;
	float roundHalfHeight;
	
	float gapWidth;
	float gapHeight;
	
	//back顶点坐标
	FloatBuffer roundVertexPointer;
	
	float menuHalfWidth;
	float menuHalfHeight;
	
	float menuX;
	float menuY;
	
	float smallWidth;
	
	//backmenu顶点坐标
	FloatBuffer menuVertexPointer;
	//纹理坐标
	FloatBuffer menuTexCoordPointer;
	
	float backHalfWidth;
	float backHalfHeight;
	
	float backX;
	float backY;
	
	//back顶点坐标
	FloatBuffer backVertexPointer;
		
	//纹理坐标
	FloatBuffer texCoordPointer;
	
	int round;
	
	public SelectRenderer(MainActivity activity)
	{
		this.activity = activity;
		
		setFloat();
		setPointer();
		
		SharedPreferences mSharedPreferences = activity.getSharedPreferences("Setting_himi", Context.MODE_PRIVATE);;
		
		round = Integer.parseInt(mSharedPreferences.getString("round", "2"));
		if(round > 9)
		{
			round = 9;
			mSharedPreferences.edit().putString("round", round+"").commit();
		}
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
			
			int i;
			int j;
			
			float roundX;
			float roundY;
			
			for(int k=0;k<10;k++)
			{
				i = k%5;
				j = k/5;
				
				roundX = (i+1)*gapWidth+(2*i+1)*roundHalfWidth;
				roundY = (2-j)*gapHeight+(3-2*j)*roundHalfHeight;
				
				if(v.x>=roundX-roundHalfWidth&&v.x<=roundX+roundHalfWidth
						&&v.y>=roundY-roundHalfHeight&&v.y<=roundY+roundHalfHeight)
				{
					if(k <= round)
					{
						GLWorld.round = k;
						MainActivity.handler.sendEmptyMessage(MainActivity.GAME_START);
					}
					return true;
				}
			}
		}
		
		return true;
	}
	
	public void setFloat()
	{
		roundHalfWidth  = 0.75f;
		roundHalfHeight = 0.75f;
		
		menuHalfWidth  = 0.75f;
		menuHalfHeight = 0.375f;

		gapWidth  = (GLConstant.GLWidth-10*roundHalfWidth)/6;
		gapHeight = (GLConstant.GLHeight-4*roundHalfHeight-2*menuHalfHeight)/4;
		
		menuX = gapWidth + menuHalfWidth;
		menuY = 3*gapHeight + 4*roundHalfHeight + menuHalfHeight;
		
		smallWidth = 0.5f;
		
		backHalfWidth  = 9.0f;
		backHalfHeight = 4.5f;
		
		backX = GLConstant.GLWidth/2;
		backY = GLConstant.GLHeight/2;
	}
	
	public void setPointer()
	{
		float roundFloatVertex[] = {
				-roundHalfWidth, roundHalfHeight,-roundHalfWidth,-roundHalfHeight, roundHalfWidth,-roundHalfHeight,
				-roundHalfWidth, roundHalfHeight, roundHalfWidth,-roundHalfHeight, roundHalfWidth, roundHalfHeight,
		}; 
		roundVertexPointer = GLBody.setPointer(roundFloatVertex);
		
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
		
		float backFloatVertex[] = {
				-backHalfWidth, backHalfHeight,-0.01f,-backHalfWidth,-backHalfHeight,-0.01f, backHalfWidth,-backHalfHeight,-0.01f,
				-backHalfWidth, backHalfHeight,-0.01f, backHalfWidth,-backHalfHeight,-0.01f, backHalfWidth, backHalfHeight,-0.01f,
		}; 
		backVertexPointer = GLBody.setPointer(backFloatVertex);
		
		float floatTexCoord[] = {
				0,0,0,1,1,1,
				0,0,1,1,1,0,
		};
		texCoordPointer = GLBody.setPointer(floatTexCoord);
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
	
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glTranslatef(menuX, menuY, 0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, menuVertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, menuTexCoordPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[10]);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 9);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(backX, backY, 0);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, backVertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[11]);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		gl.glPopMatrix();
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, roundVertexPointer);
		
		int i = 0;
		int j = 0;
		
		for(int k=0;k<10;k++)
		{
			i = k%5;
			j = k/5;
			
			gl.glPushMatrix();
			gl.glTranslatef((i+1)*gapWidth+(2*i+1)*roundHalfWidth, (2-j)*gapHeight+(3-2*j)*roundHalfHeight, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[k]);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
			gl.glPopMatrix();
		}
	}
	
	public void loadGLTextures(GL10 gl) 
	{
		Typeface font = Typeface.create("", Typeface.NORMAL);
		Paint paint   = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTypeface(font);
		paint.setTextSize(100);
		
		Canvas canvas= null;
		
		Bitmap[] mBitmap = new Bitmap[12];
		
		for(int k=0;k<10;k++)
		{
			mBitmap[k] = Bitmap.createBitmap(128,128, Bitmap.Config.ARGB_8888);
			canvas = new Canvas(mBitmap[k]);

			if(k <= round)
			{
				canvas.drawColor(Color.RED);
			}
			else
			{
				canvas.drawColor(Color.GRAY);
			}
			
			canvas.drawText(""+k,35,100,paint);
		}
		
		mBitmap[10] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.backtomenu);
		mBitmap[11] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.menuback);
	
		IntBuffer textureBuffer = IntBuffer.allocate(12);
		gl.glGenTextures(12, textureBuffer);
			
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
		
		texture[6] = textureBuffer.get(6);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[6]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[6], 0);
		
		texture[7] = textureBuffer.get(7);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[7]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[7], 0);
		
		texture[8] = textureBuffer.get(8);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[8]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[8], 0);
		
		texture[9] = textureBuffer.get(9);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[9]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[9], 0);
		
		texture[10] = textureBuffer.get(10);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[10]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[10], 0);
		
		texture[11] = textureBuffer.get(11);
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture[11]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap[11], 0);
	}
}


























