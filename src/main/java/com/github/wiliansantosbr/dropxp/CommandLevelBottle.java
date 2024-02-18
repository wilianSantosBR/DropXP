package com.github.wiliansantosbr.dropxp;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * handles the execution of the /droplvl command, which is used to give admins
 * xp bottles based off of levels rather than actual xp
 * @author Jason
 *
 */
public class CommandLevelBottle implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(!(sender instanceof Player)) return false;
		Player player = (Player)sender;

		//checks if their inventory is full
		if(player.getInventory().firstEmpty() == -1) {
			player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.BLUE + "Seu inventário está cheio!");
			return true;
		}

		//checks if they've entered their arguments correctly
		else if(args != null && args.length == 1) {
			int xp;
			//checks if they've entered an integer, and gives them the bottle if it is
			try {
				xp = Integer.parseInt(args[0]);
				player.getInventory().addItem(MainListener.makeLevelBottle(xp));
				player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.65f);
			}
			catch(NumberFormatException e) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "Número inválido");
			}
			return true;
		}

		//returns false if they don't enter their arguments correctly
		else {
			return false;
		}
	}

}
