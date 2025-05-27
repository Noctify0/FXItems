package com.noctify.Main.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EffectUtils {

    private static final Map<UUID, BukkitRunnable> activeTasks = new HashMap<>();

    /**
     * Adds an effect to a player while they hold a specific item.
     *
     * @param player The player to monitor.
     * @param itemMaterial The material of the item to check for.
     * @param effectType The type of effect to apply.
     * @param duration The duration of the effect in ticks (e.g., 20 ticks = 1 second).
     * @param amplifier The amplifier (multiplier) of the effect.
     */
    public static void addEffectWhileHolding(Player player, Material itemMaterial, PotionEffectType effectType, int duration, int amplifier) {
        UUID playerId = player.getUniqueId();

        // Cancel any existing task for this player
        if (activeTasks.containsKey(playerId)) {
            activeTasks.get(playerId).cancel();
        }

        // Create a new repeating task to monitor the player's held item
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack heldItem = player.getInventory().getItemInMainHand();

                // Check if the player is holding the specified item
                if (heldItem != null && heldItem.getType() == itemMaterial) {
                    // Apply the effect if not already active
                    if (!player.hasPotionEffect(effectType)) {
                        player.addPotionEffect(new PotionEffect(effectType, duration, amplifier, true, false));
                    }
                } else {
                    // Remove the effect if the player is not holding the item
                    player.removePotionEffect(effectType);
                }
            }
        };

        // Schedule the task to run every tick
        task.runTaskTimer(Bukkit.getPluginManager().getPlugin("YourPluginName"), 0L, 1L);
        activeTasks.put(playerId, task);
    }

    /**
     * Removes the effect monitoring task for a player.
     *
     * @param player The player to stop monitoring.
     */
    public static void removeEffectTask(Player player) {
        UUID playerId = player.getUniqueId();

        if (activeTasks.containsKey(playerId)) {
            activeTasks.get(playerId).cancel();
            activeTasks.remove(playerId);
        }
    }
}