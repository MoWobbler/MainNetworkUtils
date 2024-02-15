package me.mowobbler.mainbungeeutils.commands;

import com.google.common.collect.ImmutableSet;
import me.mowobbler.mainbungeeutils.managers.MessageManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Message extends Command implements TabExecutor {

    MessageManager messageManager;

    public Message(MessageManager messageManager) {
        super("message", "proxyutils.message", "msg");
        this.messageManager = messageManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!messageManager.isValidSender(sender)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "You must be a player to use this command"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /message <player> <msg>"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (!messageManager.isValidReceiver(args[0])) {
            player.sendMessage(new TextComponent(ChatColor.RED + "The player \"" + args[0] + "\" could not be found"));
            return;
        }

        ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(args[0]);
        if (!receiver.isConnected()) {
            player.sendMessage(new TextComponent(ChatColor.RED + receiver.getName() + " is offline"));
            return;
        }

        String message = messageManager.buildMessage(args, 1);

        if (receiver.getName().equals(player.getName())) {
            player.sendMessage(new TextComponent(ChatColor.GRAY + "Note to self: " + message));
            return;
        }

        player.sendMessage(new TextComponent(ChatColor.GRAY + "You dmed " + receiver.getName() + ": " + message));
        messageManager.setLastPlayerMessaged(player,  receiver);
        receiver.sendMessage(new TextComponent(ChatColor.GRAY + player.getName() + " dmed you: " + message));
        messageManager.setLastPlayerMessagedBy(receiver, player);

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            String search = args[0].toLowerCase();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getName().toLowerCase().startsWith(search)) {
                    playerNames.add(player.getName());
                }
            }

            ProxiedPlayer player = (ProxiedPlayer) sender;
            ProxiedPlayer lastMessagedPlayer = messageManager.getLastPlayerMessaged(player);

            // Put the last messaged player at the top of the tab complete list
            if (lastMessagedPlayer != null) {
                playerNames.remove(lastMessagedPlayer.getName());
                playerNames.add(0, lastMessagedPlayer.getName());
            }

            return playerNames;
        } else if (args.length == 2) {
            Set<String> matches = new HashSet<>();
            String search = args[1].toLowerCase();

            String completedMessage = "Warning: Beware of teleporting unicorns! They're as unpredictable as a tornado in a teacup!";

            if (completedMessage.startsWith(search)) {
                matches.add(completedMessage);
            }
            return matches;

        }
        return ImmutableSet.of();
    }
}
