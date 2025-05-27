package com.noctify.Main.Utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownUtils {

    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public static boolean isOnCooldown(UUID playerId, String key) {
        Map<String, Long> playerCooldowns = cooldowns.get(playerId);
        if (playerCooldowns == null) return false;

        Long expiryTime = playerCooldowns.get(key);
        return expiryTime != null && System.currentTimeMillis() < expiryTime;
    }

    public static double getRemainingCooldown(UUID playerId, String key) {
        Map<String, Long> playerCooldowns = cooldowns.get(playerId);
        if (playerCooldowns == null) return 0;

        Long expiryTime = playerCooldowns.get(key);
        if (expiryTime == null) return 0;

        return Math.max(0, (expiryTime - System.currentTimeMillis()) / 1000.0);
    }

    public static void setCooldown(UUID playerId, String key, int seconds) {
        cooldowns.computeIfAbsent(playerId, k -> new HashMap<>())
                .put(key, System.currentTimeMillis() + seconds * 1000L);
    }

    public static void sendCooldownMessage(Player player, String itemName, double timeLeft) {
        player.sendMessage(String.format("§c%s is on cooldown for %.1f seconds.", itemName, timeLeft));
    }

    public static void clearAllCooldowns() {
        cooldowns.clear();
    }
}