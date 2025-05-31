package com.noctify.API;

import com.noctify.Main.Utils.OneTimeCraftUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Utils {
    boolean isOnCooldown(UUID playerId, String key);
    double getRemainingCooldown(UUID playerId, String key);
    void setCooldown(UUID playerId, String key, int seconds);
    void sendCooldownMessage(Player player, String itemName, double timeLeft);
    void clearAllCooldowns();

    boolean isCustomItem(ItemStack item, Material material, String displayName);

    void addEffectWhileHolding(Player player, Material itemMaterial, PotionEffectType effectType, int duration, int amplifier);
    void removeEffectTask(Player player);

    void setSize(Entity entity, float scale);
    void resetSize(Entity entity);

    void initializeManaUtils(Plugin plugin);
    boolean useMana(Player player, int amount);
    void setMana(Player player, int amount);
    int getMana(Player player);

    void registerOneTimeCraft(OneTimeCraftUtils instance, String name, ItemStack item);
    Map<String, OneTimeCraftUtils.OneTimeCraftItem> getRegisteredOneTimeCraftItems();

    void initializeProjectileUtils(Plugin plugin);
    void createCustomProjectile(
            Plugin plugin,
            Player shooter,
            Particle trailParticle,
            int particleCount,
            boolean gravity,
            boolean glowing,
            int knockbackStrength,
            boolean silent,
            double speed,
            double damage,
            boolean speedBasedDamage,
            EntityType projectileType,
            boolean useModelEngine,
            String modelId,
            int despawnTime,
            int piercingLevel
    );

    boolean teleportPlayer(Player player, Location targetLocation, int maxDistance, boolean preserveDirection, boolean playSound, boolean spawnParticles);
}