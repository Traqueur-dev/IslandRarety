package fr.traqueur.smeltblock.rarety.modules.profiles.clazz;

import java.io.File;
import java.util.List;

import org.bukkit.entity.Player;
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
		if(!match(map)) {
			raretyItems.add(map);
		}	
	}

	public boolean match(byte[] byt) {
		for (byte[] b : raretyItems) {
			if (match(b, byt)) {
				return true;
			}
		}
		return false;
	}

	private boolean match(byte[] item, byte[] item2) {
		if (item.length != item2.length)
			return false;
		for (int i = 0; i < item2.length; i++) {
			if (item[i] != item2[i])
				return false;
		}
		return true;
	}

	public void removeItem(ItemStack current) {
		raretyItems.remove(current.serializeAsBytes());
	}
}