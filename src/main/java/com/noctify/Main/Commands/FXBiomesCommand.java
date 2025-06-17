package com.noctify.Main.Commands;

import com.noctify.Main.GUIs.FXBiomesGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FXBiomesCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    public FXBiomesCommand(JavaPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        new FXBiomesGUI(plugin).open(player);
        return true;
    }
}