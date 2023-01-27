package me.partlysunny.commands.subcommands;

import me.partlysunny.commands.TMobsCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class HelpSubCommand implements TMobsSubCommand {
    @Override
    public String getId() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Get a list of all commands.";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public void execute(CommandSender executor, String[] args) {
        Collection<TMobsSubCommand> commands = TMobsCommand.subCommands.values();
        executor.sendMessage(ChatColor.YELLOW + "List of commands (run with /sbl <command>):");
        for (TMobsSubCommand c : commands) {
            executor.sendMessage(ChatColor.WHITE + c.getId() + c.getUsage() + ": " + c.getDescription());
        }
    }
}
