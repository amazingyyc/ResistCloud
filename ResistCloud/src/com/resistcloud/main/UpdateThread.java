package com.resistcloud.main;

import org.jbox2d.common.Vec2;

import com.resistcloud.constant.GLConstant;
import com.resistcloud.dropbody.GLBody;
import com.resistcloud.worldbody.Face;
import com.resistcloud.worldbody.GLWorld;

/*
 * 物理世界渲染类
 */
public class UpdateThread extends Thread
{
	public boolean ifStep = false;

	public boolean ifUpdateThread;

	GLWorld glWorld = null;
	GLBody b = null;
	Bullet bx = null;

	public UpdateThread(GLWorld glWorld)
	{
		ifUpdateThread = true;
		this.glWorld = glWorld;
	}

	public void run()
	{
		int length = glWorld.face.size();
		Face face = null;

		while (ifUpdateThread)
		{
			ifStep = true;
			GLWorld.physicsWorld.upDate();
			ifStep = false;

			//////////////////////////////////////
			for(int k=0; k<GLWorld.needWakeUp.size(); k++)
			{
				b = GLWorld.needWakeUp.get(k);
				b.body.setXForm(b.point, 0);
				b.body.wakeUp();
				b.ifBody = true;
			}
			
			GLWorld.needWakeUp.clear();
			///////////////////////////////////////////
			
			//////////////////////////////////////
			for(int k=0; k<GLWorld.bulletWakeUp.size(); k++)
			{
				bx = GLWorld.bulletWakeUp.get(k);
				
				bx.body.setXForm(bx.newPoint, 0);
				bx.body.wakeUp();
				bx.body.setLinearVelocity(new Vec2(0, -3));
				bx.ifDrop = true;
			}

			GLWorld.bulletWakeUp.clear();
			///////////////////////////////////////////
			
			for (int i = 0; i < length; i++)
			{
				face = glWorld.face.get(i);

				if (face.point.x < -face.radius
						|| face.point.x > GLConstant.GLWidth + face.radius
						|| face.point.y < -face.radius)
				{
					MainActivity.pool.playLose();

					CloudThread.ifCloud = false;
					GLWorld.updateThread.ifUpdateThread = false;

					MainActivity.handler
							.sendEmptyMessage(MainActivity.GAME_ONLOSE);

					return;
				}
			}

			try
			{
				Thread.sleep((long) (GLWorld.physicsWorld.timeStep * 1000));
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
