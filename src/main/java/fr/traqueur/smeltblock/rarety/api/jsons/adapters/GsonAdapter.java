package fr.traqueur.smeltblock.rarety.api.jsons.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import fr.traqueur.smeltblock.rarety.api.Plugin;

public abstract class GsonAdapter<T> extends TypeAdapter<T> {

	private Plugin plugin;

	public GsonAdapter(Plugin plugin) {
		this.plugin = plugin;
	}

	public Gson getGson() {
		return this.plugin.getGson();
	}
}