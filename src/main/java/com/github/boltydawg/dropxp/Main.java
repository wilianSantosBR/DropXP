package com.github.boltydawg.dropxp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	public static FileConfiguration config;
	@Override
	public void onEnable() {
		this.getCommand("dropxp").setExecutor(new CommandDropXP());
		getServer().getPluginManager().registerEvents(new ThiccListener(), this);
		
		config = this.getConfig();
		config.addDefault("requireThickPotion", false);
		config.options().copyDefaults(true);
		saveConfig();
	}
	@Override
	public void onDisable() {
		
	}
}
