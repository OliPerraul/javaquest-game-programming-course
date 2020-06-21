package com.tutorialquest.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.entities.Character;
import com.tutorialquest.utils.Compatibility;

public class MoneyCounter {

	private final static float FONT_SCALE = 1f/4f;

	private Texture coinsTexture;
	private BitmapFont font;
	private float value = 0;

	public MoneyCounter(){
		coinsTexture = new Texture("objects/coin_gold_pile.png");
		font = new BitmapFont(Gdx.files.internal("fonts/player2.fnt"));
		font.getData().setScale(FONT_SCALE);
	}

	public void OnAvatarMoneyChanged(Character character) {
		value = character.money;
	}

	public void render(SpriteBatch spriteBatch, Vector2 position)
	{
		spriteBatch.begin();

		spriteBatch.draw(
			coinsTexture,
			position.x - 4,
			position.y,
			16,
			16);

		font.draw(
			spriteBatch,
			Compatibility.platform.format("%.2f", value),
			position.x + 4,
			position.y + 4
		);
		spriteBatch.end();
	}

}
