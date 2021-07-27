package fr.traqueur.smeltblock.rarety;

import fr.traqueur.smeltblock.rarety.api.Plugin;
import org.bukkit.Bukkit;

import fr.traqueur.smeltblock.rarety.api.modules.ModuleManager;
import fr.traqueur.smeltblock.rarety.modules.profiles.ProfileManager;
import fr.traqueur.smeltblock.rarety.modules.rarety.IslandRaretyManager;
import xzot1k.plugins.ds.DisplayShops;
import xzot1k.plugins.ds.DisplayShopsAPI;

public class IslandRarety extends Plugin {

	private static IslandRarety INSTANCE;

	private DisplayShopsAPI displayShopsAPI;

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
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> IslandRarety.getINSTANCE().save(), 20, 20 * 60 * 30);

		isDisplayShopsInstalled();
	}

	private boolean isDisplayShopsInstalled()
	{
		DisplayShops ds = (DisplayShops) getServer().getPluginManager().getPlugin("DisplayShops");

		if(ds != null)
		{
			displayShopsAPI = ds;
			return true;
		}

		return false;
	}

	public static IslandRarety getINSTANCE() {
		return INSTANCE;
	}

	public DisplayShopsAPI getDisplayShopsAPI() {
		return displayShopsAPI;
	}
}
