package com.tutorialquest;

import com.tutorialquest.entities.Collectible;
import com.tutorialquest.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class LootTable {

	public static class Loot
	{
		public Collectible.Type type;
		public float value;
		public float probability;
		public Item item;
	}

	public List<Loot> loots;

	public LootTable() {}

	public void add(Loot ... loots)
	{
		this.loots = Arrays.asList(loots);
	}

	// Etape 1: Filtre la table de tresor selon le pourcentage de chance des elements
	// Etape 2: Pour chaque element successif, interpole lineairement la chance d'obtenir un tresor additionnel
	public List<Loot> Take(int amount)
	{
		LinkedList<Loot> taken = new LinkedList<>();
		if(loots.isEmpty()) return taken;

		int trials = amount;
		while(trials > 0) {
			if(Utils.random.nextInt(amount) <  trials)
			{
				Loot loot = loots.get(Utils.random.nextInt(loots.size()));
				if(
					loot.probability < 0 ||
						Utils.random.nextFloat() <= loot.probability)
				{
					trials--;
					taken.add(loot);
					continue;
				}

			}
		}

		return taken;
	}
}
