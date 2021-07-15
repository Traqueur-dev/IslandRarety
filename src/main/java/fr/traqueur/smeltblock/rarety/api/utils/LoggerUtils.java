package fr.traqueur.smeltblock.rarety.api.utils;

import fr.traqueur.smeltblock.rarety.api.Plugin;

public class LoggerUtils {

	public static void log(String message) {
		log(Logger.Level.INFO, message);
	}

	public static void warning(String message) {
		log(Logger.Level.WARNING, message);
	}

	public static void error(String message) {
		log(Logger.Level.SEVERE, message);
	}

	public static void success(String message) {
		log(Logger.Level.SUCCESS, message);
	}

	public static void log(Logger.Level level, String message) {
		Logger.log(level, Plugin.getInstance(), message);
	}
}