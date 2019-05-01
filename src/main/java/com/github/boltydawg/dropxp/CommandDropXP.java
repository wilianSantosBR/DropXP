package com.github.boltydawg.dropxp;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import java.util.ArrayList;

public class CommandDropXP implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = ((Player) sender);
		if(Main.config.getBoolean("requireThickPotion")) {
			player.sendMessage("Right click while holding a thick potion to store your xp inside of it!");
			return true;
		}
		else {
			if(player.getInventory().firstEmpty()==-1) {
				player.sendMessage("Your inventory is full!");
				return true;
			}
			int xp = Experience.getExp(player)-7;
			if(xp>0) {
				ItemStack bottle = new ItemStack(Material.POTION);
				PotionMeta met = (PotionMeta)bottle.getItemMeta();
				ArrayList<String> l = new ArrayList<String>(1);
				l.add(ChatColor.YELLOW.toString()+xp+ChatColor.YELLOW+" orbs");
				met.setLore(l);
				met.setColor(Color.GREEN);
				met.setDisplayName(ChatColor.GREEN+player.getDisplayName()+ChatColor.GREEN+"'s XP");
				met.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				bottle.setItemMeta(met);
				player.getInventory().addItem(bottle);
				Experience.changeExp(player, -xp);
				player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.5f);
				return true;
			}
			player.sendMessage("You need over 1 level of xp!");
			return true;
		}
	}
}
