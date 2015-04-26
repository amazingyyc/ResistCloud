package com.resistcloud.main;

/*
 * SurfaceView¿‡
 */
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView
{
	MyGLRenderer mGLRenderer = null;

	public MyGLSurfaceView(MainActivity activity)
	{
		super(activity);

		mGLRenderer = new MyGLRenderer(activity);
		setRenderer(mGLRenderer);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return mGLRenderer.onTouchEvent(event);
	}
}
