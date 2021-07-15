package fr.traqueur.smeltblock.rarety.modules.profiles;

import fr.traqueur.smeltblock.rarety.modules.profiles.clazz.Profile;
import fr.traqueur.smeltblock.rarety.modules.rarety.IslandRaretyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		ProfileManager profileManager = ProfileManager.getInstance();
		IslandRaretyManager manager = IslandRaretyManager.getInstance();
		Player player = event.getPlayer();
		Profile profile = profileManager.createProfile(player);
		manager.setupInv(profile);
		//player.getInventory().addItem(new ItemBuilder(Material.DIAMOND).lore("Pioche de").build());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		IslandRaretyManager manager = IslandRaretyManager.getInstance();
		Player player = event.getPlayer();
		manager.disconnect(player);
	}
	
}
