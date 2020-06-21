package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.IAttack;

public class PhysicalObject extends Entity {

	public static final float PUSH_DECELERATION = 0.4f;
	private static final float UNPUSHED_TIME_LIMIT = 0.05f;
	private float PUSH_RESISTANCE = 0.25f;

	private float unpushedTime = 0;
	public Vector2 pushedVelocity = new Vector2();
	public Vector2 locomotionVelocity = new Vector2();
	public Vector2 velocity = new Vector2();
	public Collider collider;

	public float getPushResistance() {
		return PUSH_RESISTANCE;
	}

	public void updateVelocity(float deltaTime) {
		velocity
			.setZero()
			.add(locomotionVelocity)
			.add(pushedVelocity)
			.scl(deltaTime);
	}

	public PhysicalObject(Vector2 position) {
		super(position);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		super.render(spriteBatch);
		if(!Game.isDebugRenderEnabled) return;
		if (collider != null) {
			collider.render();
		}
	}

	public void move() {
		collider.updateObject(
			this,
			Collider.FLAG_COLLIDABLE);
		position.add(velocity);
		collider.update(position);
	}

	@Override
	public void start() {
		if (collider != null) {
			collider.update(position);
		}
	}

	public void onPushed(PhysicalObject source, Vector2 incomingPushVelocity) {
		unpushedTime = 0;
		incomingPushVelocity.scl(1f - getPushResistance());
		pushedVelocity.set(incomingPushVelocity);
	}

	@Override
	public void update(float deltaTime) {
		if (unpushedTime >= UNPUSHED_TIME_LIMIT) pushedVelocity.lerp(Vector2.Zero, PUSH_DECELERATION);
		else unpushedTime += deltaTime;
	}

	public void onAttacked(IAttack attack) {
	}

}
