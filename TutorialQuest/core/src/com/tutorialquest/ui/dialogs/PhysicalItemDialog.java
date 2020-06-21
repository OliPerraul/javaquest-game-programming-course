package com.tutorialquest.ui.dialogs;

import com.tutorialquest.Game;
import com.tutorialquest.entities.PhysicalItem;
import com.tutorialquest.utils.Compatibility;

public class PhysicalItemDialog extends ItemDialog {

	public static final String TAKE_TEXT = "Take";
	public static final String LEAVE_TEXT = "Leave";
	public static final String PURCHASE_TEXT = "Purchase";
	public static final String ITEM_FOR_SALE_TEXT = "%s - %.2f$";

	private PhysicalItem physicalItem;
	private boolean isShopItem = false;

	public PhysicalItemDialog() {
		super();
	}

	public void open(PhysicalItem physicalItem, boolean isShopItem)
	{
		this.physicalItem = physicalItem;
		this.isShopItem = isShopItem;
		open(physicalItem.item);
	}


	public boolean isAvailable()
	{
		return
			!isShopItem ||
			Game.level.avatar.hasEnoughMoney(item);
	}

	public void acceptItem()
	{
		Game.level.remove(physicalItem);
		if(isShopItem) Game.level.avatar.purchaseItem(item);
		else Game.level.avatar.inventory.add(item);
	}


	@Override
	protected String getAcceptText() {
		return isShopItem ?
			PURCHASE_TEXT :
			TAKE_TEXT;
	}

	@Override
	protected String getCancelText() {
		return LEAVE_TEXT;
	}

	@Override
	protected String getItemText() {
		return isShopItem ?
			Compatibility.platform.format(ITEM_FOR_SALE_TEXT, item.name, item.cost):
			Compatibility.platform.format(ITEM_TEXT, item.name);
	}
}
