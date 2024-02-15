package me.mowobbler.mainbungeeutils.listeners;

import me.mowobbler.mainbungeeutils.discord.DiscordBot;
import me.mowobbler.mainbungeeutils.managers.RestartManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Date;

public class PluginChannelListener implements Listener {

    ProxyServer proxy;
    DiscordBot discordBot;
    RestartManager restarterUtils;

    public PluginChannelListener(ProxyServer proxy, DiscordBot discordBot, RestartManager restarterUtils) {
        this.proxy = proxy;
        this.discordBot = discordBot;
        this.restarterUtils = restarterUtils;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent ev) {

        if (!ev.getTag().equals("BungeeCord")) {
            return;
        }

        if (!(ev.getSender() instanceof Server)) {
            return;
        }

        Server server = (Server) ev.getSender();
        String serverName = server.getInfo().getName();
        ByteArrayInputStream stream = new ByteArrayInputStream(ev.getData());
        DataInputStream in = new DataInputStream(stream);
        try {
            String subChannel = in.readUTF();
            String message = in.readUTF();
            Color color;

            switch (subChannel) {
                case "death":
                    color = new Color(0, 0, 0);
                    discordBot.sendEmbedToDiscord(ChatColor.stripColor(message), color, new Date().toInstant());
                    break;
                case "broadcast":
                    color = new Color(71, 198, 189);
                    discordBot.sendEmbedToDiscord(ChatColor.stripColor(message), color);

                    break;
                case "advancement":
                    color = new Color(243, 182, 27);
                    discordBot.sendEmbedToDiscord(ChatColor.stripColor(message), color);
                    break;
                default:
                    return;
            }

            proxy.getPlayers().stream()
                    .filter(player -> !player.getServer().getInfo().getName().equals(serverName))
                    .forEach(player -> player.sendMessage(new TextComponent(message)));
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().info("Error occured for plugin message: " + e);
        }
    }
}
