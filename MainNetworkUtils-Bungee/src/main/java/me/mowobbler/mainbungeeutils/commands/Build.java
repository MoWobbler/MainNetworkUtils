package me.mowobbler.mainbungeeutils.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Build extends Command {

    public Build() {
        super("build", "proxyutils.build");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("This command can only be executed by players"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (!player.getServer().getInfo().getName().equalsIgnoreCase("mainbasebuild")) {
            ServerInfo buildServer = ProxyServer.getInstance().getServerInfo("mainbasebuild");
            if (buildServer != null) {
                player.connect(buildServer);
            } else {
                sender.sendMessage(new TextComponent("MainBaseBuild server is not available."));
            }
        } else {
            sender.sendMessage(new TextComponent("You are already on the build server."));
        }
    }
}
