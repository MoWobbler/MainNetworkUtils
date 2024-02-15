package me.mowobbler.mainspigotutils.Utils;

import me.mowobbler.mainspigotutils.MainSpigotUtils;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class BungeeUtils {

    private final String ip;
    private final int port;
    private final String serverIdentifier;

    public BungeeUtils(String ip, int port, String serverIdentifier) {
        this.ip = ip;
        this.port = port;
        this.serverIdentifier = serverIdentifier;
    }

    public void sendPluginMessage(Player player, String subChannel, String message) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(subChannel);
            out.writeUTF(message);
            player.sendPluginMessage(MainSpigotUtils.instance, "BungeeCord", stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try (Socket socket = new Socket(ip, port);
             OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream())) {
            writer.write(serverIdentifier + "\n");
            writer.flush();
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            MainSpigotUtils.instance.getLogger().warning("Error sending message to BungeeCord plugin: " + e.getMessage());
        }
    }
}
