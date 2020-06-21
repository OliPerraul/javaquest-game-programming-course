package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.Item;
import com.tutorialquest.utils.Sprite;

public class PhysicalItem extends PhysicalObject implements IInteractible {

	public static int WIDTH = 16;
	public static int HEIGHT = 16;

	public Item item;
	private Sprite sprite = null;
	public boolean isShopItem = false;

	protected Sprite getSprite() {
		return sprite;
	}

	public PhysicalItem(
		Vector2 position,
		Item item)
	{
		super(position);

		this.item = item;
		sprite = new Sprite(item.texturePath, WIDTH, HEIGHT, 0, 0);
		collider = new Collider(
			new Vector2(WIDTH, HEIGHT),
			Collider.FLAG_INTERACTIBLE | Collider.FLAG_COLLIDABLE | Collider.FLAG_PUSHABLE);
	}

	public PhysicalItem(
		Vector2 position,
		String name,
		String type,
		String texturePath,
		boolean isShopItem,
		float cost,
		MapProperties properties) {

		super(position);

		this.isShopItem = isShopItem;
		item = new Item(name, texturePath, type, cost, properties);
		sprite = new Sprite(texturePath, WIDTH, HEIGHT, 0, 0);
		collider = new Collider(
			new Vector2(WIDTH, HEIGHT),
			Collider.FLAG_INTERACTIBLE | Collider.FLAG_COLLIDABLE | Collider.FLAG_PUSHABLE);
	}

	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		move();
		sprite.update(deltaTime);
	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{
		super.render(spriteBatch);
		sprite.render(spriteBatch, position);
	}

	@Override
	public void interact(Avatar avatar) {
		Game.hud.physicalItemDialog.open(this, isShopItem);
	}
}
