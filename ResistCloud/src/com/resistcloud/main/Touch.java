package com.resistcloud.main;

import java.util.Vector;
import org.jbox2d.common.Vec2;

import com.resistcloud.constant.GLConstant;
import com.resistcloud.dropbody.GLBody;
import com.resistcloud.worldbody.Button;
import com.resistcloud.worldbody.GLWorld;

import android.view.MotionEvent;

/*
 * 处理触屏
 */
public class Touch
{
	Vector<GLBody> sleepGlBody;
	Vector<GLBody> wakeGlBody;

	Cloud cloud;
	Button button;

	// touchPoint点击的点
	// distancePoint点击的点与GLBody中心点的向量
	// glBody点击到的GLBody
	public Vec2 touchPoint;
	public Vec2 distancePoint;
	public GLBody body;

	// 是否接受触屏响应
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

	// 当点击向下时的函数
	public boolean onTouchDown(MotionEvent event)
	{
		touchPoint = GLConstant.getVByXY(event.getX(), event.getY());

		if (button.ifInRefrash(touchPoint))
		{
			// 重新开始
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

	// 当移动时的函数
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

	// 当点击向上时的函数
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
