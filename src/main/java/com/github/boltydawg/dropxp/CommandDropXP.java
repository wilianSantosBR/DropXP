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
	 * Deals with 2 different possible arguments, no arguments or an int specifying how much xp to drop
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) return false;
		Player player = ((Player) sender);
		
		//This command only gives instructions if requireThickPotion is set to true in the config
		if(Main.thicc) {
			player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.YELLOW + "Clique com o botão direito enquanto segura uma poção grossa para armazenar seu XP dentro dela!");
			return true;
		}
		
		//checks if someone's inventory is full
		else if(player.getInventory().firstEmpty() == -1) {
			player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.BLUE + "Seu inventário está cheio!");
			return true;
		}
		
		//checks if the player didn't enter any arguments following /dropxp
		else if(args == null || args.length == 0){
			int xp = Experience.getExp(player);
			if(xp>0) {
				xpHelper(player, xp);
			}
			else {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.BLUE + "Você precisa de algum XP!");
			}
			return true;
		}
		
		//checks if the player entered a number after /dropxp
		else if(args.length == 1) {
			if(!player.hasPermission("dropxp.specify")) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "Você não tem permissão para descartar quantidades definidas de XP");
				return true;
			}
			int xp;
			try {
				xp = Experience.getExpFromLevel(Integer.parseInt(args[0]));
			}
			catch(NumberFormatException e) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "Número inválido");
				return true;
			}
			int playerxp = Experience.getExp(player);
			if(playerxp<xp) {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "Você superestima seu poder...");
		}
			else if(xp>0) {
				xpHelper(player, xp);
			}
			else {
				player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] "+ ChatColor.RED + "Você não pode perder 0 ou menos XP");
			}
			return true;
		}
		//returns false if they enter more than 1 argument, which sends the player a message containing the proper syntax of this command
		else {
			return false;
		}
	}
	/**
	 * Gives the player their xp bottle, changes their xp, and plays a sound effect
	 * @param player
	 * @param xp
	 */
	private void xpHelper(Player player, int xp) {
		player.getInventory().addItem( MainListener.makeBottle(xp) );
		Experience.changeExp(player, -xp);
		player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.5f);
	}
}
