package com.noctify.Main.Utils;

import com.noctify.Custom.ItemRegistry;
import com.noctify.Main.Registration.FXItemDefinition;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class OneTimeCraftUtils {

    private static final Map<String, OneTimeCraftItem> registeredItems = new HashMap<>();

    public OneTimeCraftUtils(Plugin plugin) {
        initializeRegisteredItems();
    }

    private void initializeRegisteredItems() {
        for (String itemId : ItemRegistry.getItemIds()) {
            FXItemDefinition def = ItemRegistry.getItemDefinition(itemId);
            if (def != null && def.oneTimeCraft) {
                ItemStack item = ItemRegistry.getCustomItem(itemId);
                if (item != null) {
                    registerCraft(itemId, item);
                }
            }
        }
    }

    public void registerCraft(String name, ItemStack item) {
        registeredItems.put(name, new OneTimeCraftItem(name, item.getType()));
    }

    public static Map<String, OneTimeCraftItem> getRegisteredItems() {
        return registeredItems;
    }

    // Checks if the item is a one-time craft (by definition)
    public boolean isOneTimeCraft(ItemStack item) {
        if (item == null) return false;
        FXItemDefinition def = ItemRegistry.getDefinition(item);
        return def != null && def.oneTimeCraft;
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