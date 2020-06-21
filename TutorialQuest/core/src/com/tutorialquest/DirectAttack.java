package com.tutorialquest;
import com.badlogic.gdx.math.Vector2;

public class DirectAttack implements IAttack
{
	private float damage = 10;
	private float knockback = 10f;
	protected Vector2 direction;

	public float getDamage() {return damage;}
	public float getKnockback() {return knockback; }
	public Vector2 getDirection() { return direction;}

	public DirectAttack(Vector2 direction, float damage, float knockback)
	{
		this.knockback = knockback;
		this.damage = damage;
		this.direction = direction;
	}
}
