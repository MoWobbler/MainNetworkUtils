package me.mowobbler.mainbungeeutils.commands;

import me.mowobbler.mainbungeeutils.utils.ConfigUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.awt.Color;


public class Maintenance extends Command {

    ConfigUtils configUtils;

    public Maintenance(ConfigUtils configUtils) {
        super("maintenance", "proxyutils.maintenance");
        this.configUtils = configUtils;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean inMaintenanceMode = !configUtils.getConfiguration().getBoolean("maintenanceMode");
        configUtils.getConfiguration().set("maintenanceMode", inMaintenanceMode);
        configUtils.saveConfig();

        String status;
        if (inMaintenanceMode) {
            status = ChatColor.RED + "Maintenance protocol initiated";
        } else {
            status = ChatColor.GREEN + "Maintenance protocol disabled";
        }
        sender.sendMessage(new TextComponent(status));
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.hasPermission("proxyutils.maintenance")) continue;
            player.disconnect(new ComponentBuilder()
                    .append("[").color(ChatColor.AQUA)
                    .append("MainBaseNetwork").color(ChatColor.of(new Color(58, 160, 153)))
                    .append("]").color(ChatColor.AQUA)
                    .append(" We have just entered maintenance mode. Please try rejoining soon")
                    .color(ChatColor.of(new Color(179, 255, 250)))
                    .create());
        }
    }
}
