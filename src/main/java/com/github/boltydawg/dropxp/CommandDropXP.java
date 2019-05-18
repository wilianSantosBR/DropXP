package com.github.boltydawg.dropxp;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the execution of the /dropxp command
 * @author Jason
 *
 */
public class CommandDropXP implements CommandExecutor {
	/**
	 * This method fires everytime someone uses the /dropxp command.
	 * The code here is kinda messy, but a lot of it is repeated stuff with slightly different values.
	 * I'm aware that this isn't necessarily good programming practice but there's a lot to handle here and
	 * this was the best way that I could think of to bug-proof my product
	 * TODO clean up this code. Maybe do a for loop on args?
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = ((Player) sender);
		//checks if someone's inventory is full
		if(player.getInventory().firstEmpty() == -1) {
			if(Main.thicc) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.YELLOW + "Right click while holding a thick potion to store your xp inside of it!");
				return true;
			}
			player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.BLUE + "Your inventory is full!");
			return true;
		}
		//checks if the player didn't enter any arguments following /dropxp
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
		//checks if the player entered a number after /dropxp
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
		//checks if the player tried using the leveled potion feature
		else if(args.length == 2 && args[0].equals("level")) {
			if(!player.hasPermission("dropxp.leveled")) {
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
		//returns false if they enter more than 2 arguments, which sends the player a message containing the proper syntax of this command
		else
			return false;
	}
}
