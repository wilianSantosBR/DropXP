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
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ThiccListener implements Listener{
	//private NamespacedKey nameKey = new NamespacedKey(Main.instance,"dropxp");
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		if(Main.thicc) {
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if(event.getItem()!=null && event.getItem().getType().equals(Material.POTION)) {
					PotionMeta pot = (PotionMeta)event.getItem().getItemMeta();
					if(pot.getBasePotionData().getType().equals(PotionType.THICK)) {
						Player player = event.getPlayer();
						if(!player.hasPermission("dropxp.drop")) {
							return;
						}
						int xp = Experience.getExp(player);
						if(xp>0) {
							Experience.changeExp(player, -xp);
							ItemStack bottle = makeBottle(xp,player.getDisplayName());
							if(event.getHand().equals(EquipmentSlot.HAND))
								player.getInventory().setItemInMainHand(bottle);
							else
								player.getInventory().setItemInOffHand(bottle);
							event.setCancelled(true);
							player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.5f);
							return;
						}
						TextComponent msg = new TextComponent();
						msg.setText("You need some xp!");
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
			if(lore!=null && lore.size()>0 && lore.get(0).contains(" orbs")) {
			//if(event.getItem().getItemMeta().getPersistentDataContainer().has(nameKey, PersistentDataType.STRING)) {
				Player player = event.getPlayer();
				if(!player.hasPermission("dropxp.drink")) {
					player.sendMessage(ChatColor.RED+"You do not have permission to drink this potion");
					event.setCancelled(true);
					return;
				}
				if(!Main.thicc) {
					event.setCancelled(true);
					player.getInventory().removeItem(event.getItem());
				}
				String xp = ChatColor.stripColor(lore.get(0));
				Experience.changeExp(player, Integer.parseInt(xp.substring(0, xp.indexOf(' '))));
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.5f);
			}
		}
	}
	@EventHandler
	public void brewEvent(BrewEvent event) {
		if(Main.thicc && event.getContents().getIngredient().getType().equals(Material.GLOWSTONE_DUST)) {
			BrewerInventory bi = event.getContents();
			//I have to use a runner here so that I can modify the new potions produced in the brewing stand rather than the original ingredients
			new BukkitRunnable() {
				@Override
				public void run() {
					for(int i=0; i<3; i++) {
						ItemStack pot = bi.getItem(i);
						if(pot.getType().equals(Material.POTION)) {
							PotionMeta met = (PotionMeta)pot.getItemMeta();
							if(met.getBasePotionData().getType().equals(PotionType.THICK)) {
								met.setDisplayName(ChatColor.YELLOW + "Empty XP Potion");
								met.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
								met.setColor(Color.fromRGB(255,250,171));
								pot.setItemMeta(met);
								bi.setItem(i, pot);
							}
						}
					}
				}
			}.runTaskLater(Main.instance, 1L);
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
}
