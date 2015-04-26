package com.resistcloud.view;

/*
 * 开始动画
 */
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.resistcloud.constant.Eye;
import com.resistcloud.constant.GLConstant;
import com.resistcloud.dropbody.GLBody;
import com.resistcloud.main.MainActivity;
import com.resistcloud.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class TagSurfaceView extends GLSurfaceView
{
	public TagSurfaceView(MainActivity activity)
	{
		super(activity);
		
		setRenderer(new TagRenderer(activity));
	}
}

class TagRenderer implements Renderer
{
	int texture;
	MainActivity activity;
	
	TagCircle tagCircle = null;
	
	public TagRenderer(MainActivity activity)
	{
		this.activity = activity;
	}
	
	public void loadGLTextures(GL10 gl) 
	{
		Bitmap mBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.face);
		
		IntBuffer textureBuffer = IntBuffer.allocate(1);
		gl.glGenTextures(1, textureBuffer);
			
		texture = textureBuffer.get();
		gl.glBindTexture  (GL10.GL_TEXTURE_2D, texture);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
	}
	
	public void onDrawFrame(GL10 gl) 
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		GLU.gluLookAt(gl,Eye.eyeX,Eye.eyeY,Eye.eyeZ,Eye.centerX,Eye.centerY,Eye.centerZ,0,1,0);
		
		tagCircle.x += 0.1f;
		
		if(tagCircle.x > GLConstant.GLWidth + 1)
		{
			MainActivity.handler.sendEmptyMessage(MainActivity.GAME_MENU);
		}
		
		tagCircle.angle = (float) (tagCircle.x/(2*Math.PI*tagCircle.radius)*360);
		
		tagCircle.drawSelf(gl);
		
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
		
		tagCircle = new TagCircle(-1, 4, texture);
	}
}

class TagCircle
{
	float x;
	float y;
	
	//小球半径
	float radius;
			
	//需要旋转的角度 弧度
	//看似向前滚动
	float angle;
	
	//纹理
	int texture;
	
	//图形中的半宽与半高
	private float halfWidth;
	private float halfHeight;
	
	//顶点坐标 纹理坐标
	FloatBuffer vertexPointer;
	FloatBuffer texCoordPointer;
	
	
	
	public TagCircle(float x, float y, int texture)
	{
		//图形中的半宽与半高
		this.halfWidth  = 0.8f;
		this.halfHeight = 0.8f;
		
		this.x       = x;
		this.y       = y;
		this.radius  = 0.8f;
		this.angle   = 0;
		this.texture = texture;
		
		setPointer();
	}
	
	//设置顶点和纹理坐标
	public void setPointer()
	{
		float newFloatVertex[] = {
				-halfWidth, halfHeight,-halfWidth,-halfHeight, halfWidth,-halfHeight,
				-halfWidth, halfHeight, halfWidth,-halfHeight, halfWidth, halfHeight,
		}; 
			
		float newFloatTexCoord[] = {
				0,0,0,1,1,1,
				0,0,1,1,1,0,
		};
			
		vertexPointer   = GLBody.setPointer(newFloatVertex);
		texCoordPointer = GLBody.setPointer(newFloatTexCoord);
	}
	
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0);
		gl.glRotatef(angle, 0, 0, -1);

		gl.glVertexPointer  (2, GL10.GL_FLOAT, 0, vertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
		//gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, number);
		gl.glEnable(GL10.GL_BLEND); // 打开混合
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
		gl.glDisable(GL10.GL_DEPTH_TEST); // 关闭深度测试
				
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
						
		gl.glDisable(GL10.GL_BLEND); // 关闭混合
		gl.glEnable(GL10.GL_DEPTH_TEST); // 打开深度测试
	
		gl.glPopMatrix();
	}
}






















