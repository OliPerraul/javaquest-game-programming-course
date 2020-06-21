package com.tutorialquest.entities;

import com.badlogic.gdx.math.Vector2;
import com.tutorialquest.utils.Sprite;

public class Money extends Collectible {

	public static final Vector2 SIZE = new Vector2(16, 16);

	public static final float COPPER_COIN_VALUE = 1f;
	public static final float SILVER_COIN_VALUE = 5;
	public static final float SILVER_STACK_VALUE = 25;
	public static final float GOLD_COIN_VALUE = 10;
	public static final float GOLD_STACK_VALUE = 50;
	public static final float GOLD_PILE_VALUE = 100;
	public static final float DIAMOND_VALUE = 200;

	private float value;

	public Money(Vector2 position, float amount)
	{
		super(position);

		this.value = amount;
		String texturePath = "";
		if(amount <= COPPER_COIN_VALUE) texturePath = "objects/coin_copper.png";
		else if(amount <= SILVER_COIN_VALUE) texturePath = "objects/coin_silver.png";
		else if(amount <= SILVER_STACK_VALUE) texturePath = "objects/coin_silver_stack.png";
		else if(amount <= GOLD_COIN_VALUE) texturePath = "objects/coin_gold.png";
		else if(amount <= GOLD_STACK_VALUE) texturePath = "objects/coin_gold_stack.png";
		else if(amount <= GOLD_PILE_VALUE) texturePath = "objects/coin_gold_pile.png";
		else if(amount <= DIAMOND_VALUE) texturePath = "objects/diamond.png";

		sprite = new Sprite(texturePath, SIZE);
		sprite.origin = new Vector2(SIZE).scl(0.5f);
	}

	@Override
	public void collect(Avatar avatar) {
		avatar.updateMoney(value);
	}
}
