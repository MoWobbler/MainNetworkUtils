package me.mowobbler.mainbungeeutils.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BroadcastUtils {

    ProxyServer proxy;

    public BroadcastUtils(ProxyServer proxy) {
        this.proxy = proxy;
    }


    public void sendHighlightedBroadcast(String serverName, BaseComponent[] highlightedMessage, BaseComponent[] dimMessage) {
        for (ProxiedPlayer player : proxy.getPlayers()) {
            if (player.getServer() != null && player.getServer().getInfo().getName().equalsIgnoreCase(serverName)) {
                player.sendMessage(new TextComponent(highlightedMessage));
            } else {
                player.sendMessage(new TextComponent(dimMessage));
            }
        }
    }

    public String getFormattedServerName(String serverName) {
        switch (serverName) {
            case "mainbasesmp":
                return "MainBaseSMP";
            case "mainbasebuild":
                return "MainBaseBuild";
            case "mainbaseevents":
                return "MainBaseEvents";
            default:
                return serverName;
        }
    }
}
