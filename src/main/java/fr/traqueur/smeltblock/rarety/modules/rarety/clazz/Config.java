package fr.traqueur.smeltblock.rarety.modules.rarety.clazz;

import java.util.List;

import com.google.common.collect.Lists;

public class Config {

	private String raretyInventoryName;
	private List<String> raretyItems;
	private List<String> loreInventory;
	
	public Config() {
		raretyInventoryName = "Mes Items Rares";
		raretyItems = Lists.newArrayList();
		raretyItems.add("Pioche de");
		raretyItems.add("Épée de");
		loreInventory = Lists.newArrayList("Lore 1", "Lore 2", "Lore 3");
	}

	public List<String> getRaretyItems() {
		return raretyItems;
	}

	public String getRaretyInventoryName() {
		return raretyInventoryName;
	}

	public List<String> getLoreInventory() {
		return loreInventory;
	}
	
	
	
}
