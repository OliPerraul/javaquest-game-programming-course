package com.tutorialquest;

import com.badlogic.gdx.maps.MapProperties;
import com.tutorialquest.entities.Avatar;

public class Item {

	public final static String PROP_HEALTH = "Health";
	public final static String PROP_DAMAGE = "Damage";
	public final static String PROP_KNOCKBACK = "Knockback";
	public final static String TYPE_EQUIP = "Equip";
	public final static String PROP_EQUIP_TYPE = "EquipType";
	public final static String EQUIP_TYPE_SWORD = "Sword";
	public final static String EQUIP_TYPE_SHIELD = "Shield";
	public final static String TYPE_CONSUMABLE = "Consumable";
	public final static String TYPE_KEY = "Key";
	public final static String PROP_TRANSITION_ID = "TransitionID";

	public float cost;
	public String name;
	public String type;
	public String texturePath = "";
	public MapProperties properties;

	public float getFloat(String key)
	{
		return (Float) properties.get(key);
	}

	public int getInt(String key)
	{
		return (Integer) properties.get(key);
	}

	public String getString(String key)
	{
		return (String) properties.get(key);
	}

	public boolean hasProperty(String property)
	{
		return properties.containsKey(property);
	}

	public Item()
	{
		properties = new MapProperties();
	}

	public Item(
		String name,
		String texturePath,
		String type,
		float cost,
		MapProperties properties)
	{
		this.name = name;
		this.properties = properties;
		this.cost = cost;
		this.type = type;
		this.texturePath = texturePath;
	}

	public void use(Avatar avatar)
	{
		switch (type)
		{
			case Item.TYPE_EQUIP:
				// Check if item has type, otherwise invalid
				if(!properties.containsKey(Item.PROP_EQUIP_TYPE))
					return;

				switch (getString(Item.PROP_EQUIP_TYPE))
				{
					case Item.EQUIP_TYPE_SHIELD:
						if(avatar.inventory.shield == this) {
							avatar.inventory.shield = null;
							break;
						}

						avatar.inventory.shield = this;
						break;

					case Item.EQUIP_TYPE_SWORD:
						if(avatar.inventory.sword == this) {
							avatar.inventory.sword = null;
							break;
						}
						avatar.inventory.sword = this;
						break;
				}

				break;

			case Item.TYPE_CONSUMABLE:
				if(properties.containsKey(PROP_HEALTH))
				{
					avatar.heal(getFloat(PROP_HEALTH));
					avatar.inventory.remove(this);
				}

				break;

			case Item.TYPE_KEY:
				if(properties.containsKey(PROP_TRANSITION_ID)) {
					if(avatar.unlock(getInt(PROP_TRANSITION_ID))) {
						avatar.inventory.remove(this);
					}
				}
				break;
		}
	}
}
