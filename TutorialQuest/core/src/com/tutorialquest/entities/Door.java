package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.utils.Sprite;

public class Door extends PhysicalObject {

	public int WIDTH = 48;
	public int HEIGHT = 48;

	private Sprite unlockedSprite;
	private Sprite lockedSprite;
	private Sprite sprite;
	private String level;
	private String direction;
	private boolean isBoss = false;
	public int transitionID;
	public Transition transition;

	public Door(
		Vector2 position,
		String level,
		int transitionID,
		String direction,
		boolean isLocked,
		boolean isBoss)
	{
		super(position);

		this.isBoss = isBoss;
		this.level = level;
		this.transitionID = transitionID;
		this.direction = direction;

		collider = new Collider(
			new Vector2(WIDTH, HEIGHT),
			Collider.FLAG_DOOR | Collider.FLAG_COLLIDABLE);

		unlockedSprite = new Sprite(isBoss ?
			"objects/boss_door_open.png" :
			"objects/door_open.png",
			WIDTH, HEIGHT, 0, 0);

		lockedSprite = new Sprite(isBoss ?
			"objects/boss_door_closed.png" :
			"objects/door_closed.png",
			WIDTH, HEIGHT, 0, 0);

		sprite = lockedSprite;

		if(!isLocked) unlock();
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		super.render(spriteBatch);
		sprite.render(spriteBatch, position);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		sprite.update(deltaTime);
		collider.update(position);
	}

	public void unlock()
	{
		sprite = unlockedSprite;
		Game.level.add(
			transition = new Transition(
				new Vector2()
					.add(position)
					.add(WIDTH/3, 0),
				level,
				transitionID,
				direction));
	}

}
