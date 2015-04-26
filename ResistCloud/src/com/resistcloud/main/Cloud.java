package com.resistcloud.main;

import java.nio.FloatBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.common.Vec2;

import com.resistcloud.constant.GLConstant;
import com.resistcloud.dropbody.GLBody;
import com.resistcloud.worldbody.GLWorld;

/*
 * ����
 * ��������Բһ�����������
 */

public class Cloud
{
	//ͼ���еİ������
	private float halfWidth;
	private float halfHeight;

	// λ��
	Vec2 point;

	// �뾶
	float radius;

	// ���
	//float halfWidth;

	// ����
	int cloudTexture;
	int bulletTexture;

	// �������� ��������
	/*FloatBuffer squareVertexPointer;
	FloatBuffer squareTexCoordPointer;

	FloatBuffer circleVertexPointer;
	FloatBuffer circleTexCoordPointer;*/
	//�������� ��������
	
	FloatBuffer vertexPointer;
	FloatBuffer texCoordPointer;

	// �������
	int number;

	// ��¼�ӵ��Ƿ��Ѿ�����
	Vector<Bullet> wakeBullet  = null;
	Vector<Bullet> sleepBullet = null;

	// �ӵ��ĸ���
	final int countBullet = 16;

	// �ƶ��߳�
	CloudThread cloudThread = null;

	public Cloud(float x, float y, int cloudTexture, int bulletTexture)
	{
		//ͼ���еİ������
		this.halfWidth  = 1.0f;
		this.halfHeight = 1.0f;
		
		this.point = new Vec2(x, y);

		this.cloudTexture  = cloudTexture;
		this.bulletTexture = bulletTexture;

		this.radius = 0.5f;
		//this.halfWidth = 0.5f;

		this.cloudThread = new CloudThread(Cloud.this);

		setVector();
		setPointer();
	}

	// ��ʼ�ƶ��߳�
	public void startMove()
	{
		this.cloudThread.start();
	}

	public void setVector()
	{
		wakeBullet  = new Vector<Bullet>();
		sleepBullet = new Vector<Bullet>();

		wakeBullet .clear();
		sleepBullet.clear();

		for (int i = 0; i < countBullet; i++)
			sleepBullet.add(new Bullet(bulletTexture));
	}

	// �����ӵ�
	public void drop()
	{

		Bullet bullet1;
		Bullet bullet2;

		if (sleepBullet.size() < 2)
		{
			bullet1 = wakeBullet.remove(0);
			bullet2 = wakeBullet.remove(0);

			bullet1.body.putToSleep();
			bullet2.body.putToSleep();

			sleepBullet.add(bullet1);
			sleepBullet.add(bullet2);
		}

		Vec2 v1 = this.point.sub(new Vec2(0.5f, 0.5f));
		Vec2 v2 = this.point.sub(new Vec2(-0.5f, 0.5f));

		int size = sleepBullet.size();

		bullet1 = sleepBullet.remove(size - 1);
		bullet2 = sleepBullet.remove(size - 2);

		bullet1.wakeUp(v1);
		bullet2.wakeUp(v2);

		wakeBullet.add(bullet1);
		wakeBullet.add(bullet2);
	}

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
		gl.glTranslatef(point.x, point.y, 0);
		
		gl.glVertexPointer  (2, GL10.GL_FLOAT, 0, vertexPointer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordPointer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, cloudTexture);

		gl.glEnable(GL10.GL_BLEND); // �򿪻��
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
		gl.glDisable(GL10.GL_DEPTH_TEST); // �ر���Ȳ���
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
				
		gl.glDisable(GL10.GL_BLEND); // �رջ��
		gl.glEnable(GL10.GL_DEPTH_TEST); // ����Ȳ���

		gl.glPopMatrix();

		for (int i = 0; i < wakeBullet.size(); i++)
			wakeBullet.get(i).drawSelf(gl);
	}
}

class CloudThread extends Thread
{
	Cloud cloud = null;
	int count;

	public static boolean ifCloud;

	public CloudThread(Cloud cloud)
	{
		this.cloud = cloud;
		this.count = 0;

		ifCloud = true;
	}

	public void run()
	{
		while (ifCloud)
		{
			this.count++;
			this.cloud.point.x += 0.1f;

			if (this.cloud.point.x > GLConstant.GLWidth + 1)
			{
				MainActivity.pool.playWin();

				ifCloud = false;
				GLWorld.updateThread.ifUpdateThread = false;

				MainActivity.handler.sendEmptyMessage(MainActivity.GAME_ONWIN);

				return;
			}

			if (this.count == 6)
			{
				this.count = 0;
				this.cloud.drop();
			}
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
