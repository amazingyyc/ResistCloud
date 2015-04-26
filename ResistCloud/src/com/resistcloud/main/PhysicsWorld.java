package com.resistcloud.main;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/*
 * jbox2d模拟物理世界
 */
public class PhysicsWorld
{
	public World world; // 创建一个管理碰撞的世界
	float timeStep; // 模拟的频率
	int iterations; // 迭代越大，模拟越精确，但性能越低

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

	// 开始渲染
	public void upDate()
	{
		world.step(timeStep, iterations);// 开始模拟
	}

}
