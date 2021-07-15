package fr.traqueur.smeltblock.rarety.modules.rarety;

import fr.traqueur.smeltblock.rarety.api.commands.annotations.Command;
import fr.traqueur.smeltblock.rarety.modules.profiles.ProfileManager;
import fr.traqueur.smeltblock.rarety.modules.profiles.clazz.Profile;
import org.bukkit.entity.Player;

import fr.traqueur.smeltblock.rarety.api.commands.CommandArgs;
import fr.traqueur.smeltblock.rarety.api.commands.ICommand;
import fr.traqueur.smeltblock.rarety.api.utils.Utils;

public class RaretyCommand extends ICommand {

	@Command(inGameOnly = true, name ="rarety", aliases = "items")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		if(args.length() == 0) {
			IslandRaretyManager.getInstance().openInv(args.getPlayer(), 1);
			return;
		}
			
		
		if(args.length() != 1 && player.hasPermission("rarety.admin")) {
			player.sendMessage(Utils.color("&cUsage: /items <joueur>"));
			return;
		}
		
		if(args.length() == 1 && player.hasPermission("rarety.admin")) {
			ProfileManager pm = ProfileManager.getInstance();
			Profile pr = pm.getProfile(args.getArgs(0));
			if(pr == null) {
				player.sendMessage(Utils.color("&cErreur, joueur introuvable"));
				return;
			}
			IslandRaretyManager.getInstance().openInv(args.getPlayer(), pr, 1);
		}
	}

}
