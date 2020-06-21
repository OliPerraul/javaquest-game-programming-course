package com.tutorialquest.ui.dialogs;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.tutorialquest.Game;
import com.tutorialquest.Item;
import com.tutorialquest.utils.Compatibility;

public abstract class ItemDialog extends Dialog {

	public static final String ITEM_TEXT = "%s";

	protected Item item;
	private Texture selectionTexture;
	private boolean isAcceptSelected = false;

	protected abstract String getAcceptText();
	protected abstract String getCancelText();
	public abstract void acceptItem();
	protected String getItemText() {
		return Compatibility.platform.format(ITEM_TEXT, item.name);
	}
	public boolean isAvailable()
	{
		return true;
	}

	public ItemDialog() {
		super();
		selectionTexture = new Texture("ui/arrow_side.png");
	}

	public void open(Item item)
	{
		open();
		this.item = item;
		reset(getItemText());
	}

	@Override
	public void update(float deltaTime)
	{
		if(!enabled) return;

		super.update(deltaTime);

		if(currentTextProgress >= currentTextLength - 1)
		{
			if(Game.hud.input.isLeftJustPressed()) isAcceptSelected = !isAcceptSelected;
			else if(Game.hud.input.isRightJustPressed()) isAcceptSelected = !isAcceptSelected;
			else if(Game.hud.input.isInteractJustPressed())
			{
				if(isAcceptSelected)
				{
					if (isAvailable()) {
						acceptItem();
						close();
					}
				}
				else close();
			}
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

		Vector2 leavePosition = new Vector2(
			position.x + (WIDTH*.25f - MARGIN),
			position.y + (HEIGHT/2 - MARGIN)
		);

		Vector2 takePosition = new Vector2(
			position.x + (WIDTH*.5f - MARGIN),
			position.y + (HEIGHT/2 - MARGIN)
		);

		// Cancel
		spriteBatch.begin();
		font.draw(
			spriteBatch,
			getCancelText(),
			leavePosition.x,
			leavePosition.y,
			(WIDTH - MARGIN * 2),
			Align.left,
			true);
		spriteBatch.end();

		// Accept
		spriteBatch.begin();
		font.setColor(
			isAvailable() ?
				Color.DARK_GRAY :
				Color.GRAY
				);

		font.draw(
			spriteBatch,
			getAcceptText(),
			takePosition.x,
			takePosition.y,
			(WIDTH - MARGIN * 2),
			Align.left,
			true);
		font.setColor(Color.DARK_GRAY);
		spriteBatch.end();

		Vector2 selectedPosition = isAcceptSelected ? takePosition : leavePosition;

		// Draw selection arrow icon
		spriteBatch.begin();
		spriteBatch.draw(
			selectionTexture,
			selectedPosition.x - ICON_SIZE,
			selectedPosition.y - (ICON_SIZE*.75f),
			ICON_SIZE,
			ICON_SIZE);
		spriteBatch.end();
	}
}
