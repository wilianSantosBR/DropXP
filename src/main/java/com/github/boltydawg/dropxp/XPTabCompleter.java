package com.github.boltydawg.dropxp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
/**
 * This class adds an auto-suggested tab completion
 * for if someone has permission to use the leveled bottle
 * functionality
 * @author BoltyDawg
 *
 */
public class XPTabCompleter implements TabCompleter{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(args != null && args.length == 1 && player.hasPermission("dropxp.leveled")) {
				List<String> commands = new ArrayList<>();
				commands.add("level");
				List<String> completions = new ArrayList<>();
				StringUtil.copyPartialMatches(args[0], commands, completions);
				Collections.sort(completions);
				return completions;
			}
		}
		return null;
	}

}
