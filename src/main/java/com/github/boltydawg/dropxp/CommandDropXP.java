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
		if(player.getInventory().firstEmpty()==-1) {
			if(Main.thicc) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.YELLOW + "Right click while holding a thick potion to store your xp inside of it!");
				return true;
			}
			player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.BLUE + "Your inventory is full!");
			return true;
		}
		else if(args == null || args.length == 0){
			if(Main.thicc) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.YELLOW + "Right click while holding a thick potion to store your xp inside of it!");
				return true;
			}
			int xp = Experience.getExp(player);
			if(xp>0) {
				player.getInventory().addItem( MainListener.makeBottle(xp, player.getDisplayName()) );
				Experience.changeExp(player, -xp);
				player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.5f);
				return true;
			}
			player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.BLUE + "You need some xp!");
			return true;
		}
		else if(args.length == 1) {
			if(Main.thicc) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.YELLOW + "Right click while holding a thick potion to store your xp inside of it!");
				return true;
			}
			int xp;
			try {
				xp = Experience.getExpFromLevel(Integer.parseInt(args[0]));
			}
			catch(NumberFormatException e) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "Invalid Number");
				return true;
			}
			int playerxp = Experience.getExp(player);
			if(playerxp<xp) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "You overestimate your power...");
				return true;
			}
			if(xp>0) {
				player.getInventory().addItem( MainListener.makeBottle(xp, player.getDisplayName()) );
				Experience.changeExp(player, -xp);
				player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.5f);
				return true;
			}
			player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "You can't drop 0 or less xp");
			return true;
		}
		else if(args.length == 2 && args[0].equals("admin")) {
			if(!player.hasPermission("dropxp.admin")) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "You don't have permission to do that");
				return true;
			}
			int xp;
			try {
				xp = Integer.parseInt(args[1]);
				player.getInventory().addItem(MainListener.makeLevelBottle(xp));
			}
			catch(NumberFormatException e) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "Invalid Number");
			}
			return true;
		}
		else
			return false;
		
		
//		else if(Main.thicc) {
//			player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.YELLOW + "Right click while holding a thick potion to store your xp inside of it!");
//			return true;
//		}
//		else if(args[0] != null && args[]){
//			int xp;
//			try {
//				xp = Experience.getExpFromLevel(Integer.parseInt(args[0]));
//			}
//			catch(NumberFormatException e) {
//				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "Invalid Number");
//				return true;
//			}
//			int playerxp = Experience.getExp(player);
//			if(playerxp<xp) {
//				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "You overestimate your power...");
//				return true;
//			}
//			if(xp>0) {
//				player.getInventory().addItem( ThiccListener.makeBottle(xp, player.getDisplayName()) );
//				Experience.changeExp(player, -xp);
//				player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.5f);
//				return true;
//			}
//			player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "You can't drop 0 or less xp");
//			return true;
//		}
//		else if(args[1] == "admin") {
//			if(!player.hasPermission("dropxp.admin")) {
//				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "You don't have permission to use this feature");
//				return true;
//			}
//			int xp;
//			try {
//				xp = Experience.getExpFromLevel(Integer.parseInt(args[1]));
//			}
//			catch(NumberFormatException e) {
//				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "Invalid Number");
//				return true;
//			}
//			player.getInventory().addItem(ThiccListener.makeLevelBottle(xp));
//			return true;
//		}
//		else
//			return false;
	}
}
