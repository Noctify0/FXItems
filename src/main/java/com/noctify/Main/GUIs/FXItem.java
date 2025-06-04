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
import java.util.UUID;

import com.noctify.Main.FXItems;

public class FXItem implements Listener {

    private final JavaPlugin plugin;

    public FXItem(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openMainMenu(Player player) {
        String guiTitle = FXItems.getFxItemsGuiTitle();
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
                addLoreToItem(food, player);
                menu.setItem(index, food);
                index++;
                if (index % 9 == 8) index += 2; // Skip edges
            }
        }

        // Add registered armors
        for (String armorId : com.noctify.Custom.ArmorRegistry.getArmorIds()) {
            ItemStack armor = com.noctify.Custom.ArmorRegistry.getCustomArmor(armorId);
            if (armor != null) {
                addLoreToItem(armor, player);
                menu.setItem(index, armor);
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
        // Clone the item to avoid modifying the original reference
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        // Remove any old right-click/left-click lore if present
        lore.remove("§eRight Click to get item");
        lore.remove("§eLeft Click to view Recipe");

        // Only add if not already present and limit total lines
        if (!lore.contains("§eLeft Click to view Recipe") && lore.size() < 255) {
            if (!lore.isEmpty()) {
                lore.add(""); // Add a blank line after original lore
            }
            lore.add("§eLeft Click to view Recipe");
        }

        // Ensure lore does not exceed 256 lines
        if (lore.size() > 256) {
            lore = lore.subList(0, 256);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String guiTitle = FXItems.getFxItemsGuiTitle();
        if (!event.getView().getTitle().equals(guiTitle)) return;

        if (event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

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
        event.setCancelled(true);

        // Check custom items
        for (String itemId : ItemRegistry.getItemIds()) {
            ItemStack registeredItem = ItemRegistry.getCustomItem(itemId);
            if (registeredItem != null && CustomItemUtils.isCustomItem(clickedItem, registeredItem.getType(), registeredItem.getItemMeta().getDisplayName())) {
                try {
                    new CraftingGUI(plugin).openCraftingMenu(player, itemId);
                } catch (RecipeException e) {}
                return;
            }
        }
        // Check custom foods
        for (String foodId : FoodRegistry.getFoodIds()) {
            ItemStack registeredFood = FoodRegistry.getFoodItem(foodId);
            if (registeredFood != null && CustomItemUtils.isCustomItem(clickedItem, registeredFood.getType(), registeredFood.getItemMeta().getDisplayName())) {
                try {
                    new CraftingGUI(plugin).openFoodCraftingMenu(player, foodId);
                } catch (RecipeException e) {}
                return;
            }
        }
        // Check custom armors
        for (String armorId : com.noctify.Custom.ArmorRegistry.getArmorIds()) {
            ItemStack registeredArmor = com.noctify.Custom.ArmorRegistry.getCustomArmor(armorId);
            if (registeredArmor != null && CustomItemUtils.isCustomItem(clickedItem, registeredArmor.getType(), registeredArmor.getItemMeta().getDisplayName())) {
                try {
                    new CraftingGUI(plugin).openArmorCraftingMenu(player, armorId);
                } catch (RecipeException e) {}
                return;
            }
        }
        player.sendMessage("§cError: Item not found in the registry.");
    }
}