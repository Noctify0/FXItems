package com.noctify.Main.GUIs;

import com.noctify.Custom.ItemRegistry;
import com.noctify.Main.Utils.OneTimeCraftUtils;
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

import java.lang.reflect.Method;
import java.util.Map;

public class CraftingGUI implements Listener {

    private final JavaPlugin plugin;

    public CraftingGUI(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openCraftingMenu(Player player, String itemName) {
        String keyName = itemName.toLowerCase();
        Class<?> itemClass = ItemRegistry.getItemClass(keyName);

        if (itemClass == null) {
            player.sendMessage("§cError: Recipe for item '" + itemName + "' not found.");
            return;
        }

        try {
            Method recipeMethod = itemClass.getMethod("getRecipe", Plugin.class, org.bukkit.NamespacedKey.class);
            org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey(plugin, keyName + "_recipe");
            Object recipe = recipeMethod.invoke(null, plugin, key);

            if (recipe instanceof org.bukkit.inventory.ShapedRecipe shapedRecipe) {
                Inventory menu = Bukkit.createInventory(null, 45, "§8§l" + itemName + "'s Recipe");

                // Fill all slots with glass panes
                ItemStack glassPane = createGlassPane();
                for (int i = 0; i < 45; i++) {
                    menu.setItem(i, glassPane);
                }

                // Custom crafting grid layout
                int[] craftingSlots = {11, 12, 13, 20, 21, 22, 29, 30, 31};
                String[] shape = shapedRecipe.getShape();
                Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();

                for (int row = 0; row < 3; row++) {
                    char[] rowChars = row < shape.length ? shape[row].toCharArray() : new char[3];
                    for (int col = 0; col < 3; col++) {
                        int slotIndex = craftingSlots[row * 3 + col];
                        char ingredientChar = (col < rowChars.length) ? rowChars[col] : ' ';
                        if (ingredientChar != ' ' && ingredientMap.containsKey(ingredientChar)) {
                            menu.setItem(slotIndex, ingredientMap.get(ingredientChar));
                        } else {
                            menu.setItem(slotIndex, null); // Leave unused slots blank
                        }
                    }
                }

                // Check if the item is globally crafted
                String craftedKey = keyName + "_crafted";
                boolean crafted = plugin.getConfig().getBoolean(craftedKey);

                if (crafted) {
                    ItemStack barrier = new ItemStack(Material.BARRIER);
                    ItemMeta meta = barrier.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName("§cItem Already Crafted");
                        meta.setLore(java.util.List.of("§7This legendary item can only be crafted once."));
                        barrier.setItemMeta(meta);
                    }
                    menu.setItem(24, barrier);
                } else {
                    menu.setItem(24, shapedRecipe.getResult().clone());
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