package com.github.boltydawg.dropxp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ThiccListener implements Listener{
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		if(Main.config.getBoolean("requireThickPotion")) {
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if(event.getItem()!=null && event.getItem().getType().equals(Material.POTION)) {
					PotionMeta pot = (PotionMeta)event.getItem().getItemMeta();
					if(pot.getBasePotionData().getType().equals(PotionType.THICK)) {
						Player player = event.getPlayer();
						if(!player.hasPermission("dropxp.drop")) {
							return;
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
							Experience.changeExp(player, -xp);
							if(event.getHand().equals(EquipmentSlot.HAND))
								player.getInventory().setItemInMainHand(bottle);
							else
								player.getInventory().setItemInOffHand(bottle);
							event.setCancelled(true);
							player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.5f);
							return;
						}
						TextComponent msg = new TextComponent();
						msg.setText("You need over 1 level of xp!");
						msg.setColor(ChatColor.RED);
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
					}
				}
			}
		}
	}
	@EventHandler
	public void drinkPotion(PlayerItemConsumeEvent event) {
		if(event.getItem().getType().equals(Material.POTION)) {
			List<String> lore = event.getItem().getItemMeta().getLore();
			if(lore!=null && lore.size()==1 && lore.get(0).contains(" orbs")) {
				Player player = event.getPlayer();
				if(!player.hasPermission("dropxp.drink")) {
					player.sendMessage(ChatColor.RED+"You do not have permission to drink this potion");
					event.setCancelled(true);
					return;
				}
				if(!Main.config.getBoolean("requireThickPotion")) {
					event.setCancelled(true);
					player.getInventory().removeItem(event.getItem());
				}
				String xp = ChatColor.stripColor(lore.get(0));
				Experience.changeExp(player, Integer.parseInt(xp.substring(0, xp.indexOf(' '))));
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.5f);
			}
		}
	}
}
