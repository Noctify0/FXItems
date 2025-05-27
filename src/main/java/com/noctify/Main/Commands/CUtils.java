package com.noctify.Main.Commands;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CUtils implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /c-Utils <subcommand>");
            return true;
        }

        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "cleararmor" -> {
                AttributeInstance armorAttribute = player.getAttribute(Attribute.ARMOR);
                if (armorAttribute != null) {
                    armorAttribute.setBaseValue(0); // Reset armor points to 0
                    player.sendMessage(ChatColor.GREEN + "Your armor points have been reset to 0!");
                } else {
                    player.sendMessage(ChatColor.RED + "Unable to reset armor points.");
                }
            }
            default -> player.sendMessage(ChatColor.RED + "Unknown subcommand. Available: cleararmor");
        }

        return true;
    }
}