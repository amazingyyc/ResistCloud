package com.resistcloud.main;

import java.util.Vector;
import org.jbox2d.common.Vec2;

import com.resistcloud.constant.GLConstant;
import com.resistcloud.dropbody.GLBody;
import com.resistcloud.worldbody.Button;
import com.resistcloud.worldbody.GLWorld;

import android.view.MotionEvent;

/*
 * ������
 */
public class Touch
{
	Vector<GLBody> sleepGlBody;
	Vector<GLBody> wakeGlBody;

	Cloud cloud;
	Button button;

	// touchPoint����ĵ�
	// distancePoint����ĵ���GLBody���ĵ������
	// glBody�������GLBody
	public Vec2 touchPoint;
	public Vec2 distancePoint;
	public GLBody body;

	// �Ƿ���ܴ�����Ӧ
	public boolean ifTouch;

	public Touch(Vector<GLBody> sleepGlBody, Vector<GLBody> wakeGlBody,
			Cloud cloud, Button button)
	{
		this.sleepGlBody = sleepGlBody;
		this.wakeGlBody = wakeGlBody;

		this.cloud = cloud;
		this.button = button;

		this.touchPoint = null;
		this.distancePoint = null;
		this.body = null;

		this.ifTouch = true;
	}

	// ���������ʱ�ĺ���
	public boolean onTouchDown(MotionEvent event)
	{
		touchPoint = GLConstant.getVByXY(event.getX(), event.getY());

		if (button.ifInRefrash(touchPoint))
		{
			// ���¿�ʼ
			CloudThread.ifCloud = false;
			GLWorld.updateThread.ifUpdateThread = false;
			MainActivity.handler.sendEmptyMessage(MainActivity.GAME_START);
			return true;
		}

		if (button.ifInBackmenu(touchPoint))
		{
			CloudThread.ifCloud = false;
			GLWorld.updateThread.ifUpdateThread = false;
			MainActivity.handler.sendEmptyMessage(MainActivity.GAME_MENU);
			return true;
		}

		if (!this.ifTouch)
			return true;

		if (button.ifInStartGame(touchPoint))
		{
			cloud.startMove();
			this.ifTouch = false;

			return true;
		}

		for (int i = 0; i < sleepGlBody.size(); i++)
		{
			if (sleepGlBody.get(i).inGLBody(touchPoint))
			{
				body = sleepGlBody.get(i);
				distancePoint = touchPoint.sub(body.getPoint());

				return true;
			}
		}

		return true;
	}

	// ���ƶ�ʱ�ĺ���
	public boolean onTouchMove(MotionEvent event)
	{
		if (!this.ifTouch)
			return true;

		if (body != null)
		{
			touchPoint = GLConstant.getVByXY(event.getX(), event.getY());
			Vec2 movePoint = touchPoint.sub(distancePoint);

			if (movePoint.y - body.getHalfLength() < 5)
				movePoint.y = 5 + body.getHalfLength();

			body.setPoint(movePoint);
		}

		return true;
	}

	// ���������ʱ�ĺ���
	public boolean onTouchUp(MotionEvent event)
	{
		if (!this.ifTouch)
			return true;

		if (body != null)
		{
			MainActivity.pool.playTouchUp();

			body.wakeUp();

			this.sleepGlBody.remove(body);
			this.wakeGlBody.add(body);
		}

		touchPoint = null;
		distancePoint = null;
		body = null;

		return true;
	}

}
