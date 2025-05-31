package com.noctify.API;

import com.noctify.Main.Exceptions.ProjectileException;
import com.noctify.Main.Utils.CooldownUtils;
import com.noctify.Main.Utils.CustomItemUtils;
import com.noctify.Main.Utils.EffectUtils;
import com.noctify.Main.Utils.EntitySizeUtils;
import com.noctify.Main.Utils.GammaUtils;
import com.noctify.Main.Utils.ManaUtils;
import com.noctify.Main.Utils.OneTimeCraftUtils;
import com.noctify.Main.Utils.ProjectileUtils;
import com.noctify.Main.Utils.TeleportUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Utils {

    // --- CooldownUtils ---
    public static boolean isOnCooldown(UUID playerId, String key) {
        return CooldownUtils.isOnCooldown(playerId, key);
    }
    public static double getRemainingCooldown(UUID playerId, String key) {
        return CooldownUtils.getRemainingCooldown(playerId, key);
    }
    public static void setCooldown(UUID playerId, String key, int seconds) {
        CooldownUtils.setCooldown(playerId, key, seconds);
    }
    public static void sendCooldownMessage(Player player, String itemName, double timeLeft) {
        CooldownUtils.sendCooldownMessage(player, itemName, timeLeft);
    }
    public static void clearAllCooldowns() {
        CooldownUtils.clearAllCooldowns();
    }

    // --- CustomItemUtils ---
    public static boolean isCustomItem(ItemStack item, Material material, String displayName) {
        return CustomItemUtils.isCustomItem(item, material, displayName);
    }

    // --- EffectUtils ---
    public static void addEffectWhileHolding(Player player, Material itemMaterial, PotionEffectType effectType, int duration, int amplifier) {
        EffectUtils.addEffectWhileHolding(player, itemMaterial, effectType, duration, amplifier);
    }
    public static void removeEffectTask(Player player) {
        EffectUtils.removeEffectTask(player);
    }

    // --- EntitySizeUtils ---
    public static void setSize(Entity entity, float scale) {
        EntitySizeUtils.setSize(entity, scale);
    }
    public static void resetSize(Entity entity) {
        EntitySizeUtils.resetSize(entity);
    }

    // --- GammaUtils ---
    // No static utility methods to expose; GammaUtils is a CommandExecutor.

    // --- ManaUtils ---
    public static void initializeManaUtils(Plugin plugin) {
        ManaUtils.initialize(plugin);
    }
    public static boolean useMana(Player player, int amount) {
        return ManaUtils.useMana(player, amount);
    }
    public static void setMana(Player player, int amount) {
        ManaUtils.setMana(player, amount);
    }
    public static int getMana(Player player) {
        return ManaUtils.getMana(player);
    }

    // --- OneTimeCraftUtils ---
    public static void registerOneTimeCraft(OneTimeCraftUtils instance, String name, ItemStack item) {
        instance.registerCraft(name, item);
    }
    public static Map<String, OneTimeCraftUtils.OneTimeCraftItem> getRegisteredOneTimeCraftItems() {
        return OneTimeCraftUtils.getRegisteredItems();
    }

    // --- ProjectileUtils ---
    public static void initializeProjectileUtils(Plugin plugin) {
        ProjectileUtils.initialize(plugin);
    }
    public static void createCustomProjectile(
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
    ) {
        try {
            ProjectileUtils.createCustomProjectile(
                    plugin, shooter, trailParticle, particleCount, gravity, glowing, knockbackStrength, silent,
                    speed, damage, speedBasedDamage, projectileType, useModelEngine, modelId, despawnTime, piercingLevel
            );
        } catch (ProjectileException e) {
            // Exception already sends/logs the message
        }
    }

    // --- TeleportUtils ---
    public static boolean teleportPlayer(Player player, Location targetLocation, int maxDistance, boolean preserveDirection, boolean playSound, boolean spawnParticles) {
        return TeleportUtils.teleportPlayer(player, targetLocation, maxDistance, preserveDirection, playSound, spawnParticles);
    }
}