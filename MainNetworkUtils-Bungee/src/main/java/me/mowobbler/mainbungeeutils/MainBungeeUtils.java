package me.mowobbler.mainbungeeutils;

import me.mowobbler.mainbungeeutils.commands.Event;
import me.mowobbler.mainbungeeutils.commands.*;
import me.mowobbler.mainbungeeutils.discord.DiscordBot;
import me.mowobbler.mainbungeeutils.listeners.*;
import me.mowobbler.mainbungeeutils.managers.MessageManager;
import me.mowobbler.mainbungeeutils.managers.RestartManager;
import me.mowobbler.mainbungeeutils.socket.SocketHandler;
import me.mowobbler.mainbungeeutils.utils.BroadcastUtils;
import me.mowobbler.mainbungeeutils.utils.ConfigUtils;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.awt.Color;

public final class MainBungeeUtils extends Plugin implements Listener {

    DiscordBot discordBot;
    ConfigUtils configUtils;
    BroadcastUtils broadcastUtils;

    @Override
    public void onEnable() {

        this.broadcastUtils = new BroadcastUtils(getProxy());
        getProxy().registerChannel("BungeeCord");
        this.configUtils = new ConfigUtils(this);
        discordBot = new DiscordBot(
                configUtils.getConfiguration().getString("discordBotToken"),
                configUtils.getConfiguration().getString("discordChannelID"));

        getProxy().getPluginManager().registerCommand(this, new Survival());
        getProxy().getPluginManager().registerCommand(this, new Build());
        getProxy().getPluginManager().registerCommand(this, new Event());
        getProxy().getPluginManager().registerCommand(this, new Announce(discordBot));
        MessageManager messageManager = new MessageManager();
        getProxy().getPluginManager().registerCommand(this, new Message(messageManager));
        getProxy().getPluginManager().registerCommand(this, new Reply(messageManager));
        getProxy().getPluginManager().registerCommand(this, new GlobalWhitelist());
        getProxy().getPluginManager().registerCommand(this, new Maintenance(configUtils));
        RestartManager restartManager = new RestartManager(this, broadcastUtils, discordBot);
        getProxy().getPluginManager().registerCommand(this, new RequestRestart(restartManager));
        getProxy().getPluginManager().registerCommand(this, new CancelRestart(restartManager));

        getProxy().getPluginManager().registerListener(this, new PluginChannelListener(getProxy(), discordBot, restartManager));
        getProxy().getPluginManager().registerListener(this, new ProxyPingListener(getProxy()));
        getProxy().getPluginManager().registerListener(this, new NetworkConnectionListener(discordBot, configUtils.getConfiguration(), broadcastUtils));
        getProxy().getPluginManager().registerListener(this, new ServerSwitchListener(getProxy(), discordBot));
        getProxy().getPluginManager().registerListener(this, new MinecraftChatListener(getProxy(), discordBot));


        SocketHandler socketHandler = new SocketHandler(this, restartManager);
        new Thread(socketHandler::startSocketListener).start();
    }

    @Override
    public void onDisable() {
        discordBot.sendEmbedToDiscord("Server has been stopped", new Color(71, 198, 189));
        getProxy().unregisterChannel("BungeeCord");
    }




}
