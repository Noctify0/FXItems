package com.noctify.Main.Utils;

import com.noctify.Custom.ItemRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class OneTimeCraftUtils {

    private static final Map<String, OneTimeCraftItem> registeredItems = new HashMap<>();
    private final Plugin plugin;

    public OneTimeCraftUtils(Plugin plugin) {
        this.plugin = plugin;
        initializeRegisteredItems();
    }

    // Initialize registered items from the ItemRegistry
    private void initializeRegisteredItems() {
        for (String itemId : ItemRegistry.getItemIds()) {
            ItemStack item = ItemRegistry.getCustomItem(itemId);
            if (item != null) {
                registerCraft(itemId, item);
            }
        }
    }

    public void registerCraft(String name, ItemStack item) {
        // Add the legendary tag to the item
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, name + "_crafted"), PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }

        // Register the item for one-time crafting
        registeredItems.put(name, new OneTimeCraftItem(name, item.getType()));
    }

    public static Map<String, OneTimeCraftItem> getRegisteredItems() {
        return registeredItems;
    }

    // Checks if the item is a one-time craft (has a *_crafted tag)
    public boolean isOneTimeCraft(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        for (NamespacedKey key : container.getKeys()) {
            if (key.getKey().endsWith("_crafted") && container.get(key, PersistentDataType.BYTE) != null) {
                return true;
            }
        }
        return false;
    }

    // Checks if the player has crafted the item (stub, adjust to your storage)
    public boolean hasCrafted(Player player, ItemStack item) {
        // TODO: Implement actual player crafting check logic
        // For now, always return false (not crafted)
        return false;
    }

    public static class OneTimeCraftItem {
        private final String name;
        private final org.bukkit.Material material;

        public OneTimeCraftItem(String name, org.bukkit.Material material) {
            this.name = name;
            this.material = material;
        }

        public String getName() {
            return name;
        }

        public org.bukkit.Material getMaterial() {
            return material;
        }
    }
}