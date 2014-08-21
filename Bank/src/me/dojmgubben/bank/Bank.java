package me.dojmgubben.bank;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Bank extends JavaPlugin {

	private static Bank main;
	public Economy economy = null;
	private final Logger log = Logger.getLogger("Minecraft");

	public ArrayList<String> transit = new ArrayList<String>();

	File configurationConfig;
	public FileConfiguration config;
	File bankConfig;
	public FileConfiguration bconfig;

	String prefix = "";

	public static Bank getMain() {
		return main;
	}

	public void onEnable() {
		setupEconomy();
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
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

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		economy = rsp.getProvider();
		return economy != null;
	}

	public void loadConfig() {

		config.addDefault("bank.gold.item", "WOOL");
		config.addDefault("bank.gold.name", "&6Gold");
		config.addDefault("messages.noperm", "&4You do not have permission to do this.");
		config.addDefault("prefix", "&0[&6&lBank&0]&r ");
		config.addDefault("bank.defaultTier", 1);
		config.addDefault("messages.goldtransit", "&cType &bDeposit 'amount' &c or &bWithdraw 'amount'.");
		config.addDefault("messages.withdraw", "&bYou withdrew %amount% from your bank account.");
		config.addDefault("messages.deposit", "&bYou deposited %amount% to your bank account.");
		bconfig.options().copyDefaults(true);
		config.options().copyDefaults(true);
		savec();
		saveb();
	}

	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
