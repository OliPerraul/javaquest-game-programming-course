package com.tutorialquest;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

	private List<Item> items = new ArrayList<>();
	public Item sword;
	public Item shield;

	public List<Item> getItems() {
		return items;
	}

	public void add(Item ... items) {
		for (Item item : items) {
			this.items.add(item);
		}
	}

	public void remove(Item item) {
		items.remove(item);
	}
}
