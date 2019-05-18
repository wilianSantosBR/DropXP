package com.github.boltydawg.dropxp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A plugin that allows you to drop and trade your hard-earned xp
 * 
 * @author BoltyDawg
 */

//TODO colorize xp potions based off of how many orbs they have

public class Main extends JavaPlugin{
	public static FileConfiguration config;
	public static Plugin instance;
	public static boolean thicc;
	@Override
	public void onEnable() {
		instance = this;
		
		config = this.getConfig();
		config.addDefault("requireThickPotion", false);
		config.options().copyDefaults(true);
		saveConfig();
		
		thicc = config.getBoolean("requireThickPotion");
		
		this.getCommand("dropxp").setTabCompleter(new XPTabCompleter());
		this.getCommand("dropxp").setExecutor(new CommandDropXP());
		if(thicc)
			getServer().getPluginManager().registerEvents(new ThiccListener(), this);
		getServer().getPluginManager().registerEvents(new MainListener(), this);
	}
	@Override
	public void onDisable() {
		
	}
}
