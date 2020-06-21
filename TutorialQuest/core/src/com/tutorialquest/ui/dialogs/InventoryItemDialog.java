package com.tutorialquest.ui.dialogs;

import com.tutorialquest.Game;
import com.tutorialquest.Item;

public class InventoryItemDialog extends ItemDialog {

	public static final String USE_TEXT = "Use";
	public static final String EQUIP_TEXT = "Equip";
	public static final String UNEQUIP_TEXT = "Unequip";

	public String acceptString;

	public InventoryItemDialog() {
		super();
	}

	@Override
	protected String getCancelText()
	{
		return "Return";
	}

	@Override
	protected String getAcceptText()
	{
		return acceptString;
	}

	@Override
	public void acceptItem()
	{
		item.use(Game.level.avatar);
	}

	@Override
	public void open(Item item)
	{
		super.open(item);

		if(item.type.equals(Item.TYPE_EQUIP)) acceptString = Game.level.avatar.isEquipped(item) ? UNEQUIP_TEXT : EQUIP_TEXT;
		else acceptString = USE_TEXT;
	}

	@Override
	public void close()
	{
		super.close();
		Game.hud.inventoryDialog.open();
	}

}
