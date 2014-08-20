package me.dojmgubben.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {

	private Messages() {
	}

	private static Bank plugin = Bank.getMain();
	private static Messages instance = new Messages();

	public static Messages getInstance() {
		return instance;
	}

	public void onlyplayer(CommandSender sender) {
		cmsg(sender, plugin.config.getString("messages.onlyplayer"));
	}

	public void noperm(Player player) {
		msg(player, plugin.config.getString("messages.noperm"));
	}

	public void msg(Player player, String msg) {
		String prefix = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("prefix"));
		player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void cmsg(CommandSender sender, String msg) {
		String prefix = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("prefix"));
		sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', msg));
	}
}
