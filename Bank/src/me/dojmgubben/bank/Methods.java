package me.dojmgubben.bank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

public class Methods {

	private Methods() {

	}

	private static Methods instance = new Methods();
	private static Messages msg = Messages.getInstance();

	private static Bank plugin = Bank.getMain();

	public static Methods getInstance() {
		return instance;
	}

	public void enterBank(Player player, int i) {
		int size = 9 * i;
		Inventory inv = Bukkit.getServer().createInventory(player, size, player.getName() + "'s Bank");
		ItemStack gold = new ItemStack(Material.getMaterial(plugin.config.getString("bank.gold.item")));
		if (gold.getType() == Material.WOOL) {
			Wool wool = new Wool(DyeColor.YELLOW);
			gold = wool.toItemStack();
		}
		ItemMeta gm = gold.getItemMeta();
		gm.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("bank.gold.name")));
		gold.setItemMeta(gm);
		inv.setItem(8, gold);
		player.openInventory(inv);
	}
}
