package com.resistcloud.main;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/*
 * jbox2dģ����������
 */
public class PhysicsWorld
{
	public World world; // ����һ��������ײ������
	float timeStep; // ģ���Ƶ��
	int iterations; // ����Խ��ģ��Խ��ȷ��������Խ��

	public PhysicsWorld()
	{
		AABB worldAABB = new AABB(new Vec2(-20, -20), new Vec2(40, 20));
		Vec2 gravity = new Vec2(0.0f, -10.0f);
		boolean doSleep = true;

		world = new World(worldAABB, gravity, doSleep);
		world.setContactListener(new MyContactListener());

		timeStep = 1.0f / 60.0f;
		iterations = 10;
	}

	// ��ʼ��Ⱦ
	public void upDate()
	{
		world.step(timeStep, iterations);// ��ʼģ��
	}

}
