package fr.traqueur.smeltblock.rarety.api;

import java.lang.reflect.Modifier;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.traqueur.smeltblock.rarety.api.commands.CommandFramework;
import fr.traqueur.smeltblock.rarety.api.commands.ICommand;
import fr.traqueur.smeltblock.rarety.api.jsons.JsonPersist;
import fr.traqueur.smeltblock.rarety.api.jsons.adapters.LocationAdapter;
import fr.traqueur.smeltblock.rarety.api.modules.ModuleManager;
import fr.traqueur.smeltblock.rarety.api.utils.LoggerUtils;

public abstract class Plugin extends JavaPlugin {

	protected static Plugin instance;

	private Gson gson;
	private CommandFramework framework;
	private ModuleManager moduleManager;
	private List<JsonPersist> persists;
	
	public Plugin() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		LoggerUtils.log("Vérification du dossier de données...");
		this.getDataFolder().mkdir();
		LoggerUtils.success("Vérification effectuée avec succés.");

		LoggerUtils.log("Vérification du module GSON...");
		this.gson = this.createGsonBuilder().create();
		LoggerUtils.success("Vérification effectuée avec succés.");
		
		this.persists = Lists.newArrayList();

		LoggerUtils.log("Initialisation du module des commandes...");
		this.framework = new CommandFramework(this);
		framework.registerHelp();
		LoggerUtils.success("Initialisation effectuée avec succés.");
		
		LoggerUtils.log("Initialisation des modules...");
		this.moduleManager = new ModuleManager();
		LoggerUtils.success("Initialisation effectuée avec succés.");
		
		this.registerManagers();
		this.registerOthers();

		LoggerUtils.log("Chargement des configurations...");
		this.loadPersists();
		LoggerUtils.success("Chargement effectuée avec succés.");
	}

	@Override
	public void onDisable() {
		this.savePersists();
		MultiThreading.POOL.shutdown();
		MultiThreading.RUNNABLE_POOL.shutdown();
	}
	
	private GsonBuilder createGsonBuilder() {
		GsonBuilder ret = new GsonBuilder();

		ret.setPrettyPrinting();	
		ret.disableHtmlEscaping();
		ret.excludeFieldsWithModifiers(Modifier.TRANSIENT);
		
		ret.registerTypeAdapter(Location.class, new LocationAdapter(this));

		return ret;
	}

	public void registerPersist(JsonPersist persist) {
		this.persists.add(persist);
	}

	public void registerListener(Listener listener) {
		LoggerUtils.log("Enregistrement du listener " + listener.getClass().getName() + "...");
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(listener, this);
		LoggerUtils.success("Enregistrement effectuée avec succés.");
	}

	public void registerCommand(ICommand command) {
		LoggerUtils.log("Enregistrement du listener " + command.getClass().getName() + "...");
		getFramework().registerCommands(command);
		LoggerUtils.success("Enregistrement effectuée avec succés.");
	}



	public void loadPersists() {
		for (JsonPersist persist : this.persists) {
			persist.loadData();
		}
	}

	public void savePersists() {
		for (JsonPersist persist : this.persists) {
			persist.saveData();
		}
	}

	public abstract void registerManagers();

	public abstract void registerOthers();

	
	public static Plugin getInstance() {
		return instance;
	}
	
	public Gson getGson() {
		return this.gson;
	}

	public CommandFramework getFramework() {
		return framework;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}
	
	public void save() {
		long time = System.currentTimeMillis();
		this.savePersists();
		time = System.currentTimeMillis() - time;
		LoggerUtils.success("Sauvegarde des données (" + time + "ms)");
	}
	
}
