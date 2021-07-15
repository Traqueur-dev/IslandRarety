package fr.traqueur.smeltblock.rarety.api.modules;

import java.util.List;

import com.google.common.collect.Lists;

import fr.traqueur.smeltblock.rarety.api.Plugin;
import fr.traqueur.smeltblock.rarety.api.utils.LoggerUtils;

public class ModuleManager {

	private static ModuleManager instance;
	private List<Module> modules;

	public ModuleManager() {
		instance = this;
		this.modules = Lists.newArrayList();
	}

	public void registerManager(Module module) {
		LoggerUtils.log("Création du module " + module.getName() + "...");
		this.modules.add(module);
		LoggerUtils.success("Création effectuée avec succés.");
	}

	public void registerPersist(Module module) {
		this.registerManager(module);
	}

	public void registerPersist(Saveable module) {
		this.registerManager(module);
		Plugin plugin = module.getPlugin();
		plugin.registerPersist(module);
	}
	
	public static ModuleManager getInstance() {
		return instance;
	}
	
	public List<Module> getModules() {
		return modules;
	}
	
}