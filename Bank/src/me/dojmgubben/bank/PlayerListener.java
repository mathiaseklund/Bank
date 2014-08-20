package me.dojmgubben.bank;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
}
