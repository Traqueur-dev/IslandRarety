package fr.traqueur.smeltblock.rarety.api.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Utils {

	public static String LINE = color("&7&m" + StringUtils.repeat("-", 44));
	private static Random random = new Random();

	public static String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static void deleteFile(File file) {
		if (file.exists()) {
			file.delete();
		}
	}

	public static <T> List<T> convertArrayToList(T array[]) {
		List<T> list = new ArrayList<>();
		for (T t : array) {
			list.add(t);
		}
		return list;
	}

	public static String toStringOnOff(boolean value) {
		return value ? Utils.color("&aON") : Utils.color("&cOFF");
	}

	public static String toStringOnOff(boolean value, boolean desactivable) {
		return !desactivable ? Utils.color("&4&lNON DESACTIVABLE") : Utils.toStringOnOff(value);
	}

	public static String formatLocation(Location location) {
		return Utils.color("&6X&7: &e" + location.getBlockX() + " &6Y&7: &e" + location.getBlockY() + " &6Z&7: &e"
				+ location.getBlockZ());
	}

	public static Collection<? extends Player> getOnlinePlayers() {
		return Bukkit.getOnlinePlayers();
	}

	public static boolean emptyInventory(Player player) {
		PlayerInventory inv = player.getInventory();
		for (int i = 0; i < inv.getContents().length; i++) {
			ItemStack is = inv.getContents()[i];
			if (is != null) {
				return false;
			}
		}
		for (int i = 0; i < inv.getArmorContents().length; i++) {
			ItemStack is = inv.getArmorContents()[i];
			if (is != null) {
				return false;
			}
		}
		return true;
	}

	public static int randomInt(int min, int max) {
		Random random = new Random();
		int range = max - min + 1;
		int randomNum = random.nextInt(range) + min;
		return randomNum;
	}

	public static String getPrefix(String prefix) {
		return color("&7(&9" + prefix + "&7) &b");
	}

	public static File getFormatedFile(File fileName) {
		return fileName;
	}

	public static <T> T random(List<T> collection) {
		return collection.size() == 0 ? null : collection.get(random.nextInt(collection.size()));
	}

	public static int random(int min, int max) {
		return min + random(max - min + 1);
	}

	private static int random(int range) {
		return range <= 0 ? 0 : random.nextInt(range);
	}

	public static boolean stringToBool(String s) {
		s = s.toLowerCase();

		Set<String> trueSet = new HashSet<String>(Arrays.asList("true"));
		Set<String> falseSet = new HashSet<String>(Arrays.asList("false"));

		if (trueSet.contains(s)) {
			return true;
		} else if (falseSet.contains(s)) {
			return false;
		}

		throw new IllegalArgumentException(s + " is not a boolean.");
	}

}