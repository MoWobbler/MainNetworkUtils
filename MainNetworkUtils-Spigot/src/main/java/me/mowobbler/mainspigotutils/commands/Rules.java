package me.mowobbler.mainspigotutils.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.awt.*;

public class Rules implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        ChatColor mainColor = ChatColor.of(new Color(40, 225, 202));
        ChatColor secondaryMainColor = ChatColor.of(new Color(66, 134, 244)); // Blue
        ChatColor secondaryColor = ChatColor.of(new Color(147, 147, 147)); // Orange

        String rulesMessage = secondaryMainColor + "===== " + mainColor + "MainBaseSMP Main Rules " + secondaryMainColor + "=====\n" +
                secondaryColor + ChatColor.BOLD + "Rule #1: " + ChatColor.WHITE + "Nonconsensual griefing and raiding is not allowed\n" +
                secondaryColor + ChatColor.BOLD + "Rule #2: " + ChatColor.WHITE + "Cheating that provides the user with a significant or unnatural advantage is not allowed\n" +
                secondaryColor + ChatColor.BOLD + "Rule #3: " + ChatColor.WHITE + "Doxxing and leaking real life personal information non-consensually is not allowed";

        sender.sendMessage(rulesMessage.split("\n"));

        return true;
    }
}
