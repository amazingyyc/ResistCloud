package com.resistcloud.main;

/*
 * 碰撞监听类
 */
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import com.resistcloud.worldbody.GLWorld;

public class MyContactListener implements ContactListener
{
	public MyContactListener()
	{
		super();
	}

	// 在两个刚体发生碰撞时，box2d会自动生成对应两个碰撞实体所产生的碰撞数据，执行碰撞增加方法（Add）。
	public void add(ContactPoint cp)
	{
		Body b1 = cp.shape1.getBody();
		Body b2 = cp.shape2.getBody();

		if ((b1.getUserData() == "Bullet" && b2.getUserData() == "Face")
				|| (b1.getUserData() == "Face" && b2.getUserData() == "Bullet"))
		{
			MainActivity.pool.playLose();

			CloudThread.ifCloud = false;
			GLWorld.updateThread.ifUpdateThread = false;
			MainActivity.handler.sendEmptyMessage(MainActivity.GAME_ONLOSE);

			return;
		}

		if ((b1.getUserData()=="Bullet" && b2.getUserData()!="Bullet") || 
		    (b2.getUserData()=="Bullet" && b1.getUserData()!="Bullet"))
		{
			MainActivity.pool.playBullet();

			return;
		}
		
		if ((b1.getUserData() == "GLBody" && b2.getUserData() == "Wall")
				|| (b1.getUserData() == "Wall" && b2.getUserData() == "GLBody"))
		{
			MainActivity.pool.playGLBody();

			return;
		}

		if ((b1.getUserData() == "Circle" && b2.getUserData() == "Wall")
				|| (b1.getUserData() == "Wall" && b2.getUserData() == "Circle"))
		{
			MainActivity.pool.playCircle();

			return;
		}
	}

	// // 碰撞的两个刚体所持续的时间，碰撞点所重合时间长度。
	public void persist(ContactPoint cp)
	{
	}

	// //在两个刚体碰撞过后，刚体弹开，刚会自动触发移除碰撞点事件。
	public void remove(ContactPoint cp)
	{
	}

	public void result(ContactResult point)
	{
	}
}
