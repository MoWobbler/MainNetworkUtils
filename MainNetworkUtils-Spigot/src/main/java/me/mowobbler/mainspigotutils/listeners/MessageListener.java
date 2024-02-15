package me.mowobbler.mainspigotutils.listeners;

import me.mowobbler.mainspigotutils.Utils.BungeeUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class MessageListener implements Listener {

    BungeeUtils bungeeUtils;

    public MessageListener(BungeeUtils bungeeUtils) {
        this.bungeeUtils = bungeeUtils;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }

    @EventHandler()
    public void onDeath(PlayerDeathEvent event) {
        bungeeUtils.sendPluginMessage(event.getEntity(), "death", event.getDeathMessage());
    }

    @EventHandler()
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        if (event.getAdvancement().getDisplay() == null) return;
        String advancementMessage = ChatColor.GRAY + event.getPlayer().getName() + " has made the advancement "
                + ChatColor.DARK_GRAY + "[" + event.getAdvancement().getDisplay().getTitle()
                + "]";

        bungeeUtils.sendPluginMessage(event.getPlayer(), "advancement", advancementMessage);
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        bungeeUtils.sendMessage("serverstarted");
    }
}
