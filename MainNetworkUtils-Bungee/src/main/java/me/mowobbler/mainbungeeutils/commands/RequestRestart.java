package me.mowobbler.mainbungeeutils.commands;

import me.mowobbler.mainbungeeutils.managers.RestartManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RequestRestart extends Command {

    RestartManager restartManager;

    public RequestRestart(RestartManager utils) {
        super("requestrestart", "proxyutils.requestrestart");
        this.restartManager = utils;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "You must be a player to use this command"));
            return;
        }
        restartManager.requestRestart((ProxiedPlayer) sender);
    }
}
