package fr.traqueur.smeltblock.rarety.modules.rarety;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import xzot1k.plugins.ds.api.events.ShopItemSetEvent;

public class ShopListener implements Listener {

    @EventHandler
    public void onSetItemInShop(ShopItemSetEvent event) {
        IslandRaretyManager manager = IslandRaretyManager.getInstance();
        ItemStack item = event.getItemStack();
        if(manager.isRaretyItem(item)) {
            event.setCancelled(true);
        }
    }
}
