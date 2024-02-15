package me.mowobbler.mainbungeeutils.utils;

import me.mowobbler.mainbungeeutils.MainBungeeUtils;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigUtils {

    MainBungeeUtils plugin;
    Configuration configuration;

    public ConfigUtils(MainBungeeUtils plugin) {
        this.plugin = plugin;
        initConfig();
    }

    private void initConfig() {
        try {
            makeConfig();
        } catch (IOException e) {
            plugin.getLogger().info("Error creating config: " + e);
        }

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfiguration() {
        return  configuration;
    }

    private void makeConfig() throws IOException {
        // Create plugin config folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getLogger().info("Created config folder: " + plugin.getDataFolder().mkdir());
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");

        // Copy default config if it doesn't exist
        if (!configFile.exists()) {
            FileOutputStream outputStream = new FileOutputStream(configFile);
            InputStream in = plugin.getResourceAsStream("config.yml");
            in.transferTo(outputStream);
        }
    }

}
