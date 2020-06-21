package com.tutorialquest.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Collider;
import com.tutorialquest.Game;
import com.tutorialquest.utils.Sprite;


// Est-ce que reelement le bon lien d'heritage

public class NPC extends Character implements IInteractible{


	private int WIDTH = 32;
	private int HEIGHT = 32;

	private String[] texts;

	private Sprite sprite;

	public NPC(Vector2 position, int type, String[] texts)
	{
		super(position, -1, -1, -1, -1, -1);
		this.sprite = new Sprite("objects/npc_spritesheet.png", WIDTH, HEIGHT, type, 0);
		this.sprite.origin = new Vector2(WIDTH/2, 0);

		this.collider = new Collider(
			Collider.DEFAULT_SIZE,
			Collider.FLAG_INTERACTIBLE | Collider.FLAG_COLLIDABLE);
		this.collider.origin.set(Collider.DEFAULT_SIZE.x/2, 0);
		this.texts = texts;
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

	@Override
	public void interact(Avatar avatar) {
		Game.hud.messageDialog.open(texts);
	}
}
