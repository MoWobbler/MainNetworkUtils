package me.mowobbler.mainbungeeutils.managers;

import me.mowobbler.mainbungeeutils.MainBungeeUtils;
import me.mowobbler.mainbungeeutils.discord.DiscordBot;
import me.mowobbler.mainbungeeutils.utils.BroadcastUtils;
import me.mowobbler.mainbungeeutils.utils.PluginMessagingUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RestartManager {

    MainBungeeUtils instance;
    BroadcastUtils broadcastUtils;
    DiscordBot bot;

    ArrayList<ScheduledTask> tasks;
    HashMap<String, Long> serverLastRequestTime;

    private ScheduledTask restartTask;
    private String restartingServer;

    public RestartManager(MainBungeeUtils instance, BroadcastUtils broadcastUtils, DiscordBot bot) {
        this.instance = instance;
        this.broadcastUtils = broadcastUtils;
        this.bot = bot;
        this.tasks = new ArrayList<>();
        this.serverLastRequestTime = new HashMap<>();
        initializeTimes();
    }

    public void requestRestart(ProxiedPlayer player) {

        if (restartTask != null) {
            player.sendMessage(new TextComponent(ChatColor.RED +
                    "A restart is already in progress. Please wait until the current restart is complete."));
            return;
        }

        final String serverName = broadcastUtils.getFormattedServerName(player.getServer().getInfo().getName());
        Long lastRequestTime = serverLastRequestTime.get(serverName);
        if (lastRequestTime != null && System.currentTimeMillis() < (3 * 60 * 1000) + lastRequestTime) {
            player.sendMessage(new TextComponent(ChatColor.RED +
                    "A restart has been requested too recently for " + serverName
                    + ". Please wait a bit longer before requesting a restart."));
            return;
        }

        restartingServer = serverName;
        serverLastRequestTime.put(serverName, System.currentTimeMillis());
        String requestMessageContent = player.getName()
                + " has requested a restart. Attempting to restart "
                + serverName
                + " in 30 seconds";
        BaseComponent[] message = buildRestarterMessage(requestMessageContent,
                ChatColor.AQUA,
                ChatColor.of(new Color(58, 160, 153)),
                ChatColor.of(new Color(179, 255, 250)));
        BaseComponent[] dimMessage = buildRestarterMessage(requestMessageContent,
                ChatColor.of(new Color(131, 131, 131)),
                ChatColor.of(new Color(63, 63, 63)),
                ChatColor.of(new Color(206, 206, 206)));

        PluginMessagingUtils.sendPluginMessageToBukkit("playsound");
        broadcastUtils.sendHighlightedBroadcast(serverName, message, dimMessage);
        bot.sendEmbedToDiscord(serverName + " Announcement", requestMessageContent,
                new Color(58, 160, 153));

        restartTask = instance.getProxy().getScheduler().schedule(instance, () -> {
            restartTask = null;
            PluginMessagingUtils.sendPluginMessageToBukkit("restarter", serverName.toLowerCase(), player);
        }, 30, TimeUnit.SECONDS);

    }


    public void cancelRestart(CommandSender sender) {
        if (restartTask == null) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "There is no requested restart to cancel, chill"));
            return;
        }

        restartTask.cancel();
        restartTask = null;
        String cancelMessageContent = sender.getName()
                + " has cancelled the restart";
        BaseComponent[] message = buildRestarterMessage(cancelMessageContent,
                ChatColor.AQUA,
                ChatColor.of(new Color(58, 160, 153)),
                ChatColor.of(new Color(179, 255, 250)));
        BaseComponent[] dimMessage = buildRestarterMessage(cancelMessageContent,
                ChatColor.of(new Color(131, 131, 131)),
                ChatColor.of(new Color(63, 63, 63)),
                ChatColor.of(new Color(206, 206, 206)));

        broadcastUtils.sendHighlightedBroadcast(restartingServer, message, dimMessage);
        bot.sendEmbedToDiscord(restartingServer + " Announcement", cancelMessageContent,
                new Color(58, 160, 153));
    }


    private void initializeTimes() {
        for (String server : instance.getProxy().getServersCopy().keySet()) {
            if (!serverLastRequestTime.containsKey(server)) {
                serverLastRequestTime.put(broadcastUtils.getFormattedServerName(server), System.currentTimeMillis());
            }
        }
    }


    private BaseComponent[] buildRestarterMessage(String restarterMessageContent,
                                                  ChatColor bracketColor,
                                                  ChatColor serverNameColor,
                                                  ChatColor contentColor) {
        return new ComponentBuilder()
                .append("[").color(bracketColor)
                .append(restartingServer)
                .color(serverNameColor)
                .append("] ").color(bracketColor)
                .append(restarterMessageContent)
                .color(contentColor)
                .create();
    }


    public void setServerLastRequestTime(String serverName) {
        instance.getLogger().info(serverName + " restart time has been updated");
        serverLastRequestTime.put(broadcastUtils.getFormattedServerName(serverName), System.currentTimeMillis());
    }
}
