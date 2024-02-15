package me.mowobbler.mainspigotutils.commands;

import me.mowobbler.mainspigotutils.MainSpigotUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Coords implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player player = null;
        if (sender instanceof Player)
            player = (Player) sender;

        if (player == null) {
            if (args.length != 1) {
                MainSpigotUtils.instance.getLogger().info("You need to specify an argument.");
                return true;
            }

            /* Only used on online players */
            Player target = MainSpigotUtils.instance.getServer().getPlayer(args[0]);
            if (target == null) {
                MainSpigotUtils.instance.getLogger().info("The specified player was not found.");
                return true;
            }

            Location loc = target.getLocation();
            if (loc.getWorld() == null) {
                MainSpigotUtils.instance.getLogger().info("The world could not be found.");
                return true;
            }
            MainSpigotUtils.instance.getLogger().info(target.getName() + " is at "
                    + loc.getBlockX() + " "
                    + loc.getBlockY() + " "
                    + loc.getBlockZ() + " "
                    + loc.getWorld().getName());

        } else {
            Location loc = player.getLocation();
            if (loc.getWorld() == null) {
                MainSpigotUtils.instance.getLogger().info("The world could not be found.");
                return true;
            }
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            sender.sendMessage(ChatColor.GOLD + " Your coordinates are:\n" +
                    " World = " + loc.getWorld().getName() + "\n" +
                    " X = " + x + "\n" +
                    " Y = " + y + "\n" +
                    " Z = " + z);
        }
        return true;
    }
}
