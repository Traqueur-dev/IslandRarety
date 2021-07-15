package fr.traqueur.smeltblock.rarety.api.modules;


import com.google.gson.Gson;

import fr.traqueur.smeltblock.rarety.api.Plugin;
import fr.traqueur.smeltblock.rarety.api.jsons.JsonPersist;

public abstract class Saveable extends Module implements JsonPersist {

	public transient boolean needDir, needFirstSave;

	public Saveable(Plugin plugin, String name) {
		super(plugin, name);
	}
	
	public Gson getGson() {
		return this.getPlugin().getGson();
	}
}