package com.tutorialquest.ui.dialogs;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.tutorialquest.Game;
import com.tutorialquest.Inventory;
import com.tutorialquest.Item;

public class InventoryDialog extends Dialog {

	// https://www.youtube.com/watch?v=OHHSQxn_IOY
	// 5:39:05

	public static final String NO_ITEM_TEXT = "No items";
	public static final int MAX_VISIBLE_ITEMS = 4;

	private Texture selectionTexture;
	private int selectedIndex = 0;
	private GlyphLayout glyphLayout;
	private Inventory inventory;

	public InventoryDialog() {
		super();
		selectionTexture = new Texture("ui/arrow_side.png");
		glyphLayout = new GlyphLayout();
	}

	public void open(Inventory inventory)
	{
		open();
		this.inventory = inventory;
	}

	@Override
	public void update(float deltaTime)
	{
		if(!enabled) return;
		super.update(deltaTime);

		if(Game.hud.input.isDownJustPressed()) selectedIndex++;
		else if(Game.hud.input.isUpJustPressed()) selectedIndex--;
		else if(Game.hud.input.isInteractJustPressed())
		{
			if(inventory.getItems().size() == 0) return;
			close();
			Item item = inventory.getItems().get(selectedIndex);
			Game.hud.inventoryItemDialog.open(item);
		}
		else if(Game.hud.input.isMenuJustPressed())
		{
			close();
			return;
		}

		// If item is removed from the inventory update the selection index
		// Clamping
		if(selectedIndex >= inventory.getItems().size())
			selectedIndex = inventory.getItems().size() - 1;
		else if(selectedIndex < 0)
			selectedIndex = 0;

	}

	@Override
	public void render(
		SpriteBatch spriteBatch,
		Camera camera,
		Vector2 position)
	{
		if(!enabled) return;

		super.render(spriteBatch, camera, position);

		Vector2 startPosition = new Vector2(
			position.x + (MARGIN + ICON_SIZE/2),
			position.y + (HEIGHT - MARGIN)
		);


		if(inventory.getItems().isEmpty())
		{
			spriteBatch.begin();
			font.draw(
				spriteBatch,
				NO_ITEM_TEXT,
				startPosition.x,
				startPosition.y,
				(WIDTH - MARGIN * 2),
				Align.left,
				true);

			spriteBatch.end();
			return;
		}

		float offsetHeight = 0;
		float selectedOffsetHeight = 0;
		int offset = selectedIndex < MAX_VISIBLE_ITEMS ? 0 : selectedIndex - (MAX_VISIBLE_ITEMS - 1);
		for(int i = offset; i < offset + MAX_VISIBLE_ITEMS; i++)
		{
			if(i >= inventory.getItems().size())
				break;

			Item item = inventory.getItems().get(i);

			spriteBatch.begin();
			if(Game.level.avatar.isEquipped(item)) font.setColor(Color.BLUE);
			font.draw(
				spriteBatch,
				item.name,
				startPosition.x,
				startPosition.y - offsetHeight,
				(WIDTH - MARGIN * 2),
				Align.left,
				true);

			font.setColor(Color.DARK_GRAY);
			spriteBatch.end();

			glyphLayout.setText(font, item.name);
			offsetHeight += glyphLayout.height*2;
			if(i < selectedIndex) selectedOffsetHeight += glyphLayout.height*2;
		}

		Vector2 selectedPosition = new Vector2(
			startPosition.x,
			startPosition.y - selectedOffsetHeight
		);

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
