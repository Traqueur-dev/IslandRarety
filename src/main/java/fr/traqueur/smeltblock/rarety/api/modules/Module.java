package fr.traqueur.smeltblock.rarety.api.modules;

import fr.traqueur.smeltblock.rarety.api.Plugin;
import fr.traqueur.smeltblock.rarety.api.commands.CommandFramework;
import fr.traqueur.smeltblock.rarety.api.commands.ICommand;
import org.bukkit.event.Listener;

import fr.traqueur.smeltblock.rarety.api.utils.LoggerUtils;

public abstract class Module {
	
	private Plugin plugin;
	private String name;

	public Module(Plugin plugin, String name) {
		this.plugin = plugin;
		this.name = name;
	}

	public void registerCommand(ICommand command) {
		CommandFramework framework = this.plugin.getFramework();
		
		LoggerUtils.log("Création de la commande " + command.getClass().getName() + "...");
		framework.registerCommands(command);
		LoggerUtils.success("Enregistrement effectuée avec succés.");
	}

	public void registerListener(Listener listener) {
		this.plugin.registerListener(listener);
	}

	public String getName() {
		return name;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	
}
