package me.mowobbler.mainbungeeutils.commands;

import me.mowobbler.mainbungeeutils.utils.PluginMessagingUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GlobalWhitelist extends Command {

    public GlobalWhitelist() {
        super("globalwhitelist", "proxyutils.globalwhitelist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "You must be a player to use this command"));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /whitelist <add | remove> <player>"));
            return;
        }

        if (!(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /whitelist <add|remove> <player>"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PluginMessagingUtils.sendPluginMessageToBukkit("whitelist", args[0], args[1]);
        player.sendMessage(new TextComponent(ChatColor.GREEN + "Attempting to " + args[0] + " " + args[1] + " globally"));
    }
}
