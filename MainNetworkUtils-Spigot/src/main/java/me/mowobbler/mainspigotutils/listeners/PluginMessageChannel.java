package me.mowobbler.mainspigotutils.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessageChannel implements PluginMessageListener {

    String serverIdentifier;

    public PluginMessageChannel(String serverIdentifier) {
        this.serverIdentifier = serverIdentifier;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        switch (subchannel) {
            case "restarter":
                String serverName = in.readUTF();
                if (serverIdentifier.equals(serverName)) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
                }
                break;
            case "playsound":

                for (Player playsoundPlayer : Bukkit.getOnlinePlayers()) {
                    playsoundPlayer.playSound(player.getLocation(),
                            Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, SoundCategory.MASTER,
                            1.0f, 0.1f);
                }
                break;
            case "whitelist":
                String operation = in.readUTF();
                String playerToWhitelist = in.readUTF();
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist "
                        + operation
                        + " "
                        + playerToWhitelist);
                break;
        }
    }
}
