package fr.traqueur.smeltblock.rarety.api.commands;

import org.bukkit.entity.Player;

import fr.traqueur.smeltblock.rarety.api.utils.CooldownUtils;
import fr.traqueur.smeltblock.rarety.api.utils.DurationFormatter;
import fr.traqueur.smeltblock.rarety.api.utils.Utils;

public abstract class ICommand {
	
	public abstract void onCommand(CommandArgs args);
	
	public void logCommand(CommandArgs args) {
		if (!args.isPlayer()) {
			return;
		}
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < args.getArgs().length; i++) {
			buffer.append(args.getArgs(i) + " ");
		}
		
		for (Player player : Utils.getOnlinePlayers()) {
			if (player.hasPermission("base.log.command")) {
				player.sendMessage(Utils.color(Utils.getPrefix("Staff") + "&eLe joueur &c" + args.getPlayer().getName() + " &evient d'utiliser la commande&7: "
						+ "&6/" + args.getLabel() + " " + buffer.toString()));
			}
		}
	}
	
    public boolean checkCooldown(Player target, int seconds) {
        String name = this.getClass().getName().substring(0, 16);
        String cooldownName = name + "Cmd";
        if (CooldownUtils.isOnCooldown(cooldownName, target) && !target.hasPermission("core.cooldown")) {
            String cooldown = DurationFormatter.getRemaining(CooldownUtils.getCooldownForPlayerLong(cooldownName, target), true);
            target.sendMessage(Utils.color("&eVeuillez patienter &c" + cooldown + " &eavant de pouvoir utiliser cette commande!"));
            return false;
        }
        CooldownUtils.addCooldown(cooldownName, target, seconds);
        return true;
    }
}