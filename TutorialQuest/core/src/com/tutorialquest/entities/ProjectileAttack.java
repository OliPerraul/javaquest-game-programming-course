package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.IAttack;
import com.tutorialquest.utils.Sprite;

import java.util.LinkedList;
import java.util.List;

public class ProjectileAttack extends PhysicalObject implements IAttack
{
	public static float SPEED = 50f;
	public static Vector2 SIZE = new Vector2(16, 16);
	private static final int NUM_HITS = 3;

	private int hits = 0;
	private Vector2 direction;
	private Sprite sprite;
	private List<PhysicalObject> collisionResults = new LinkedList<>();
	private float damage;
	private float knockback;
	private float speed = 1f;

	@Override
	public float getDamage() {
		return damage;
	}

	@Override
	public float getKnockback() {
		return knockback;
	}

	@Override
	public Vector2 getDirection() {
		return direction;
	}

	public ProjectileAttack(
		Vector2 position,
		Vector2 direction,
		float damage,
		float knockback,
		float speed)
	{
		super(position);
		this.sprite = new Sprite("objects/projectile.png", SIZE);
		this.collider = new Collider(SIZE, Collider.FLAG_ENEMY);
		this.direction = direction;
		this.damage = damage;
		this.knockback = knockback;
		this.speed = speed;
		this.locomotionVelocity
			.set(direction)
			.scl(speed);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		updateVelocity(deltaTime);
		move();
		sprite.update(deltaTime);
		collisionResults.clear();
		if(collider.getObjectCollisions(
			this,
			velocity.x,
			velocity.y,
			Collider.FLAG_AVATAR,
			collisionResults))
		{
			collisionResults.iterator().next().onAttacked(this);
			destroy();
			return;
		}

		if(collider.isColliding(this, velocity, Collider.FLAG_NONE))
		{
			destroy();
		}
	}

	public void destroy()
	{
		Game.level.remove(this);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		super.render(spriteBatch);
		sprite.render(spriteBatch, position);
	}

	@Override
	public void onAttacked(IAttack attack)
	{
		sprite.flash();
		onPushed(this, attack.getDirection().scl(attack.getKnockback()));
		hits++;
		if(hits >= NUM_HITS) {
			Game.level.remove(this);
		}
	}
}
