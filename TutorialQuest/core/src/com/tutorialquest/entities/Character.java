package com.tutorialquest.entities;

import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.DirectAttack;
import com.tutorialquest.Game;
import com.tutorialquest.IAttack;
import com.tutorialquest.LootTable;
import com.tutorialquest.utils.AnimatedSprite;
import com.tutorialquest.utils.EventHandler;
import com.tutorialquest.utils.Utils;

import java.util.LinkedList;
import java.util.List;

public abstract class Character extends PhysicalObject {

	public class SpriteUtils {
		public static final int IDLE_FRONT = 1;
		public static final int WALK_FRONT = 2;
		public static final int IDLE_SIDE = 3;
		public static final int WALK_SIDE = 4;
		public static final int IDLE_BACK = 5;
		public static final int WALK_BACK = 6;
	}

	private static float INVINCIBILITY_TIME_LIMIT = 0.5f;
	private static float TURN_EPSILON = 0.5f;
	public static final int DROP_AMOUNT = 4;
	public static final float DROP_DISTANCE_RANGE = 5f;

	public float pushForce = 0.5f;
	public float maxHealth = 100f;
	public float health = maxHealth;
	public float damage = 5f;
	public float knockback = 5f;
	public float speed = 45f;
	public float money = 0f;

	public float invincibilityTime = 0f;
	public Vector2 controlAxes = new Vector2();
	protected Utils.Direction fixedDirection = Utils.Direction.DOWN;
	public Vector2 direction = new Vector2(0, -1);
	public EventHandler<Character> onHealthChangedHandler = new EventHandler<>();
	public EventHandler<Character> onMoneyChangedHandler = new EventHandler<>();
	protected AnimatedSprite sprite;
	protected LootTable lootTable;

	public void collisionAttack(int mask) {
		List<PhysicalObject> results = new LinkedList<>();
		if (collider.getObjectCollisions(
			this,
			velocity.x,
			velocity.y,
			mask,
			results))
		{
			Vector2 collisionDirection = new Vector2(velocity).nor();
			results.iterator().next()
				.onAttacked(new DirectAttack(
					collisionDirection,
					damage,
					knockback));
		}
	}

	public void onDefeated() {
		Game.level.remove(this);
		Game.level.drop(lootTable, DROP_AMOUNT, position, DROP_DISTANCE_RANGE);
	}

	public void push(int collisionMask) {
		List<PhysicalObject> results = new LinkedList<>();
		if (!collider.getObjectCollisions(
			this,
			velocity.x,
			velocity.y,
			collisionMask,
			results)) return;

		Vector2 resistedVelocityTotal = new Vector2();
		Vector2 resistedVelocity = new Vector2();
		for (PhysicalObject result : results) {
			resistedVelocity
				.set(velocity)
				.scl(pushForce);
			result.onPushed(this, resistedVelocity);
			resistedVelocityTotal.add(resistedVelocity);
		}

		velocity
			.set(resistedVelocityTotal)
			.scl(1f / results.size());
	}

	public void control() {

	}

	public void turn()
	{
		if (controlAxes.epsilonEquals(Vector2.Zero)) {
			switch (fixedDirection) {
				case LEFT:
				case RIGHT:
					playAnimation(SpriteUtils.IDLE_SIDE, false, false);
					break;

				case UP:
					playAnimation(SpriteUtils.IDLE_BACK, false, false);
					break;

				case DOWN:
					playAnimation(SpriteUtils.IDLE_FRONT, false, false);
					break;
			}

			return;
		}

		if (controlAxes.x > TURN_EPSILON) {
			direction.set(1, direction.y);
			fixedDirection = Utils.Direction.RIGHT;
			sprite.flipX = false;
			playAnimation(SpriteUtils.WALK_SIDE, false, false);
		} else if (controlAxes.x < -TURN_EPSILON) {
			direction.set(-1, direction.y);
			fixedDirection = Utils.Direction.LEFT;
			sprite.flipX = true;
			playAnimation(SpriteUtils.WALK_SIDE, false, false);
		}

		if (controlAxes.y > TURN_EPSILON) {
			direction.set(direction.x, 1);
			fixedDirection = Utils.Direction.UP;
			sprite.flipX = false;
			playAnimation(SpriteUtils.WALK_BACK, false, false);
		} else if (controlAxes.y < -TURN_EPSILON) {
			direction.set(direction.x, -1);
			fixedDirection = Utils.Direction.DOWN;
			sprite.flipX = false;
			playAnimation(SpriteUtils.WALK_FRONT, false, false);
		}

		direction.nor();
	}

	public void playAnimation(int anim, boolean reset, boolean force) {
		sprite.play(anim, reset);
	}

	@Override
	public void onAttacked(IAttack attack) {
		if (invincibilityTime < INVINCIBILITY_TIME_LIMIT)
			return;

		invincibilityTime = 0;
		if(sprite != null) sprite.flash();
		onPushed(
			this,
			new Vector2(attack.getDirection())
				.scl(attack.getKnockback()));

		health -= attack.getDamage();
		onHealthChangedHandler.invoke(this);
		if (health <= 0) {
			health = 0;
			onDefeated();
		}
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(sprite != null) sprite.update(deltaTime);
		if (invincibilityTime < INVINCIBILITY_TIME_LIMIT) invincibilityTime += deltaTime;
	}

	public void heal(float value) {
		health += value;
		if (health > maxHealth) health = maxHealth;
		onHealthChangedHandler.invoke(this);
	}

	public Character(
		Vector2 position,
		float damage,
		float knockback,
		float maxHealth,
		float speed,
		float pushForce) {
		super(position);

		this.damage = damage;
		this.knockback = knockback;
		this.pushForce = pushForce;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.speed = speed;
	}
}
