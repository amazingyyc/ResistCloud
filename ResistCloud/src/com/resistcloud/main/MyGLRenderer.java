package com.resistcloud.main;

/*
 * GLSurfaceViewµƒ‰÷»æ¿‡
 * 
 */
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.resistcloud.R;
import com.resistcloud.constant.Eye;
import com.resistcloud.worldbody.GLWorld;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

public class MyGLRenderer implements Renderer
{
	MainActivity activity;
	int[] texture = new int[18];

	GLWorld glWorld = null;

	public MyGLRenderer(MainActivity activity)
	{
		this.activity = activity;
	}

	public void onDrawFrame(GL10 gl)
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		GLU.gluLookAt(gl, Eye.eyeX, Eye.eyeY, Eye.eyeZ, Eye.centerX, Eye.centerY, Eye.centerZ, 0, 1, 0);

		glWorld.drawSelf(gl);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public void onSurfaceChanged(GL10 gl, int w, int h)
	{
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 90.0f, (float) w / (float) h, 0.1f, 100.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LESS);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		loadGLTextures(gl);

		glWorld = new GLWorld(activity, texture);
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			glWorld.touch.onTouchDown(event);
			break;
		case MotionEvent.ACTION_MOVE:
			glWorld.touch.onTouchMove(event);
			break;
		case MotionEvent.ACTION_UP:
			glWorld.touch.onTouchUp(event);
			break;
		}

		return true;
	}

	public void loadGLTextures(GL10 gl)
	{
		GLImage.load(this.activity.getResources());

		IntBuffer textureBuffer = IntBuffer.allocate(18);
		gl.glGenTextures(18, textureBuffer);

		texture[0] = textureBuffer.get();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[0], 0);

		texture[1] = textureBuffer.get(1);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[1], 0);

		texture[2] = textureBuffer.get(2);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[2], 0);

		texture[3] = textureBuffer.get(3);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[3]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[3], 0);

		texture[4] = textureBuffer.get(4);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[4]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[4], 0);

		texture[5] = textureBuffer.get(5);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[5]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[5], 0);

		texture[6] = textureBuffer.get(6);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[6]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[6], 0);

		texture[7] = textureBuffer.get(7);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[7]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[7], 0);

		texture[8] = textureBuffer.get(8);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[8]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[8], 0);

		texture[9] = textureBuffer.get(9);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[9]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[9], 0);

		texture[10] = textureBuffer.get(10);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[10]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[10], 0);

		texture[11] = textureBuffer.get(11);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[11]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[11], 0);

		texture[12] = textureBuffer.get(12);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[12]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[12], 0);

		texture[13] = textureBuffer.get(13);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[13]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[13], 0);

		texture[14] = textureBuffer.get(14);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[14]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[14], 0);

		texture[15] = textureBuffer.get(15);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[15]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[15], 0);

		texture[16] = textureBuffer.get(16);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[16]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[16], 0);

		texture[17] = textureBuffer.get(17);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[17]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap[17], 0);
	}

}

class GLImage
{
	public static Bitmap[] mBitmap = new Bitmap[18];

	public static void load(Resources resources)
	{
		mBitmap[0] = BitmapFactory.decodeResource(resources,
				R.drawable.smallspuare);
		mBitmap[1] = BitmapFactory.decodeResource(resources,
				R.drawable.bigsquare);
		mBitmap[2] = BitmapFactory.decodeResource(resources,
				R.drawable.rectangle);
		mBitmap[3] = BitmapFactory.decodeResource(resources,
				R.drawable.triangle);
		mBitmap[4] = BitmapFactory.decodeResource(resources, R.drawable.circle);

		mBitmap[5] = BitmapFactory.decodeResource(resources, R.drawable.face);
		mBitmap[6] = BitmapFactory.decodeResource(resources, R.drawable.wall);

		mBitmap[7] = BitmapFactory.decodeResource(resources, R.drawable.bullet);

		mBitmap[8] = BitmapFactory.decodeResource(resources, R.drawable.back);
		mBitmap[9] = BitmapFactory.decodeResource(resources, R.drawable.shade);

		mBitmap[10] = BitmapFactory.decodeResource(resources, R.drawable.one);
		mBitmap[11] = BitmapFactory.decodeResource(resources, R.drawable.two);
		mBitmap[12] = BitmapFactory.decodeResource(resources, R.drawable.three);

		mBitmap[13] = BitmapFactory.decodeResource(resources, R.drawable.cloud);

		mBitmap[14] = BitmapFactory.decodeResource(resources,
				R.drawable.startgamered);
		mBitmap[15] = BitmapFactory.decodeResource(resources,
				R.drawable.startgamegray);
		mBitmap[16] = BitmapFactory.decodeResource(resources,
				R.drawable.refrash);
		mBitmap[17] = BitmapFactory.decodeResource(resources,
				R.drawable.backmenu);
	}
}
