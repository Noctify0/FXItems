package com.noctify.Main.GUIs;

import com.noctify.Custom.ItemRegistry;
import com.noctify.Main.Utils.CustomItemUtils;
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

        // Add registered items to the menu
        int index = 10; // Start from the second row
        for (Class<?> itemClass : ItemRegistry.getRegisteredItemClasses()) {
            try {
                // Get the item from the attributes class
                Method createItemMethod = itemClass.getMethod("createItem");
                ItemStack item = (ItemStack) createItemMethod.invoke(null);

                // Add lore to the item
                addLoreToItem(item, player);

                // Add the item to the menu
                menu.setItem(index, item);
                index++;
                if (index % 9 == 8) index += 2; // Skip edges
            } catch (Exception e) {
                Bukkit.getLogger().warning("[FXItems] Failed to load item from class: " + itemClass.getName());
                e.printStackTrace();
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
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.GRAY_STAINED_GLASS_PANE) {
                Player player = (Player) event.getWhoClicked();
                ItemStack clickedItem = event.getCurrentItem();

                if (event.isLeftClick()) {
                    boolean found = false;
                    try {
                        for (Class<?> itemClass : ItemRegistry.getRegisteredItemClasses()) {
                            Method createItemMethod = itemClass.getMethod("createItem");
                            ItemStack registeredItem = (ItemStack) createItemMethod.invoke(null);

                            ItemMeta regMeta = registeredItem.getItemMeta();
                            if (regMeta != null && regMeta.hasDisplayName()) {
                                if (CustomItemUtils.isCustomItem(clickedItem, registeredItem.getType(), regMeta.getDisplayName())) {
                                    // Use the same key for both recipe and GUI
                                    String keyName = itemClass.getSimpleName();
                                    Method recipeMethod = itemClass.getMethod("getRecipe", Plugin.class, org.bukkit.NamespacedKey.class);
                                    org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey(plugin, keyName + "_recipe");
                                    recipeMethod.invoke(null, plugin, key);
                                    new CraftingGUI(plugin).openCraftingMenu(player, keyName);
                                    found = true;
                                    break;
                                }
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        player.sendMessage("§cError: Recipe method not found for the item.");
                        Bukkit.getLogger().severe("[FXItems] Method 'getRecipe' not found in class.");
                        e.printStackTrace();
                    } catch (Exception e) {
                        player.sendMessage("§cError: Failed to open recipe.");
                        Bukkit.getLogger().severe("[FXItems] Failed to invoke 'getRecipe' method.");
                        e.printStackTrace();
                    }
                    if (!found) {
                        player.sendMessage("§cError: Item not found in the registry.");
                    }
                }
            }
        }
    }
}