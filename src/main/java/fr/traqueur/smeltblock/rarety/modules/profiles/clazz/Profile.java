package fr.traqueur.smeltblock.rarety.modules.profiles.clazz;

import java.io.File;
import java.util.List;

import fr.traqueur.smeltblock.rarety.api.utils.InventoryUtils;
import fr.traqueur.smeltblock.rarety.modules.rarety.IslandRaretyManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import fr.traqueur.smeltblock.rarety.modules.profiles.ProfileManager;

public class Profile {

	private String name;
	private List<byte[]> raretyItems;

	public Profile() {
	}

	public Profile(Player player) {
		name = player.getName();
		raretyItems = Lists.newArrayList();
	}

	public File getProfileFile() {
		return new File(ProfileManager.getInstance().getFile(), this.name + ".json");
	}

	public String getName() {
		return name;
	}

	public List<byte[]> getRaretyItems() {
		return raretyItems;
	}

	public void setRaretyItems(List<byte[]> items) {
		this.raretyItems = items;
	}

	public void saveItem(byte[] map, int slot) {
		IslandRaretyManager manager = IslandRaretyManager.getInstance();
		List<Inventory> invs = manager.getInventories(this);
		if(invs == null) {
			return;
		}
		for(Inventory inv:  invs) {
			int firstEmpty = inv.firstEmpty();
			if(firstEmpty == -1) {
				continue;
			}
			inv.setItem(firstEmpty, ItemStack.deserializeBytes(map));
		}
		manager.put(this, invs);
	}


	public void save() {
		IslandRaretyManager.getInstance().saveItems(this);
	}
}