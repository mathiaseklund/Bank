package me.dojmgubben.bank;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Bank extends JavaPlugin {

	private static Bank main;

	File configurationConfig;
	public FileConfiguration config;
	File bankConfig;
	public FileConfiguration bconfig;

	String prefix = "";

	public static Bank getMain() {
		return main;
	}

	public void onEnable() {
		main = this;
		configurationConfig = new File(getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configurationConfig);
		bankConfig = new File(getDataFolder(), "banks.yml");
		bconfig = YamlConfiguration.loadConfiguration(bankConfig);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		loadConfig();
	}

	public void savec() {
		try {
			config.save(configurationConfig);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveb() {
		try {
			bconfig.save(bankConfig);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadConfig() {

		config.addDefault("bank.gold.item", "WOOL");
		config.addDefault("bank.gold.name", "&6Gold");
		config.addDefault("messages.noperm", "&4You do not have permission to do this.");
		config.addDefault("prefix", "&0[&6&lBank&0]&r ");
		config.addDefault("bank.defaultTier", 1);
		bconfig.options().copyDefaults(true);
		config.options().copyDefaults(true);
		savec();
		saveb();
	}
}
