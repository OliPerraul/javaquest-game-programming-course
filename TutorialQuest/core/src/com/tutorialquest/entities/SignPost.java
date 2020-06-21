package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.utils.Sprite;

public class SignPost extends PhysicalObject implements IInteractible {

	public final static Vector2 SIZE = new Vector2(16, 16);

	private Sprite sprite;
	private String[] texts;

	public Sprite getSprite() {
		return sprite;
	}

	public SignPost(Vector2 position, String[] texts) {
		super(position);
		this.texts = texts;
		sprite = new Sprite("objects/sign_spritesheet.png", (int)SIZE.x, (int)SIZE.y, 0, 0);
		collider = new Collider(
			SIZE,
			Collider.FLAG_INTERACTIBLE | Collider.FLAG_COLLIDABLE);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		sprite.update(deltaTime);
		collider.update(position);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		super.render(spriteBatch);
		sprite.render(spriteBatch, position);
	}

	public void interact(Avatar avatar)
	{
		Game.hud.messageDialog.open(texts);
	}
}
