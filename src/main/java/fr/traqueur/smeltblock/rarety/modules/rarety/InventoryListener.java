package fr.traqueur.smeltblock.rarety.modules.rarety;

import fr.traqueur.smeltblock.rarety.api.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import fr.traqueur.smeltblock.rarety.api.utils.CooldownUtils;
import fr.traqueur.smeltblock.rarety.api.utils.Utils;

public class InventoryListener implements Listener {

	private IslandRaretyManager manager = IslandRaretyManager.getInstance();

	@EventHandler
	public void onCraft(PrepareItemCraftEvent event) {
		for (ItemStack i : event.getInventory()) {
			if (i == null)
				continue;
			if (manager.isRaretyItem(i)) {
				event.getInventory().setResult(new ItemStack(Material.AIR));
			}
		}
	}

	@EventHandler
	public void onAnvil(PrepareAnvilEvent event) {
		for (ItemStack i : event.getInventory()) {
			if (i == null || i.getType() == Material.AIR)
				continue;
			if (manager.isRaretyItem(i)) {
				event.getInventory().setResult(new ItemStack(Material.AIR));
				event.getViewers().forEach(h -> ((Player) h).updateInventory());
			}
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		ItemStack item = event.getItemDrop().getItemStack();
		if (manager.isRaretyItem(item) && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			if (!CooldownUtils.isOnCooldown("drop", event.getPlayer())) {
				event.getPlayer()
						.sendMessage(Utils.color("&f『&b&lItems Rares&f』⇨ "
								+ "&6Le seul endroit où tu peux ranger cet item rare c'est dans ton inventaire privé"
								+ " &f⌞&c&l/items&f⌝"));
				CooldownUtils.addCooldown("drop", event.getPlayer(), 5);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;

		InventoryView view = event.getView();
		ItemStack item = event.getCurrentItem();
		ItemStack cursor = event.getCursor();
		Inventory topInv = view.getTopInventory();
		Player player = (Player) event.getWhoClicked();

		if(topInv.getType() == InventoryType.CRAFTING) {
			if(event.getSlotType() == InventoryType.SlotType.ARMOR) {
				if(manager.isRaretyItem(cursor)) {
					event.setCancelled(true);
					return;
				}
			}

		}

		if (manager.isRaretyInventory(view)) {
			if (event.getClick() == ClickType.NUMBER_KEY && (!manager.isRaretyItem(item)
					|| (!manager.isRaretyItem(player.getInventory().getItem(event.getHotbarButton()))))) {
				event.setCancelled(true);
			}

			if (!manager.isRaretyItem(item) && !manager.isRaretyItem(cursor)) {
				event.setCancelled(true);
			}

			if(item == null) {
				return;
			}

			String name = item.getItemMeta().getDisplayName();
			if (name.contains("Page")) {
				event.setCancelled(true);
				if (name.contains("suivante")) {
					manager.nextPage(player);
				} else if (name.contains("précédente")) {
					manager.previousPage(player);
				}
			}
		} else {


			if(manager.isRaretyItem(player.getInventory().getHelmet())) {
				ItemStack helmet = player.getInventory().getHelmet().clone();
				player.getInventory().setHelmet(null);
				InventoryUtils.addItem(player, helmet, helmet.getAmount());
			}

			if (event.getClick() == ClickType.NUMBER_KEY && (manager.isRaretyItem(item)
					|| (manager.isRaretyItem(player.getInventory().getItem(event.getHotbarButton()))))) {
				event.setCancelled(true);
			}

			if (topInv.getType() != InventoryType.CRAFTING && (manager.isRaretyItem(item) || manager.isRaretyItem(cursor))) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		IslandRaretyManager manager = IslandRaretyManager.getInstance();
		if (event.getRightClicked() instanceof ItemFrame) {
			if (manager.isRaretyItem(event.getPlayer().getInventory().getItemInMainHand())
					|| manager.isRaretyItem(event.getPlayer().getInventory().getItemInOffHand())) {
				event.setCancelled(true);
			}

		}
	}

}
