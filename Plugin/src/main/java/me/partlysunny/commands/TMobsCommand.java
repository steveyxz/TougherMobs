package me.partlysunny.commands;

import me.partlysunny.commands.subcommands.TMobsSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TMobsCommand implements CommandExecutor {

    public static final String command = "tmobs";
    public static Map<String, TMobsSubCommand> subCommands = new HashMap<>();

    public static void registerSubCommand(TMobsSubCommand c) {
        subCommands.put(c.getId(), c);
    }

    public static boolean executeSubCommand(String id, CommandSender exe, String[] args) {
        TMobsSubCommand tMobsSubCommand = subCommands.get(id);
        if (tMobsSubCommand == null) {
            return false;
        }
        tMobsSubCommand.execute(exe, args);
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            if (!p.hasPermission(TMobsCommand.command + ".admin")) {
                p.sendMessage(ChatColor.RED + "You cannot use this command!");
                return true;
            }
            if (strings.length == 0) {
                executeSubCommand("help", commandSender, new String[]{});
                return true;
            }
            String subCommand = strings[0];
            ArrayList<String> newArgs = new ArrayList<>(Arrays.asList(strings));
            newArgs.remove(0);
            if (!executeSubCommand(subCommand, commandSender, newArgs.toArray(new String[0]))) {
                p.sendMessage(ChatColor.RED + "That command does not exist!");
            }
        }
        return true;
    }

}
