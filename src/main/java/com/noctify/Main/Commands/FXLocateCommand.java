package com.noctify.Main.Commands;

import com.noctify.Custom.BiomeRegistry;
import com.noctify.Main.Registration.FXBiomeDefinition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FXLocateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length != 1) {
            player.sendMessage("Usage: /fxlocate <biome_id>");
            return true;
        }
        String biomeId = args[0].toLowerCase();
        FXBiomeDefinition def = BiomeRegistry.getBiomeDefinition(biomeId);
        if (def == null) {
            player.sendMessage("§cCustom biome not found: " + biomeId);
            return true;
        }
        Location start = player.getLocation();
        int radius = 2000; // blocks
        int step = 16; // chunk size
        Location found = null;
        outer:
        for (int r = step; r <= radius; r += step) {
            for (int dx = -r; dx <= r; dx += step) {
                for (int dz = -r; dz <= r; dz += step) {
                    Location loc = start.clone().add(dx, 0, dz);
                    int chunkX = loc.getBlockX() >> 4;
                    int chunkZ = loc.getBlockZ() >> 4;
                    if (!loc.getWorld().isChunkLoaded(chunkX, chunkZ)) {
                        continue; // skip unloaded chunks to avoid server hang
                    }
                    loc.setY(loc.getWorld().getHighestBlockYAt(loc));
                    FXBiomeDefinition at = BiomeRegistry.getBiomeDefinitionAt(loc);
                    if (at != null && at.id.equalsIgnoreCase(biomeId)) {
                        found = loc;
                        break outer;
                    }
                }
            }
        }
        if (found != null) {
            player.sendMessage("§aNearest " + def.displayName + " found at: §e" +
                    found.getBlockX() + " " + found.getBlockY() + " " + found.getBlockZ());
        } else {
            player.sendMessage("§cNo " + def.displayName + " found within " + radius + " blocks (in loaded chunks).");
        }
        return true;
    }
}