package me.mowobbler.mainbungeeutils.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener implements Listener {

    private final ProxyServer proxy;

    public ProxyPingListener(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @EventHandler
    public void onProxyPing(final ProxyPingEvent event) {
        final ServerPing response = event.getResponse();
        final ServerPing.Players players = response.getPlayers();
        final ServerPing.PlayerInfo[] playerHoverInfo = proxy.getPlayers().stream()
                .map(player -> new ServerPing.PlayerInfo(player.getName(), player.getUniqueId()))
                .toArray(ServerPing.PlayerInfo[]::new);
        players.setSample(playerHoverInfo);
        players.setOnline(proxy.getPlayers().size());
        event.setResponse(response);
    }
}
