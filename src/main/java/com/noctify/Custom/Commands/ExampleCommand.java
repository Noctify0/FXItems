package com.noctify.Custom.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExampleCommand implements CommandExecutor, TabCompleter {

    // This method is called when the command is executed by a player or the console
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true; // Return true to indicate the command was handled
        }

        // If no arguments are provided, show a usage message
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /example <subcommand>");
            return true;
        }

        // Handle subcommands based on the first argument
        switch (args[0].toLowerCase()) {
            case "info" -> {
                // Subcommand: /example info
                player.sendMessage(ChatColor.GREEN + "This is an example command with subcommands.");
            }
            case "help" -> {
                // Subcommand: /example help
                player.sendMessage(ChatColor.AQUA + "Example Command Help:");
                player.sendMessage(ChatColor.YELLOW + "/example info" + ChatColor.WHITE + " - Get information about this command.");
                player.sendMessage(ChatColor.YELLOW + "/example help" + ChatColor.WHITE + " - Show this help message.");
                player.sendMessage(ChatColor.YELLOW + "/example greet <name>" + ChatColor.WHITE + " - Greet a player.");
            }
            case "greet" -> {
                // Subcommand: /example greet <name>
                if (args.length < 2) {
                    // If no name is provided, show usage
                    player.sendMessage(ChatColor.RED + "Usage: /example greet <name>");
                } else {
                    // Greet the specified player
                    player.sendMessage(ChatColor.GREEN + "Hello, " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + "!");
                }
            }
            default -> {
                // Handle unknown subcommands
                player.sendMessage(ChatColor.RED + "Unknown subcommand. Use /example help for a list of subcommands.");
            }
        }
        return true; // Return true to indicate the command was handled
    }

    // This method is called when the player presses TAB while typing the command
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            // If the player is typing the first argument, suggest subcommands
            suggestions.addAll(Arrays.asList("info", "help", "greet"));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("greet")) {
            // If the player is typing the second argument for the "greet" subcommand, suggest player names
            suggestions.addAll(Arrays.asList("Player1", "Player2", "Player3")); // Replace with dynamic player names if needed
        }

        return suggestions; // Return the list of suggestions
    }
}