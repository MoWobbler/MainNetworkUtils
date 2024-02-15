package me.mowobbler.mainbungeeutils.commands;

import me.mowobbler.mainbungeeutils.managers.MessageManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Reply extends Command {

    MessageManager messageManager;

    public Reply(MessageManager messageManager) {
        super("reply", "proxyutils.message", "r");
        this.messageManager = messageManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!messageManager.isValidSender(sender)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "You must be a player to use this command"));
            return;
        }

        if (args.length < 1) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /reply <msg>"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ProxiedPlayer receiver = messageManager.getLastPlayerMessagedBy(player);
        if (receiver == null) {
            player.sendMessage(new TextComponent(ChatColor.RED + "You have no one to respond to"));
            return;
        }

        if (!receiver.isConnected()) {
            player.sendMessage(new TextComponent(ChatColor.RED + receiver.getName() + " is offline"));
            return;
        }

        String message = messageManager.buildMessage(args, 0);
        player.sendMessage(new TextComponent(ChatColor.GRAY + "You replied to " + receiver.getName() + ": " + message));
        messageManager.setLastPlayerMessaged(player,  receiver);
        receiver.sendMessage(new TextComponent(ChatColor.GRAY + player.getName() + " replied to you: " + message));
        messageManager.setLastPlayerMessagedBy(receiver, player);
    }
}
