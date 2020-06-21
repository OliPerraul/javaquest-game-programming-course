package com.tutorialquest.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.entities.Character;

public class HealthBar {

	private TextureRegion[][] healtBarTextures;
	private Texture hearthTexture;
	private float fillAmount = 1;

	public HealthBar(){
		healtBarTextures = TextureRegion.split(new Texture("ui/health_spritesheet.png"), 64, 16);
		hearthTexture = new Texture("objects/heart_icon.png");
	}

	public void render(SpriteBatch spriteBatch, Vector2 position)
	{
		spriteBatch.begin();
		spriteBatch.draw(
			healtBarTextures[0][0],
			position.x,
			position.y,
			64,
			16);

		TextureRegion fillRegion = new TextureRegion(
			healtBarTextures[0][1],
			0,
			0,
			(int) (healtBarTextures[0][1].getRegionWidth() * fillAmount),
			healtBarTextures[0][1].getRegionHeight());

		spriteBatch.draw(
			fillRegion,
			position.x,
			position.y,
			fillRegion.getRegionWidth(),
			fillRegion.getRegionHeight());

		spriteBatch.draw(
			hearthTexture,
			position.x - 4,
			position.y,
			16,
			16);

		spriteBatch.end();
	}

	public void onAvatarHealthChanged(Character character) {
		fillAmount = character.health / character.maxHealth;
	}
}
