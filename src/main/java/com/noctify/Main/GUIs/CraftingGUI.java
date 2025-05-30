package com.noctify.Main.GUIs;

import com.noctify.Custom.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class CraftingGUI implements Listener {

    private final JavaPlugin plugin;

    public CraftingGUI(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openCraftingMenu(Player player, String itemName) {
        Class<?> itemClass = ItemRegistry.getItemClass(itemName);

        if (itemClass == null) {
            player.sendMessage("§cError: Recipe for item '" + itemName + "' not found.");
            return;
        }

        try {
            // Retrieve the recipe method
            java.lang.reflect.Method recipeMethod = itemClass.getMethod("getRecipe", Plugin.class, org.bukkit.NamespacedKey.class);
            org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey(plugin, itemName + "_recipe");
            Object recipe = recipeMethod.invoke(null, plugin, key);

            if (recipe instanceof org.bukkit.inventory.ShapedRecipe shapedRecipe) {
                Inventory menu = Bukkit.createInventory(null, 9 * 5, "§8§l" + itemName + "'s Recipe");

                // Fill edges with dark gray glass panes
                ItemStack glassPane = createGlassPane();
                for (int i = 0; i < menu.getSize(); i++) {
                    if (i < 9 || i >= menu.getSize() - 9 || i % 9 == 0 || i % 9 == 8) {
                        menu.setItem(i, glassPane);
                    }
                }

                // Define crafting slots
                int[] craftingSlots = {11, 12, 13, 20, 21, 22, 29, 30, 31};
                String[] shape = shapedRecipe.getShape();
                Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();

                // Populate crafting slots based on the recipe shape
                for (int row = 0; row < shape.length; row++) {
                    char[] rowChars = shape[row].toCharArray();
                    for (int col = 0; col < rowChars.length; col++) {
                        char ingredientChar = rowChars[col];
                        int slotIndex = craftingSlots[row * 3 + col];
                        if (ingredientMap.containsKey(ingredientChar)) {
                            menu.setItem(slotIndex, ingredientMap.get(ingredientChar));
                        }
                    }
                }

                // Set the result item in slot 24
                menu.setItem(24, shapedRecipe.getResult().clone());

                // Fill remaining slots with glass panes
                for (int i = 0; i < menu.getSize(); i++) {
                    if (menu.getItem(i) == null) {
                        menu.setItem(i, glassPane);
                    }
                }

                player.openInventory(menu);
            } else {
                player.sendMessage("§cError: Recipe for item '" + itemName + "' is not a valid ShapedRecipe.");
            }
        } catch (Exception e) {
            player.sendMessage("§cError: Failed to retrieve recipe for item '" + itemName + "'.");
            e.printStackTrace();
        }
    }

    private ItemStack createGlassPane() {
        ItemStack glassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = glassPane.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" "); // Blank name
            glassPane.setItemMeta(meta);
        }
        return glassPane;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().endsWith("'s Recipe")) {
            event.setCancelled(true); // Prevent item removal
        }
    }
}