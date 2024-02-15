package me.mowobbler.mainbungeeutils.listeners;

import me.mowobbler.mainbungeeutils.discord.DiscordBot;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MinecraftChatListener implements Listener {

    ProxyServer proxy;
    DiscordBot discordBot;

    public MinecraftChatListener(ProxyServer instance, DiscordBot discordBot) {
        this.proxy = instance;
        this.discordBot = discordBot;
    }

    @EventHandler
    public void onChatEvent(ChatEvent event) {
        if (event.getMessage().startsWith("/")) return;
        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        String messageContent = discordBot.convertMentionsFromNames(event.getMessage());
        String message = "<" + event.getSender() + "> " + messageContent;
        discordBot.sendMessageToDiscord(message);
        String serverName = ((ProxiedPlayer) event.getSender()).getServer().getInfo().getName();
        proxy.getPlayers().stream()
                .filter(player -> !player.getServer().getInfo().getName().equals(serverName))
                .forEach(player -> player.sendMessage(new TextComponent(message)));
    }
}
