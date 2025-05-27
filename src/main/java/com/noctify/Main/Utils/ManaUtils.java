package com.noctify.Main.Utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ManaUtils {

    private static final int MAX_MANA = 100;
    private static final int REGEN_RATE = 4; // Mana per second
    private static final int REGEN_DELAY = 8; // Seconds
    private static final String MANA_EMOJI = "✨"; // Emoji representing mana

    private static final HashMap<UUID, Integer> mana = new HashMap<>();
    private static final HashMap<UUID, Long> lastManaUse = new HashMap<>();

    private static Plugin plugin;

    public static void initialize(Plugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static boolean useMana(Player player, int amount) {
        UUID playerId = player.getUniqueId();
        mana.putIfAbsent(playerId, MAX_MANA);

        int currentMana = mana.get(playerId);
        if (currentMana < amount) {
            player.sendMessage(ChatColor.RED + "Not enough mana!");
            return false;
        }

        mana.put(playerId, currentMana - amount);
        lastManaUse.put(playerId, System.currentTimeMillis());
        updateManaBar(player);

        // Start regeneration task if not already running
        startRegenTask(player);
        return true;
    }

    private static void startRegenTask(Player player) {
        UUID playerId = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                long lastUse = lastManaUse.getOrDefault(playerId, 0L);
                if (System.currentTimeMillis() - lastUse < REGEN_DELAY * 1000L) {
                    return; // Wait for the delay to pass
                }

                int currentMana = mana.getOrDefault(playerId, MAX_MANA);
                if (currentMana >= MAX_MANA) {
                    cancel(); // Stop task if mana is full
                    return;
                }

                mana.put(playerId, Math.min(currentMana + REGEN_RATE, MAX_MANA));
                updateManaBar(player);
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every second
    }

    private static void updateManaBar(Player player) {
        int currentMana = mana.getOrDefault(player.getUniqueId(), MAX_MANA);
        String manaBar = ChatColor.AQUA + "Mana: " + currentMana + "/" + MAX_MANA + " " + MANA_EMOJI;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(manaBar));
    }

    public static void setMana(Player player, int amount) {
        mana.put(player.getUniqueId(), Math.min(amount, MAX_MANA));
        updateManaBar(player);
    }

    public static int getMana(Player player) {
        return mana.getOrDefault(player.getUniqueId(), MAX_MANA);
    }
}