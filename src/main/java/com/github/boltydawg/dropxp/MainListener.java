package com.github.boltydawg.dropxp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import net.md_5.bungee.api.ChatColor;
/**
 * This listener handles all events that are independent of
 * the user's configuration
 * @author Jason
 *
 */
public class MainListener implements Listener {
	@EventHandler
	public void drinkPotion(PlayerItemConsumeEvent event) {
		if(event.getItem().getType().equals(Material.POTION)) {
			List<String> lore = event.getItem().getItemMeta().getLore();
			if(lore!=null && lore.size()>0) {
				if(lore.get(0).contains(" orbs")) {
					Player player = event.getPlayer();
					String xp = ChatColor.stripColor(lore.get(0));
					int orbs;
					try {
						orbs = Integer.parseInt(xp.substring(0, xp.indexOf(' ')));
					}
					catch(NumberFormatException e) {
						return;
					}
					if(!player.hasPermission("dropxp.drink")) {
						player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.RED+ "You do not have permission to drink this potion");
						event.setCancelled(true);
						return;
					}
					Experience.changeExp(player, orbs);
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.5f);
					if(!Main.thicc) {
						event.setCancelled(true);
						if(player.getInventory().getItemInMainHand().equals(event.getItem()))
							player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
						else
							player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
					}
				}
				else if(lore.get(0).contains(" levels")) {
					Player player = event.getPlayer();
					String xp = ChatColor.stripColor(lore.get(0));
					int levels;
					try {
						levels = Integer.parseInt(xp.substring(0, xp.indexOf(' ')));
					}
					catch(NumberFormatException e) {
						return;
					}
					if(!player.hasPermission("dropxp.drink")) {
						player.sendMessage(ChatColor.DARK_AQUA + "[DropXP] " + ChatColor.RED + "You do not have permission to drink this potion");
						event.setCancelled(true);
						return;
					}
					player.setLevel(player.getLevel()+levels);
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.25f);
					if(!Main.thicc) {
						event.setCancelled(true);
						if(player.getInventory().getItemInMainHand().equals(event.getItem()))
							player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
						else
							player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
					}
				}
				
			}
		}
	}
	public static ItemStack makeBottle(int xp, String name) {
		ItemStack bottle = new ItemStack(Material.POTION);
		PotionMeta met = (PotionMeta)bottle.getItemMeta();
		//met.getPersistentDataContainer().set(nameKey, PersistentDataType.STRING, "xp");
		ArrayList<String> lore = new ArrayList<String>(2);
		lore.add(ChatColor.YELLOW.toString()+xp+ChatColor.YELLOW+" orbs");
		DecimalFormat df = new DecimalFormat("0.0");
		lore.add(ChatColor.GRAY.toString() + "0-"+ df.format(Experience.getLevelFromExp((xp))));
		met.setLore(lore);
		met.setColor(Color.fromRGB(0,208,35));
		met.setDisplayName(ChatColor.GREEN+name+ChatColor.GREEN+"'s XP");
		met.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		bottle.setItemMeta(met);
		return bottle;
	}
	public static ItemStack makeLevelBottle(int xp) {
		ItemStack bottle = new ItemStack(Material.POTION);
		PotionMeta met = (PotionMeta)bottle.getItemMeta();
		//met.getPersistentDataContainer().set(nameKey, PersistentDataType.STRING, "xp");
		ArrayList<String> lore = new ArrayList<String>(1);
		lore.add(ChatColor.YELLOW.toString()+xp+ChatColor.YELLOW+" levels");
		met.setLore(lore);
		met.setColor(Color.fromRGB(0,208,35));
		met.setDisplayName(ChatColor.GREEN+"Leveled XP Bottle");
		met.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		bottle.setItemMeta(met);
		return bottle;
	}
}
