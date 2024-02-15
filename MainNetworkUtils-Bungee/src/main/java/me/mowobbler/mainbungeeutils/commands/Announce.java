package me.mowobbler.mainbungeeutils.commands;

import me.mowobbler.mainbungeeutils.discord.DiscordBot;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.awt.Color;

public class Announce extends Command {

    ProxyServer proxy;
    ChatColor primaryColor;
    ChatColor secondaryColor;
    DiscordBot bot;

    public Announce(DiscordBot bot) {
        super("announce", "proxyutils.announce");
        this.proxy = ProxyServer.getInstance();
        this.primaryColor = ChatColor.of(new Color(58, 160, 153));
        this.secondaryColor = ChatColor.of(new Color(179, 255, 250));
        this.bot = bot;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String message = String.join(" ", args);
        proxy.broadcast(new ComponentBuilder()
                .append("[").color(ChatColor.AQUA)
                .append("MainBaseNetwork").color(primaryColor)
                .append("] ").color(ChatColor.AQUA)
                .append(message).color(secondaryColor)
                .create());
        bot.sendEmbedToDiscord("Main Announcement", message, primaryColor.getColor());
    }
}
