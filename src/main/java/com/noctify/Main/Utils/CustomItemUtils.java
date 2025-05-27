package com.noctify.Main.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemUtils {

    /**
     * Checks if the given item matches the specified material and display name.
     *
     * @param item        The item to check.
     * @param material    The expected material of the item.
     * @param displayName The expected display name of the item.
     * @return True if the item matches the specified material and display name, false otherwise.
     */
    public static boolean isCustomItem(ItemStack item, Material material, String displayName) {
        if (item == null || item.getType() != material) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }

        return meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', displayName));
    }
}