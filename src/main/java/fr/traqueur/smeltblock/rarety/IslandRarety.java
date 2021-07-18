package fr.traqueur.smeltblock.rarety;

import fr.traqueur.smeltblock.rarety.api.Plugin;
import org.bukkit.Bukkit;

import fr.traqueur.smeltblock.rarety.api.modules.ModuleManager;
import fr.traqueur.smeltblock.rarety.modules.profiles.ProfileManager;
import fr.traqueur.smeltblock.rarety.modules.rarety.IslandRaretyManager;

public class IslandRarety extends Plugin {

	private static IslandRarety INSTANCE;

	public IslandRarety() {
		INSTANCE = this;
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}


	@Override
	public void registerManagers() {
		ModuleManager mm = this.getModuleManager();
		mm.registerPersist(new IslandRaretyManager(this));
		mm.registerPersist(new ProfileManager(this));
	}

	@Override
	public void registerOthers() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {

			@Override
			public void run() {
				IslandRarety.getINSTANCE().save();

			}
		}, 20, 20 * 60 * 30);
	}

	public static IslandRarety getINSTANCE() {
		return INSTANCE;
	}

}
