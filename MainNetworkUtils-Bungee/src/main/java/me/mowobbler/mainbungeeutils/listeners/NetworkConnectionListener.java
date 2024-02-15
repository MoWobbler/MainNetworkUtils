package me.mowobbler.mainbungeeutils.listeners;

import me.mowobbler.mainbungeeutils.discord.DiscordBot;
import me.mowobbler.mainbungeeutils.utils.BroadcastUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.awt.Color;

public class NetworkConnectionListener implements Listener {

    ProxyServer proxy;
    DiscordBot discordBot;
    Configuration configuration;
    BroadcastUtils broadcastUtils;

    public NetworkConnectionListener(DiscordBot discordBot, Configuration configuration, BroadcastUtils broadcastUtils) {
        this.proxy = ProxyServer.getInstance();
        this.discordBot = discordBot;
        this.configuration = configuration;
        this.broadcastUtils = broadcastUtils;
    }

    @EventHandler
    public void onLogin(ServerConnectEvent event) {
        if (configuration.getBoolean("maintenanceMode") && !event.getPlayer().hasPermission("proxyutils.maintenance")) {
            event.getPlayer().disconnect(new ComponentBuilder()
                    .append("[").color(ChatColor.AQUA)
                    .append("MainBaseNetwork").color(ChatColor.of(new Color(58, 160, 153)))
                    .append("]").color(ChatColor.AQUA)
                    .append(" We are currently in maintenance mode. Please try rejoining soon")
                    .color(ChatColor.of(new Color(179, 255, 250)))
                    .create());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String message = player.getName() + " joined the game";
        TextComponent text = new TextComponent(ChatColor.YELLOW + message);
        proxy.broadcast(text);
        discordBot.sendEmbedToDiscord(message, new Color(0, 204, 0), player.getUniqueId(), player.getName());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String message = player.getName() + " left the game";
        TextComponent text = new TextComponent(ChatColor.YELLOW + message);
        proxy.broadcast(text);
        discordBot.sendEmbedToDiscord(message, new Color(204, 0, 0), player.getUniqueId(), player.getName());
    }
}
