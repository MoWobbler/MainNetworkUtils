package me.mowobbler.mainbungeeutils.commands;

import me.mowobbler.mainbungeeutils.managers.RestartManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CancelRestart extends Command {

    RestartManager restartManager;

    public CancelRestart(RestartManager restartManager) {
        super("cancelrestart", "proxyutils.cancelrestart");
        this.restartManager = restartManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        restartManager.cancelRestart(sender);
    }
}
