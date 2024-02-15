package me.mowobbler.mainbungeeutils.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;

public class PluginMessagingUtils {

    public static void sendPluginMessageToBukkit(String subchannel) {
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();

        if (networkPlayers == null || networkPlayers.isEmpty()) return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);

        for (String serverName : ProxyServer.getInstance().getServersCopy().keySet()) {
            ProxyServer.getInstance().getServerInfo(serverName).sendData("BungeeCord", out.toByteArray());
        }

    }

    public static void sendPluginMessageToBukkit(String subchannel, String data, ProxiedPlayer player) {
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();

        if ( networkPlayers == null || networkPlayers.isEmpty()) return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        out.writeUTF(data);

        player.getServer().sendData("BungeeCord", out.toByteArray());
    }

    public static void sendPluginMessageToBukkit(String subchannel, String data, String secondData) {
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();

        if ( networkPlayers == null || networkPlayers.isEmpty()) return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        out.writeUTF(data);
        out.writeUTF(secondData);

        for (String serverName : ProxyServer.getInstance().getServersCopy().keySet()) {
            System.out.println("SENDIN");
            ProxyServer.getInstance().getServerInfo(serverName).sendData("BungeeCord", out.toByteArray());
        }
    }

}
