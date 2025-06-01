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

import java.util.ArrayList;
import java.util.List;

import com.noctify.Main.FXItems;

public class FXItem implements Listener {

    private final JavaPlugin plugin;

    public FXItem(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openMainMenu(Player player) {
        String guiTitle = FXItems.getFxItemsGuiTitle(); // Use the value from Lang.yml
        Inventory menu = Bukkit.createInventory(null, 9 * 5, guiTitle);

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
            if (player.hasPermission("fxitems.itemguiperm") && !lore.contains("§eRight Click to get item")) {
                lore.add("§eRight Click to get item");
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String guiTitle = FXItems.getFxItemsGuiTitle();
        if (!event.getView().getTitle().equals(guiTitle)) return;

        // Only handle clicks in the top inventory (the GUI)
        if (event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        // Only handle left or right click (not shift, double, or drag)
        if (!(event.isLeftClick() || event.isRightClick())) {
            event.setCancelled(true);
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.BLACK_STAINED_GLASS_PANE) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true); // Always cancel to prevent Bukkit from processing further

        // Handle right click: give item if admin, else show recipe
        if (event.isRightClick()) {
            if (handleGiveOrShowRecipe(clickedItem, player, true)) {
                player.closeInventory();
            }
            return;
        }

        // Handle left click: always show recipe
        if (event.isLeftClick()) {
            handleGiveOrShowRecipe(clickedItem, player, false);
        }
    }

    private boolean handleGiveOrShowRecipe(ItemStack clickedItem, Player player, boolean tryGive) {
        // Check custom items
        for (String itemId : ItemRegistry.getItemIds()) {
            ItemStack registeredItem = ItemRegistry.getCustomItem(itemId);
            if (registeredItem != null && CustomItemUtils.isCustomItem(clickedItem, registeredItem.getType(), registeredItem.getItemMeta().getDisplayName())) {
                if (tryGive && player.hasPermission("fxitems.itemguiperm")) {
                    player.getInventory().addItem(registeredItem.clone());
                    player.sendMessage("§aYou have received the item: " + registeredItem.getItemMeta().getDisplayName());
                    return true;
                } else {
                    try {
                        new CraftingGUI(plugin).openCraftingMenu(player, itemId);
                    } catch (RecipeException e) {}
                    return false;
                }
            }
        }
        // Check custom foods
        for (String foodId : FoodRegistry.getFoodIds()) {
            ItemStack registeredFood = FoodRegistry.getFoodItem(foodId);
            if (registeredFood != null && CustomItemUtils.isCustomItem(clickedItem, registeredFood.getType(), registeredFood.getItemMeta().getDisplayName())) {
                if (tryGive && player.hasPermission("fxitems.itemguiperm")) {
                    player.getInventory().addItem(registeredFood.clone());
                    player.sendMessage("§aYou have received the food: " + registeredFood.getItemMeta().getDisplayName());
                    return true;
                } else {
                    try {
                        new CraftingGUI(plugin).openFoodCraftingMenu(player, foodId);
                    } catch (RecipeException e) {}
                    return false;
                }
            }
        }
        player.sendMessage("§cError: Item not found in the registry.");
        return false;
    }
}