package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.utils.Utils;

public class Transition extends PhysicalObject
{
	public static final float DISABLE_TIME = 2f;

	private Vector2 size = new Vector2(16, 16);
	private String level;
	private float disabledTime = 0;
	private float disableTimeLimit = 0;
	public int id;
	public Utils.Direction direction;

	public Vector2 getDestination()
	{
		return
			position.cpy()
				.add(size.cpy().scl(.5f))
				.add(Utils.toVector(direction).scl(size.x));
	}

	public Transition(
		Vector2 position,
		String level,
		int id,
		String direction,
		Vector2 size)
	{
		super(position);
		this.size = size;
	}

	public Transition(
		Vector2 position,
		String level,
		int id,
		String direction) {

		super(position);

		this.id = id;
		this.level = level;
		this.direction = Utils.toDirection(direction);

		collider = new Collider(
			size,
			Collider.FLAG_TRANSITION | Collider.FLAG_PUSHABLE
		);
	}

	public void disable(float disableTime)
	{
		this.disabledTime = 0;
		disableTimeLimit = disableTime;
	}

	public void transition(Avatar avatar)
	{
		if(disabledTime < disableTimeLimit) return;
		Game.level.remove(avatar);
		Game.load(level, id, avatar);
	}

	@Override
	public void onPushed(PhysicalObject source, Vector2 incomingPushVelocity) {
		if(!(source instanceof Avatar)) return;
		transition((Avatar) source);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		collider.update(position);

		if(disabledTime < disableTimeLimit) {
			disabledTime += deltaTime;
			return;
		}

	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		super.render(spriteBatch);
	}

}
