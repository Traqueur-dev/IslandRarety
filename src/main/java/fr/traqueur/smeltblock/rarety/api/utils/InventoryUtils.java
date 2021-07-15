package fr.traqueur.smeltblock.rarety.api.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class InventoryUtils {

	public static boolean isNullItem(ItemStack item) {
		return item == null || item.getType() == Material.AIR;
	}

	public static void addItem(Player player, ItemStack item) {
		InventoryUtils.addItem(player, item, 1);
	}

	public static void addItem(Player player, ItemStack item, int quantity) {
		PlayerInventory inventory = player.getInventory();
		ItemStack current = new ItemStack(item);
		if(isFullInventory(player)) {
			int max = current.getMaxStackSize();
			while(quantity > max) {
				current = current.clone();
				current.setAmount(max);
				player.getWorld().dropItem(player.getLocation(), current);
				quantity -= max;
			}
			current = current.clone();
			current.setAmount(quantity);
			player.getWorld().dropItem(player.getLocation(), current);
			return;
		}
		
		int max = current.getMaxStackSize();
		if (quantity > max) {
			int add;
			for (int leftOver = quantity; leftOver > 0; leftOver -= add) {
				add = 0;
				add = leftOver <= max ? (add += leftOver) : (add += max);
				current = current.clone();
				current.setAmount(add);
				inventory.addItem(new ItemStack[] { current });
			}
		} else {
			current = current.clone();
			current.setAmount(quantity);
			inventory.addItem(new ItemStack[] { current });
		}
	}

	public static boolean haveRequiredItem(Player player, ItemStack item) {
		return InventoryUtils.haveRequiredItem(player, item, 1);
	}

	public static boolean haveRequiredItem(Player player, ItemStack item, int quantity) {
		int quantityInInventory = InventoryUtils.getItemCount(player, item);
		return quantityInInventory >= quantity;
	}

	public static boolean isFullInventory(Player player) {
		PlayerInventory inventory = player.getInventory();
		for (ItemStack current : inventory.getContents()) {
			if (!InventoryUtils.isNullItem(current))
				continue;
			return false;
		}
		return true;
	}

	public static boolean hasSpaceInventory(Player player, ItemStack item, int count) {
		int left = count;
		ItemStack[] items = player.getInventory().getContents();
		for (int i = 0; i < items.length; ++i) {
			ItemStack stack = items[i];
			if (stack == null || stack.getType() == Material.AIR) {
				left -= item.getMaxStackSize();
			} else if (stack.getType() == item.getType() && stack.getData().getData() == item.getData().getData()
					&& item.getMaxStackSize() > 1 && stack.getAmount() < item.getMaxStackSize()) {
				left -= stack.getMaxStackSize() - stack.getAmount();
			}
			if (left <= 0)
				break;
		}
		return left <= 0;
	}

	public static int getItemCount(Player player, ItemStack item) {
		int quantityInInventory = 0;
		PlayerInventory inventory = player.getInventory();
		for (ItemStack current : inventory.getContents()) {
			if (InventoryUtils.isNullItem(current) || current.getType() != item.getType()
					|| current.getData().getData() != item.getData().getData())
				continue;
			quantityInInventory += current.getAmount();
		}
		return quantityInInventory;
	}

	public static void decrementCurrentItem(Player player, ItemStack item, int quantity) {
		int currentAmount = item.getAmount();
		if (currentAmount <= 1) {
			player.setItemInHand(null);
		} else {
			int amount = currentAmount - quantity;
			item.setAmount(amount);
		}
	}

	public static void decrementItem(Player player, ItemStack item, int quantity) {
		int toRemove = quantity;
		PlayerInventory inv = player.getInventory();
		for (ItemStack is : inv.getContents()) {
			if (toRemove <= 0)
				break;
			if (is == null || is.getType() != item.getType() || is.getData().getData() != item.getData().getData())
				continue;
			int amount = is.getAmount() - toRemove;
			toRemove -= is.getAmount();
			if (amount <= 0) {
				player.getInventory().removeItem(new ItemStack[] { is });
				continue;
			}
			is.setAmount(amount);
		}
	}

	public static void addItem(Inventory inv, ItemStack item, Integer quantity) {
		ItemStack current = new ItemStack(item);
		
		int max = current.getMaxStackSize();
		if (quantity > max) {
			int add;
			for (int leftOver = quantity; leftOver > 0; leftOver -= add) {
				add = 0;
				add = leftOver <= max ? (add += leftOver) : (add += max);
				current = current.clone();
				current.setAmount(add);
				inv.addItem(new ItemStack[] { current });
			}
		} else {
			current = current.clone();
			current.setAmount(quantity);
			inv.addItem(new ItemStack[] { current });
		}
	}
}