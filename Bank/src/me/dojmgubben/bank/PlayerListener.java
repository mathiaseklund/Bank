package me.dojmgubben.bank;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {

	Bank plugin = Bank.getMain();
	Messages msg = Messages.getInstance();
	Methods methods = Methods.getInstance();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.bconfig.get(player.getUniqueId().toString() + ".size") == null) {
			int size = plugin.config.getInt("bank.defaultTier");
			plugin.bconfig.set(player.getUniqueId().toString() + ".size", size);
			plugin.bconfig.set(player.getUniqueId().toString() + ".gold", 0);
			plugin.saveb();
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType() == Material.ENDER_CHEST) {
				event.setCancelled(true);
				if (player.hasPermission("bank.use")) {
					int size = plugin.bconfig.getInt(player.getUniqueId().toString() + ".size");
					if (!player.isSneaking()) {
						methods.enterBank(player, size);
					} else {
						// Upgrade
					}
				} else {
					msg.noperm(player);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryclick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getInventory().getTitle().equalsIgnoreCase(player.getName() + "'s Bank")) {
			event.setCancelled(true);
			ItemStack item = event.getCurrentItem();
			ItemMeta im = item.getItemMeta();
			int slot = event.getRawSlot();
			if (slot < event.getInventory().getSize()) {
				slot = slot + 1;
				if (item.hasItemMeta()) {
					if (im.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("bank.gold.name")))) {
						methods.goldTransit(player);
					} else {
						plugin.bconfig.set(player.getUniqueId().toString() + ".slot." + slot, null);
						plugin.saveb();
						event.getInventory().remove(item);
						player.getInventory().addItem(item);
						msg.msg(player, "taken from bank");

					}
				} else {
					plugin.bconfig.set(player.getUniqueId().toString() + ".slot." + slot, null);
					plugin.saveb();
					event.getInventory().remove(item);
					player.getInventory().addItem(item);
					msg.msg(player, "taken from bank");
				}
			} else {
				for (int i = 0; i < event.getInventory().getSize(); i++) {
					slot = i + 1;
					if (plugin.bconfig.getItemStack(player.getUniqueId().toString() + ".slot." + slot) == null) {
						plugin.bconfig.set(player.getUniqueId().toString() + ".slot." + slot, item);
						plugin.saveb();
						msg.msg(player, "added to bank");
						player.getInventory().remove(item);
						methods.enterBank(player, plugin.bconfig.getInt(player.getUniqueId().toString() + ".size"));
						break;
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (plugin.transit.contains(player.getName())) {
			String message = event.getMessage().toLowerCase();
			if (message.split(" ")[0].contains("withdraw")) {
				event.setCancelled(true);
				if (plugin.isInteger(message.split(" ")[1])) {
					int am = Integer.parseInt(message.split(" ")[1]);
					int m = plugin.bconfig.getInt(player.getUniqueId() + ".gold");
					if (m >= am) {
						EconomyResponse r = plugin.economy.depositPlayer(player, am);
						if (r.transactionSuccess()) {
							msg.msg(player, plugin.config.getString("messages.withdraw").replace("%amount%", "" + am));
							m = m - am;
							plugin.bconfig.set(player.getUniqueId().toString() + ".gold", m);
							plugin.saveb();
							plugin.transit.remove(player.getName());
						}
					}
				}
			} else if (message.split(" ")[0].contains("deposit")) {
				event.setCancelled(true);
				if (plugin.isInteger(message.split(" ")[1])) {
					int am = Integer.parseInt(message.split(" ")[1]);
					int m = plugin.bconfig.getInt(player.getUniqueId() + ".gold");

					EconomyResponse r = plugin.economy.withdrawPlayer(player, am);
					if (r.transactionSuccess()) {
						msg.msg(player, plugin.config.getString("messages.deposit").replace("%amount%", "" + am));
						m = m + am;
						plugin.bconfig.set(player.getUniqueId().toString() + ".gold", m);
						plugin.saveb();
						plugin.transit.remove(player.getName());
						event.setCancelled(true);
					}
				}

			}
		}
	}
}
