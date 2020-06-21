package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.utils.Sprite;

import java.util.LinkedList;
import java.util.List;

public class Collectible extends PhysicalObject {

	Sprite sprite;
	private List<PhysicalObject> collisionResults = new LinkedList<PhysicalObject>();

	public enum Type
	{
		Health,
		Money,
		Item
	}

	public Collectible(Vector2 position) {
		super(position);

		collider = new Collider(
			new Vector2(
				8,
				8),
			Collider.FLAG_COLLECTIBLE);
		collider.origin.set(4, 4);
	}

	public void collect(Avatar avatar) {}

	@Override
	public void update(float deltaTime)
	{
		collisionResults.clear();
		if(collider.getObjectCollisions(
			this,
			0,0,
			Collider.FLAG_AVATAR,
			collisionResults))
		{
			collect((Avatar) collisionResults.iterator().next());
			Game.level.remove(this);
			return;
		}

		collider.update(position);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		super.render(spriteBatch);
		sprite.render(spriteBatch, position);
	}
}
