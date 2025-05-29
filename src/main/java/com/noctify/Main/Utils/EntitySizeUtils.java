package com.noctify.Main.Utils;

import com.maximde.entitysize.EntitySize;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class EntitySizeUtils {

    private static final EntitySize entitySizePlugin;
    public static final boolean ENABLED;

    static {
        EntitySize plugin = null;
        boolean enabled = true;
        try {
            plugin = (EntitySize) Bukkit.getPluginManager().getPlugin("EntitySize");
            if (plugin == null) {
                enabled = false;
                Bukkit.getLogger().warning("[FXItems] EntitySize plugin not found. EntitySizeUtils is disabled.");
            }
        } catch (Throwable t) {
            enabled = false;
            Bukkit.getLogger().warning("[FXItems] Error loading EntitySize plugin. EntitySizeUtils is disabled.");
        }
        entitySizePlugin = plugin;
        ENABLED = enabled;
    }

    public static void setSize(Entity entity, float scale) {
        if (!ENABLED) return;
        if (entity instanceof Player player) {
            entitySizePlugin.setSize(player, scale);
        } else if (entity instanceof LivingEntity livingEntity) {
            entitySizePlugin.setSize(livingEntity, scale);
        } else {
            Bukkit.getLogger().warning("EntitySize plugin does not support resizing this entity type: " + entity.getType());
        }
    }

    public static void resetSize(Entity entity) {
        if (!ENABLED) return;
        if (entity instanceof Player player) {
            entitySizePlugin.resetSize(player);
        } else if (entity instanceof LivingEntity livingEntity) {
            entitySizePlugin.setSize(livingEntity, 1.0f);
        } else {
            Bukkit.getLogger().warning("EntitySize plugin does not support resetting size for this entity type: " + entity.getType());
        }
    }
}