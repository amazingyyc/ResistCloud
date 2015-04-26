package com.resistcloud.dropbody;

/*
 * Ͷ�����ǽ����ĸ���
 * 
 */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import com.resistcloud.worldbody.GLWorld;

public abstract class GLBody 
{
	//���ĵ�
	public Vec2 point;
	
	public Vec2 getPoint()
	{
		return this.point;
	}
	
	public void setPoint(Vec2 point)
	{
		this.point = point;
	}
	
	//����İ볤
	float halfLength;
	
	public float getHalfLength()
	{
		return this.halfLength;
	}
	
	//��ת�ĽǶ�
	float angle;
	
	//�Ƿ�����ƶ���־
	//boolean ifMove;

	//����
	int texture;
	
	//���ͱ�ʶ
	int style;
	
	public int getStyle()
	{
		return this.style;
	}
	
	//��Ӧ����ʵ����
	public Body body;
	
	//�Ƿ��������ʵ����������
	public boolean ifBody;
	
	//��������
	FloatBuffer vertexPointer;
	
	//��������
	FloatBuffer texCoordPointer;
	
	//�жϵ��Ƿ��ڡ����塱��,���ж��Ƿ�����ƶ�
	public abstract boolean inGLBody(Vec2 v);
	
	//����ʵ�������������"����"
	public abstract void addBody();
	
	//��ʼģ��
	//public abstract void wakeUp();
	//��ʼģ�� ��������
	public void wakeUp()
	{
		GLWorld.needWakeUp.add(this);
	}
	
	//���ƺ���
	public abstract void drawSelf(GL10 gl);
	
	//�ֱ����С������ �������� Բ ������ ������ Ц�� ǽ
	public static final int GLBODY_SMALLSQUARE = 0;
	public static final int GLBODY_BIGSQUARE   = 1;
	public static final int GLBODY_CIRCLE      = 2;
	public static final int GLBODY_TRIANGLR    = 3;
	public static final int GLBODY_RECTANGLE   = 4;
	//static final int GLBODY_FACE        = 5;
	//static final int GLBODY_WALL        = 6;
	
	
	//����ľ���
	static public float getLong(Vec2 v1, Vec2 v2)
	{
		return (float) Math.sqrt((v1.x-v2.x)*(v1.x-v2.x) + (v1.y-v2.y)*(v1.y-v2.y));
	}
	
	static public FloatBuffer setPointer(float[] f)
	{
		FloatBuffer verPointer = null;
		
		ByteBuffer vbb= ByteBuffer.allocateDirect(f.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		verPointer = vbb.asFloatBuffer();
		verPointer.put(f);
		verPointer.position(0);
				
		return verPointer;
	}
}



















