package me.mowobbler.mainbungeeutils.listeners;

import me.mowobbler.mainbungeeutils.discord.DiscordBot;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.awt.Color;

public class ServerSwitchListener implements Listener {
    ProxyServer proxy;
    DiscordBot discordBot;

    public ServerSwitchListener(ProxyServer instance, DiscordBot discordBot) {
        this.proxy = instance;
        this.discordBot = discordBot;
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {

        if (event.getFrom() == null) return;

        ProxiedPlayer player = event.getPlayer();
        String serverName = player.getServer().getInfo().getName();

        if (serverName.equals("mainbasesmp")) { serverName = "MainBaseSMP"; }
        if (serverName.equals("mainbasebuild")) { serverName = "MainBaseBuild"; }
        if (serverName.equals("mainbaseevents")) { serverName = "MainBaseEvents"; }

        String message = player.getName() + " joined " + serverName;
        discordBot.sendEmbedToDiscord(message, new Color(71, 198, 189));
        proxy.broadcast(new TextComponent(ChatColor.GRAY + message));
    }
}
