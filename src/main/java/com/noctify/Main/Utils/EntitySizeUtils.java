package com.noctify.Main.Utils;

import com.maximde.entitysize.EntitySize;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class EntitySizeUtils {

    private static final EntitySize entitySizePlugin = (EntitySize) Bukkit.getPluginManager().getPlugin("EntitySize");

    /**
     * Sets the size of an entity.
     *
     * @param entity The entity to resize.
     * @param scale The scale factor (e.g., 0.5 for half size, 2.0 for double size).
     */
    public static void setSize(Entity entity, float scale) {
        if (entitySizePlugin == null) {
            Bukkit.getLogger().warning("EntitySize plugin is not available.");
            return;
        }

        if (entity instanceof Player player) {
            entitySizePlugin.setSize(player, scale);
        } else if (entity instanceof LivingEntity livingEntity) {
            entitySizePlugin.setSize(livingEntity, scale);
        } else {
            Bukkit.getLogger().warning("EntitySize plugin does not support resizing this entity type: " + entity.getType());
        }
    }

    /**
     * Resets the size of an entity to its default.
     *
     * @param entity The entity to reset.
     */
    public static void resetSize(Entity entity) {
        if (entitySizePlugin == null) {
            Bukkit.getLogger().warning("EntitySize plugin is not available.");
            return;
        }

        if (entity instanceof Player player) {
            entitySizePlugin.resetSize(player);
        } else if (entity instanceof LivingEntity livingEntity) {
            entitySizePlugin.setSize(livingEntity, 1.0f);
        } else {
            Bukkit.getLogger().warning("EntitySize plugin does not support resetting size for this entity type: " + entity.getType());
        }
    }
}