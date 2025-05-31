package com.noctify.Main.GUIs;

import com.noctify.Custom.FoodRegistry;
import com.noctify.Custom.ItemRegistry;
import com.noctify.Main.Utils.CustomItemUtils;
import com.noctify.Main.Exceptions.RecipeException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FXItem implements Listener {

    private final JavaPlugin plugin;

    public FXItem(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openMainMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9 * 5, "§8§lFXItems");

        // Fill edges with dark gray glass panes
        ItemStack glassPane = createGlassPane();
        for (int i = 0; i < menu.getSize(); i++) {
            if (i < 9 || i >= menu.getSize() - 9 || i % 9 == 0 || i % 9 == 8) {
                menu.setItem(i, glassPane);
            }
        }

        int index = 10; // Start from the second row

        // Add registered items
        for (String itemId : ItemRegistry.getItemIds()) {
            ItemStack item = ItemRegistry.getCustomItem(itemId);
            if (item != null) {
                addLoreToItem(item, player);
                menu.setItem(index, item);
                index++;
                if (index % 9 == 8) index += 2; // Skip edges
            }
        }

        // Add registered foods
        for (String foodId : FoodRegistry.getFoodIds()) {
            ItemStack food = FoodRegistry.getFoodItem(foodId);
            if (food != null) {
                addLoreToItem(food, player); // Reuse the same lore helper
                menu.setItem(index, food);
                index++;
                if (index % 9 == 8) index += 2; // Skip edges
            }
        }

        player.openInventory(menu);
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

    private void addLoreToItem(ItemStack item, Player player) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore() != null ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
            if (!lore.contains("§eLeft Click to view Recipe")) {
                lore.add("§eLeft Click to view Recipe");
            }
            if (player.hasPermission("fxitems.itemguiperm") && !lore.contains("§eShift + Right Click to get item")) {
                lore.add("§eShift + Right Click to get item");
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§8§lFXItems")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.BLACK_STAINED_GLASS_PANE) {
                Player player = (Player) event.getWhoClicked();
                ItemStack clickedItem = event.getCurrentItem();

                if (event.isLeftClick()) {
                    boolean found = false;
                    // Check custom items
                    for (String itemId : ItemRegistry.getItemIds()) {
                        ItemStack registeredItem = ItemRegistry.getCustomItem(itemId);
                        if (registeredItem != null && CustomItemUtils.isCustomItem(clickedItem, registeredItem.getType(), registeredItem.getItemMeta().getDisplayName())) {
                            String keyName = itemId;
                            try {
                                new CraftingGUI(plugin).openCraftingMenu(player, keyName);
                            } catch (RecipeException e) {
                                // Exception already sends the message to the player
                            }
                            found = true;
                            break;
                        }
                    }
                    // Check custom foods if not found in items
                    if (!found) {
                        for (String foodId : FoodRegistry.getFoodIds()) {
                            ItemStack registeredFood = FoodRegistry.getFoodItem(foodId);
                            if (registeredFood != null && CustomItemUtils.isCustomItem(clickedItem, registeredFood.getType(), registeredFood.getItemMeta().getDisplayName())) {
                                String keyName = foodId;
                                try {
                                    // You may need to implement openFoodCraftingMenu in CraftingGUI
                                    new CraftingGUI(plugin).openFoodCraftingMenu(player, keyName);
                                } catch (RecipeException e) {
                                    // Exception already sends the message to the player
                                }
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        player.sendMessage("§cError: Item not found in the registry.");
                    }
                }
            }
        }
    }
}