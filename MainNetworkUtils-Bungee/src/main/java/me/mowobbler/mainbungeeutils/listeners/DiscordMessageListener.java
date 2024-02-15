package me.mowobbler.mainbungeeutils.listeners;

import me.mowobbler.mainbungeeutils.discord.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.Collection;

public class DiscordMessageListener extends ListenerAdapter {
    DiscordBot bot;
    ProxyServer instance;


    public DiscordMessageListener(DiscordBot bot, ProxyServer instance) {
        this.bot = bot;
        this.instance = instance;
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getChannel().getId().equals(bot.getMainChannel().getId())) return;
        if (event.getAuthor().isBot()) return;
        if (event.getMessage().getContentStripped().length() <= 0) return;

        BaseComponent[] message = new ComponentBuilder()
                .append("[")
                .append("Discord").color(ChatColor.of(new Color(86, 98, 246)))
                .append("] <").color(ChatColor.WHITE)
                .append(event.getAuthor().getEffectiveName())
                .append("> ")
                .append(event.getMessage().getContentStripped())
                .create();


        bot.sendDiscordMessageToMinecraft(message);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (!event.getName().equalsIgnoreCase("playerlist")) return;

        if (!event.getChannel().getId().equals(bot.getMainChannel().getId())) {

            Color color = new Color(145, 49, 42);
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("You must be in the " + bot.getMainChannel().getName() +
                            " channel to use this command")
                    .setColor(color)
                    .build();

            event.replyEmbeds(embed).setEphemeral(true).queue();
            return;
        }

        Collection<ProxiedPlayer> players = instance.getPlayers();

        StringBuilder description = new StringBuilder();
        for (ProxiedPlayer player : players) {
            description.append(player.getName()).append("\n");
        }

        Color color = new Color(77, 211, 202);
        MessageEmbed playerList = new EmbedBuilder()
                .setTitle("Players Online: " + players.size())
                .setDescription(description)
                .setColor(color)
                .build();
        event.replyEmbeds(playerList).queue();


    }
}
