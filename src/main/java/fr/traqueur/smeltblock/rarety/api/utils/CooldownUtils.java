package fr.traqueur.smeltblock.rarety.api.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class CooldownUtils {
	private static HashMap<String, HashMap<UUID, Long>> cooldowns = new HashMap<String, HashMap<UUID, Long>>();

	public static void createCooldown(String alias) {
		if (cooldowns.containsKey(alias)) {
			return;
		}
		cooldowns.put(alias, new HashMap<UUID, Long>());
	}

	public static void addCooldown(String alias, Player player, int seconds) {
		if (!cooldowns.containsKey(alias)) {
			CooldownUtils.createCooldown(alias);
		}
		long next = System.currentTimeMillis() + (long) seconds * 1000;
		cooldowns.get(alias).put(player.getUniqueId(), next);
	}

	public static String getCooldownRemaining(Player player, String name) {
		return DurationFormatter.getRemaining(CooldownUtils.getCooldownForPlayerLong(name, player), true);
	}

	public static void removeCooldown(String alias, Player player) {
		if (!cooldowns.containsKey(alias)) {
			return;
		}
		cooldowns.get(alias).remove(player.getUniqueId());
	}

	public static HashMap<UUID, Long> getCooldownMap(String alias) {
		if (cooldowns.containsKey(alias)) {
			return cooldowns.get(alias);
		}
		return null;
	}

	public static boolean isOnCooldown(String alias, Player player) {
		if (cooldowns.containsKey(alias) && cooldowns.get(alias).containsKey(player.getUniqueId())
				&& System.currentTimeMillis() <= cooldowns.get(alias).get(player.getUniqueId())) {
			return true;
		}
		return false;
	}

	public static int getCooldownForPlayerInt(String alias, Player player) {
		return (int) CooldownUtils.getCooldownForPlayerLong(alias, player);
	}

	public static long getCooldownForPlayerLong(String alias, Player player) {
		return cooldowns.get(alias).get(player.getUniqueId()) - System.currentTimeMillis();
	}

	public static void clearCooldowns() {
		cooldowns.clear();
	}
}