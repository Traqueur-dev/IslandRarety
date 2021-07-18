package fr.traqueur.smeltblock.rarety.modules.profiles;

import java.io.File;
import java.util.List;

import fr.traqueur.smeltblock.rarety.api.Plugin;
import fr.traqueur.smeltblock.rarety.modules.profiles.clazz.Profile;
import fr.traqueur.smeltblock.rarety.modules.rarety.IslandRaretyManager;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;

import fr.traqueur.smeltblock.rarety.api.jsons.DiscUtil;
import fr.traqueur.smeltblock.rarety.api.modules.Saveable;

public class ProfileManager extends Saveable {

	private List<Profile> profiles;
	
	private static ProfileManager instance;
	
	public ProfileManager(Plugin plugin) {
		super(plugin, "Profiles");
		instance = this;
		profiles = Lists.newArrayList();
		this.registerListener(new ProfileListener());
	}

	
	public Profile getProfile(String value) {
		for (Profile profile : this.profiles) {
			if (profile.getName().equalsIgnoreCase(value)) {
				return profile;
			}
		}
		return null;
	}
	
	public Profile getProfile(Player value) {
		for (Profile profile : this.profiles) {
			if (profile.getName().equalsIgnoreCase(value.getName())) {
				return profile;
			}
		}
		return null;
	}

	public Profile createProfile(Profile profile) {
		if (!this.profileExist(profile.getName())) {
			this.profiles.add(profile);
			return profile;
		}
		return this.getProfile(profile.getName());
	}

	public Profile createProfile(Player player) {
		Profile profile = new Profile(player);
		return this.createProfile(profile);
	}

	public boolean profileExist(String name) {
		for (Profile profile : getProfiles()) {
			if (profile.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	@Override
	public File getFile() {
		return new File(this.getPlugin().getDataFolder(), "/profiles/");
	}

	@Override
	public void loadData() {
		if (!this.getFile().exists()) {
			this.getFile().mkdir();
		}
		File[] files = this.getFile().listFiles();
		if(files == null || files.length == 0) {return;}
		for (int count = 0; count < files.length; ++count) {
			File file = files[count];
			String content = DiscUtil.readCatch((File) file);
			if (content == null)
				continue;
			try {
				Profile profile = (Profile) this.getGson().fromJson(content, new TypeToken<Profile>() {
				}.getType());
				this.createProfile(profile);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	@Override
	public void saveData() {
		for (Profile profile : this.profiles) {
			profile.save();
			DiscUtil.writeCatch(profile.getProfileFile(), this.getGson().toJson(profile));
		}
	}
	
	public static ProfileManager getInstance() {
		return instance;
	}

}
