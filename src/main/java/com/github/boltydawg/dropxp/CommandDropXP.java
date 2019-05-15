package com.github.boltydawg.dropxp;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDropXP implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = ((Player) sender);
		if(Main.thicc) {
			player.sendMessage(ChatColor.DARK_AQUA + "Right click while holding a thick potion to store your xp inside of it!");
			return true;
		}
		else if(player.getInventory().firstEmpty()==-1) {
			player.sendMessage(ChatColor.BLUE + "Your inventory is full!");
			return true;
		}
		else if(!player.hasPermission("dropxp.specify") || args == null || args.length == 0){
			int xp = Experience.getExp(player);
			if(xp>0) {
				player.getInventory().addItem( ThiccListener.makeBottle(xp, player.getDisplayName()) );
				Experience.changeExp(player, -xp);
				player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.5f);
				return true;
			}
			player.sendMessage(ChatColor.BLUE + "You need some xp!");
			return true;
		}
		else {
			int xp;
			try {
				xp = Experience.getExpFromLevel(Integer.parseInt(args[0]));
			}
			catch(NumberFormatException e) {
				player.sendMessage(ChatColor.RED + "Invalid Number");
				return true;
			}
			int playerxp = Experience.getExp(player);
			if(playerxp<xp) {
				player.sendMessage(ChatColor.RED + "You overestimate your power...");
				return true;
			}
			if(xp>0) {
				player.getInventory().addItem( ThiccListener.makeBottle(xp, player.getDisplayName()) );
				Experience.changeExp(player, -xp);
				player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.5f);
				return true;
			}
			player.sendMessage(ChatColor.RED + "You can't drop 0 or less xp");
			return true;
		}
	}
}
