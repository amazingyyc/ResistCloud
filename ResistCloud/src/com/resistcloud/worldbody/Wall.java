package com.resistcloud.worldbody;

/*
 * ��ǽ�� ����
 */
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.common.Vec2;

public abstract class Wall 
{
	Vec2 point;
	
	int texture;
	
	//�������� ��������
	FloatBuffer vertexPointer;
	FloatBuffer texCoordPointer;
	
	//����ʵ�������������"����"
	public abstract void addBody();
	
	//���ƺ���
	public abstract void drawSelf(GL10 gl);
}
