package com.resistcloud.main;

/*
 * ��ײ������
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

	// ���������巢����ײʱ��box2d���Զ����ɶ�Ӧ������ײʵ������������ײ���ݣ�ִ����ײ���ӷ�����Add����
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

	// // ��ײ������������������ʱ�䣬��ײ�����غ�ʱ�䳤�ȡ�
	public void persist(ContactPoint cp)
	{
	}

	// //������������ײ���󣬸��嵯�����ջ��Զ������Ƴ���ײ���¼���
	public void remove(ContactPoint cp)
	{
	}

	public void result(ContactResult point)
	{
	}
}
