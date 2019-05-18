package com.github.boltydawg.dropxp;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

/**
 * This listener only gets activated if the user has
 * "requireThickPotion" set to true in the config file
 * @author Jason
 *
 */
public class ThiccListener implements Listener{
	/**
	 * Runs when a player right or left clicks, but I'm only interested in when they right click while holding a thick potion
	 * @param event
	 */
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
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
						ItemStack bottle = MainListener.makeBottle(xp,player.getDisplayName());
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
	/**
	 * Whenever a thick potion is brewed, it will be replaced with an empty xp potion
	 * @param event
	 */
	@EventHandler
	public void brewEvent(BrewEvent event) {
		if(event.getContents().getIngredient().getType().equals(Material.GLOWSTONE_DUST)) {
			BrewerInventory bi = event.getContents();
			
			/**
			 * I have to use a runner here so that I can modify the new potions produced in the brewing stand rather than the original ingredients
			 * It's a bit finicky but I couldn't think of any other way around it, the API doesn't have any methods for doing this
			 * This BukkitRunnable runs the code 1 tick after the event is triggered, that way the BrewerInventory updates
			 */
			
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
}
