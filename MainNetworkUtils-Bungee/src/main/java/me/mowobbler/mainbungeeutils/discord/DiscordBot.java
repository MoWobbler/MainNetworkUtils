package me.mowobbler.mainbungeeutils.discord;

import me.mowobbler.mainbungeeutils.listeners.DiscordMessageListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.Color;
import java.time.temporal.TemporalAccessor;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordBot extends ListenerAdapter {

    ProxyServer instance;
    JDA jda;
    MessageChannel mainChannel;
    Guild guild;

    public DiscordBot(String token, String mainChannelID){
        this.instance = ProxyServer.getInstance();
        jda = JDABuilder.create(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .setToken(token)
                .setActivity(Activity.playing("MainBaseSMP"))
                .addEventListeners(new DiscordMessageListener(this, instance))
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOJI,
                        CacheFlag.STICKER, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS,
                        CacheFlag.SCHEDULED_EVENTS)
                .build();
        try {
            jda.awaitReady();
            mainChannel = (MessageChannel) jda.getGuildChannelById(mainChannelID);
            if (mainChannel == null) {
                ProxyServer.getInstance().getLogger().warning("Invalid discord token provided, shutting down");
                jda.shutdown();
                return;
            }
            guild = ((GuildChannel) mainChannel).getGuild();
            updateCommands();
            sendEmbedToDiscord("Server has started!", new Color(71, 198, 189));
            instance.getLogger().info("Discord bot successfully started");
        } catch (InterruptedException exception) {
            instance.getLogger().warning("Error occured in starting discord bot");
        }
    }


    public void sendDiscordMessageToMinecraft(BaseComponent[] message) {
        instance.broadcast(new TextComponent(message));
    }


    public void sendMessageToDiscord(String message) {
        if (message.length() >= 256) return;
        MessageCreateData data = new MessageCreateBuilder()
                .setContent(message)
                .build();
        mainChannel.sendMessage(data).queue();
    }


    public void sendEmbedToDiscord(String title, Color color) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(title)
                .setColor(color)
                .build();

        mainChannel.sendMessageEmbeds(embed).queue();
    }


    public void sendEmbedToDiscord(String title, String description, Color color) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(color)
                .build();

        mainChannel.sendMessageEmbeds(embed).queue();
    }


    public void sendEmbedToDiscord(String title, Color color, TemporalAccessor timeStamp) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(title)
                .setColor(color)
                .setTimestamp(timeStamp)
                .build();

        mainChannel.sendMessageEmbeds(embed).queue();
    }


    public void sendEmbedToDiscord(String title, Color color, UUID UUID, String username) {
        String avatarUrl = "https://heads.discordsrv.com/head.png?uuid=" + UUID.toString() + "&name=" + username + "&overlay";
        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(title, avatarUrl, avatarUrl)
                .setColor(color)
                .build();
        mainChannel.sendMessageEmbeds(embed).queue();
    }


    public void updateCommands() {
        guild.updateCommands().addCommands(
                Commands.slash("playerlist", "Lists online players")
        ).queue();
    }


    public MessageChannel getMainChannel() {
        return mainChannel;
    }


    public String convertMentionsFromNames(String message) {
        Pattern mentionPattern = Pattern.compile("@\\w+");
        Matcher matcher = mentionPattern.matcher(message);

        while (matcher.find()) {
            String mentionText = matcher.group();
            String mentionName = mentionText.substring(1);

            for (Member member : guild.getMembers()) {
                if (member.getEffectiveName().equalsIgnoreCase(mentionName)) {
                    message = message.replace(mentionText, member.getAsMention());
                    break;
                }
            }
        }
        return message;
    }
}
