package com.tutorialquest.ui.dialogs;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.Game;

public class MessageDialog extends Dialog {

	public MessageDialog() {
		super();
	}

	public void open(String[] texts)
	{
		if(texts.length == 0) return;

		open();

		this.texts = texts;
		currentTextIndex = 0;
		reset(texts[0]);
	}

	public void nextText()
	{
		currentTextIndex++;
		if(currentTextIndex > texts.length - 1)
		{
			currentTextIndex = 0;
			close();
			return;
		}

		reset(texts[currentTextIndex]);
	}

	@Override
	public void update(float deltaTime)
	{
		if(!enabled) return;

		super.update(deltaTime);

		if(currentTextProgress >= currentTextLength - 1)
		{
			finished = true;
			currentTextProgress = currentTextLength -1;
			if(Game.hud.input.isInteractJustPressed()) nextText();
			return;
		}
	}

	@Override
	public void render(
		SpriteBatch spriteBatch,
		Camera camera,
		Vector2 position)
	{
		if(!enabled) return;

		super.render(spriteBatch, camera, position);

		// Draw arrow icon
		if(finished) {
			spriteBatch.begin();
			spriteBatch.draw(
				arrowTexture,
				position.x + (WIDTH - MARGIN - ICON_SIZE),
				position.y + MARGIN,
				ICON_SIZE,
				ICON_SIZE);

			spriteBatch.end();
		}
	}
}
