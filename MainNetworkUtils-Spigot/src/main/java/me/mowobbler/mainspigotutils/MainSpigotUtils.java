package me.mowobbler.mainspigotutils;

import me.mowobbler.mainspigotutils.Utils.BungeeUtils;
import me.mowobbler.mainspigotutils.commands.Coords;
import me.mowobbler.mainspigotutils.commands.Rules;
import me.mowobbler.mainspigotutils.commands.Uptime;
import me.mowobbler.mainspigotutils.listeners.MessageListener;
import me.mowobbler.mainspigotutils.listeners.PluginMessageChannel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainSpigotUtils extends JavaPlugin {

    public static JavaPlugin instance;
    FileConfiguration config;

    @Override
    public void onEnable() {

        instance = this;
        this.saveDefaultConfig();
        this.config = getConfig();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageChannel(config.get("ServerIdentifier").toString()));

        getCommand("coords").setExecutor(new Coords());
        getCommand("uptime").setExecutor(new Uptime());
        getCommand("rules").setExecutor(new Rules());

        BungeeUtils bungeeUtils = new BungeeUtils(config.get("IPAddress").toString(), Integer.parseInt(config.get("Port").toString()), config.get("ServerIdentifier").toString());
        getServer().getPluginManager().registerEvents(new MessageListener(bungeeUtils), this);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }
}
