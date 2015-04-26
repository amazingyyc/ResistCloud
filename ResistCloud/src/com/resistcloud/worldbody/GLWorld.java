package com.resistcloud.worldbody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.khronos.opengles.GL10;

import com.resistcloud.dropbody.BigSquare;
import com.resistcloud.dropbody.Circle;
import com.resistcloud.dropbody.GLBody;
import com.resistcloud.dropbody.Rectangle;
import com.resistcloud.dropbody.SmallSquare;
import com.resistcloud.dropbody.Triangle;
import com.resistcloud.main.Bullet;
import com.resistcloud.main.Cloud;
import com.resistcloud.main.MainActivity;
import com.resistcloud.main.PhysicsWorld;
import com.resistcloud.main.Touch;
import com.resistcloud.main.UpdateThread;

import android.content.res.AssetManager;

/*
 * openGL世界类
 */
public class GLWorld 
{
	public Vector<GLBody> sleepGlBody = new Vector<GLBody>();
	public Vector<GLBody> wakeGlBody  = new Vector<GLBody>();
	public Vector<Wall>     wall      = new Vector<Wall>();
	public Vector<Face>     face      = new Vector<Face>();
	
	
	//包含需要唤醒的物体
	public static Vector<GLBody> needWakeUp = new Vector<GLBody>();
	
	//包含需要唤醒的雨滴
	public static Vector<Bullet> bulletWakeUp = new Vector<Bullet>();
	
	
	public Back  back    = null;
	public Cloud cloud   = null;
	public Button button = null;
	public Touch touch   = null;

	MainActivity activity;
	
	//纹理
	int[] texture;
	
	//关数
	public static int round = 0;
	
	//渲染线程
	public static UpdateThread updateThread = null;
	
	//模拟物理世界
	public static PhysicsWorld physicsWorld = null;
	
	public GLWorld(MainActivity activity, int[] texture)
	{
		this.activity     = activity;
		this.texture      = texture;
		
		physicsWorld = new PhysicsWorld();
		updateThread = new UpdateThread(GLWorld.this);
		cloud        = new Cloud(-1, 7, texture[13], texture[7]);
		button       = new Button(texture[14], texture[15], texture[16], texture[17]);
		touch        = new Touch(sleepGlBody, wakeGlBody, cloud, button);
		back         = new Back(texture[8], texture[9], texture[10], texture[11], texture[12], sleepGlBody);
		
		readWorld();
		
		updateThread.start();
	}
	
	//读取地图
	public void readWorld()
	{
		InputStream inputStream = null;
		
		AssetManager am = this.activity.getResources().getAssets();
		try 
		{
			if(round < 0 || round > 9)
				System.exit(1);
			
			inputStream =  am.open("world/world"+round);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		sleepGlBody.clear();
		wall       .clear();
		face       .clear();
		
		try 
		{
			String line = null;
			
			while((line = br.readLine()) != null)
			{
				if(line.trim().length() <= 0 || line.startsWith("/"))
				{
					continue;
				}
				
				String part[] = line.trim().split("\\s+");
				
				int style = Integer.valueOf(part[0]);
				
				float number;
				float x;
				float y;
				float halfWidth;
				float halfHeight;
				float h;
				float angle;
				
				switch(style)
				{
				case 0:
					number = Integer.valueOf(part[1]);
					
					for(int i=0;i<number&&i<4;i++)
						sleepGlBody.add(new SmallSquare(texture[0]));
					break;
				case 1:
					number = Integer.valueOf(part[1]);
					
					for(int i=0;i<number&&i<4;i++)
						sleepGlBody.add(new BigSquare(texture[1]));
					break;
				case 2:
					number = Integer.valueOf(part[1]);
					
					for(int i=0;i<number&&i<4;i++)
						sleepGlBody.add(new Circle(texture[4]));
					break;
				case 3:
					number = Integer.valueOf(part[1]);
					
					for(int i=0;i<number&&i<4;i++)
						sleepGlBody.add(new Triangle(texture[3]));
					break;
				case 4:
					number = Integer.valueOf(part[1]);
					
					for(int i=0;i<number&&i<4;i++)
						sleepGlBody.add(new Rectangle(texture[2]));
					break;
				case 5:
					x = Float.valueOf(part[1]);
					y = Float.valueOf(part[2]);
					
					face.add(new Face(x, y, texture[5]));
					break;
				case 6:
					x          = Float.valueOf(part[1]);
					y          = Float.valueOf(part[2]);
					halfWidth  = Float.valueOf(part[3]);
					halfHeight = Float.valueOf(part[4]);
					
					wall.add(new RectangleWall(x, y, halfWidth, halfHeight, texture[6]));
					break;
				case 7:
					x     = Float.valueOf(part[1]);
					y     = Float.valueOf(part[2]);
					h     = Float.valueOf(part[3]);
					angle = Float.valueOf(part[4]);
					
					wall.add(new TriangleWall(x, y, h, angle, texture[6]));
					break;
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void drawSelf(GL10 gl)
	{
		back.drawSelf(gl);
		
		for(int i=0;i<wall.size();i++)
			wall.get(i).drawSelf(gl);
		
		for(int i=0;i<wakeGlBody.size();i++)
			wakeGlBody.get(i).drawSelf(gl);
		
		for(int i=0;i<sleepGlBody.size();i++)
			sleepGlBody.get(i).drawSelf(gl);
	
		for(int i=0;i<face.size();i++)
			face.get(i).drawSelf(gl);

		button.drawSelf(gl);
		
		cloud.drawSelf(gl);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
