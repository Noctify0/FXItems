package com.noctify.API;

import com.noctify.Main.Exceptions.ProjectileException;
import com.noctify.Main.Utils.CooldownUtils;
import com.noctify.Main.Utils.CustomItemUtils;
import com.noctify.Main.Utils.EffectUtils;
import com.noctify.Main.Utils.EntitySizeUtils;
import com.noctify.Main.Utils.ManaUtils;
import com.noctify.Main.Utils.OneTimeCraftUtils;
import com.noctify.Main.Utils.ProjectileUtils;
import com.noctify.Main.Utils.TeleportUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.EntityType;

import java.util.Map;
import java.util.UUID;

public class UtilsAPI implements Utils {

    @Override
    public boolean isOnCooldown(UUID playerId, String key) {
        return CooldownUtils.isOnCooldown(playerId, key);
    }

    @Override
    public double getRemainingCooldown(UUID playerId, String key) {
        return CooldownUtils.getRemainingCooldown(playerId, key);
    }

    @Override
    public void setCooldown(UUID playerId, String key, int seconds) {
        CooldownUtils.setCooldown(playerId, key, seconds);
    }

    @Override
    public void sendCooldownMessage(Player player, String itemName, double timeLeft) {
        CooldownUtils.sendCooldownMessage(player, itemName, timeLeft);
    }

    @Override
    public void clearAllCooldowns() {
        CooldownUtils.clearAllCooldowns();
    }

    @Override
    public boolean isCustomItem(ItemStack item, Material material, String displayName) {
        return CustomItemUtils.isCustomItem(item, material, displayName);
    }

    @Override
    public void addEffectWhileHolding(Player player, Material itemMaterial, PotionEffectType effectType, int duration, int amplifier) {
        EffectUtils.addEffectWhileHolding(player, itemMaterial, effectType, duration, amplifier);
    }

    @Override
    public void removeEffectTask(Player player) {
        EffectUtils.removeEffectTask(player);
    }

    @Override
    public void setSize(Entity entity, float scale) {
        EntitySizeUtils.setSize(entity, scale);
    }

    @Override
    public void resetSize(Entity entity) {
        EntitySizeUtils.resetSize(entity);
    }

    @Override
    public void initializeManaUtils(Plugin plugin) {
        ManaUtils.initialize(plugin);
    }

    @Override
    public boolean useMana(Player player, int amount) {
        return ManaUtils.useMana(player, amount);
    }

    @Override
    public void setMana(Player player, int amount) {
        ManaUtils.setMana(player, amount);
    }

    @Override
    public int getMana(Player player) {
        return ManaUtils.getMana(player);
    }

    @Override
    public void registerOneTimeCraft(OneTimeCraftUtils instance, String name, ItemStack item) {
        instance.registerCraft(name, item);
    }

    @Override
    public Map<String, OneTimeCraftUtils.OneTimeCraftItem> getRegisteredOneTimeCraftItems() {
        return OneTimeCraftUtils.getRegisteredItems();
    }

    @Override
    public void initializeProjectileUtils(Plugin plugin) {
        ProjectileUtils.initialize(plugin);
    }

    @Override
    public void createCustomProjectile(
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

    @Override
    public boolean teleportPlayer(Player player, Location targetLocation, int maxDistance, boolean preserveDirection, boolean playSound, boolean spawnParticles) {
        return TeleportUtils.teleportPlayer(player, targetLocation, maxDistance, preserveDirection, playSound, spawnParticles);
    }
}