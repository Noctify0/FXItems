package com.noctify.Main.GUIs;

import com.noctify.Custom.FoodRegistry;
import com.noctify.Custom.ItemRegistry;
import com.noctify.Main.Registration.FXFoodDefinition;
import com.noctify.Main.Registration.FXItemRecipe;
import com.noctify.Main.Exceptions.RecipeException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CraftingGUI implements Listener {

    private final JavaPlugin plugin;

    public CraftingGUI(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openCraftingMenu(Player player, String itemId) throws RecipeException {
        FXItemRecipe recipe = ItemRegistry.getRecipe(itemId);
        if (recipe == null) { throw new RecipeException(player, itemId); }

        // Get display name from item definition, fallback to itemId if not found
        String displayName = itemId;
        var def = ItemRegistry.getItemDefinition(itemId);
        if (def != null && def.displayName != null) {
            displayName = def.displayName;
        }

        // Strip color codes from display name
        displayName = ChatColor.stripColor(displayName);

        // Always use gray and bold for the title
        Inventory menu = Bukkit.createInventory(null, 45, "§8§l" + displayName + "§8§l's Recipe");

        // Fill all slots with glass panes
        ItemStack glassPane = createGlassPane();
        for (int i = 0; i < 45; i++) {
            menu.setItem(i, glassPane);
        }

        // Custom crafting grid layout
        int[] craftingSlots = {11, 12, 13, 20, 21, 22, 29, 30, 31};
        List<ItemStack> ingredients = recipe.ingredients;
        for (int i = 0; i < Math.min(9, ingredients.size()); i++) {
            ItemStack ingredient = ingredients.get(i);
            if (ingredient != null && ingredient.getType() != Material.AIR) {
                menu.setItem(craftingSlots[i], ingredient.clone());
            } else {
                menu.setItem(craftingSlots[i], null);
            }
        }

        // Output slot (slot 24)
        ItemStack result = ItemRegistry.getCustomItem(itemId);
        if (result != null) {
            result = result.clone();
            result.setAmount(recipe.amount);
        }

        // Check if the item is globally crafted (one-time craft)
        String craftedKey = itemId.toLowerCase() + "_crafted";
        boolean crafted = plugin.getConfig().getBoolean(craftedKey);

        if (crafted && recipe.isOneTimeCraft()) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta meta = barrier.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§cItem Already Crafted");
                meta.setLore(java.util.List.of("§7This legendary item can only be crafted once."));
                barrier.setItemMeta(meta);
            }
            menu.setItem(24, barrier);
        } else if (result != null) {
            menu.setItem(24, result);
        }

        // Add Close arrow in slot 44
        ItemStack closeArrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = closeArrow.getItemMeta();
        if (arrowMeta != null) {
            arrowMeta.setDisplayName("§c§lBack");
            closeArrow.setItemMeta(arrowMeta);
        }
        menu.setItem(44, closeArrow);

        player.openInventory(menu);
    }

    public void openFoodCraftingMenu(Player player, String foodId) throws RecipeException {
        FXFoodDefinition def = FoodRegistry.getFoodDefinition(foodId);
        if (def == null) { throw new RecipeException(player, foodId); }

        String displayName = def.displayName != null ? def.displayName : foodId;
        displayName = org.bukkit.ChatColor.stripColor(displayName);

        Inventory menu = Bukkit.createInventory(null, 45, "§8§l" + displayName + "§8§l's Recipe");

        ItemStack glassPane = createGlassPane();
        for (int i = 0; i < 45; i++) {
            menu.setItem(i, glassPane);
        }

        int[] craftingSlots = {11, 12, 13, 20, 21, 22, 29, 30, 31};
        for (int i = 0; i < Math.min(9, def.recipe.size()); i++) {
            ItemStack ingredient = def.recipe.get(i);
            if (ingredient != null && ingredient.getType() != Material.AIR) {
                menu.setItem(craftingSlots[i], ingredient.clone());
            } else {
                menu.setItem(craftingSlots[i], null);
            }
        }

        ItemStack result = FoodRegistry.getFoodItem(foodId);
        if (result != null) {
            result = result.clone();
            result.setAmount(1);
        }
        menu.setItem(24, result);

        ItemStack closeArrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = closeArrow.getItemMeta();
        if (arrowMeta != null) {
            arrowMeta.setDisplayName("§c§lBack");
            closeArrow.setItemMeta(arrowMeta);
        }
        menu.setItem(44, closeArrow);

        player.openInventory(menu);
    }

    public void openArmorCraftingMenu(Player player, String armorId) throws RecipeException {
        var def = com.noctify.Custom.ArmorRegistry.getArmorDefinition(armorId);
        if (def == null || def.recipe == null) {
            throw new RecipeException(player, "No recipe found for armor: " + armorId);
        }

        String displayName = def.displayName != null ? def.displayName : armorId;
        displayName = org.bukkit.ChatColor.stripColor(displayName);

        Inventory menu = Bukkit.createInventory(null, 45, "§8§l" + displayName + "§8§l's Recipe");

        ItemStack glassPane = createGlassPane();
        for (int i = 0; i < 45; i++) {
            menu.setItem(i, glassPane);
        }

        int[] craftingSlots = {11, 12, 13, 20, 21, 22, 29, 30, 31};
        for (int i = 0; i < Math.min(9, def.recipe.ingredients.size()); i++) {
            ItemStack ingredient = def.recipe.ingredients.get(i);
            if (ingredient != null && ingredient.getType() != Material.AIR) {
                menu.setItem(craftingSlots[i], ingredient.clone());
            } else {
                menu.setItem(craftingSlots[i], null);
            }
        }

        ItemStack result = com.noctify.Custom.ArmorRegistry.getCustomArmor(armorId);
        if (result != null) {
            result = result.clone();
            result.setAmount(def.recipe.amount);
        }
        menu.setItem(24, result);

        ItemStack closeArrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = closeArrow.getItemMeta();
        if (arrowMeta != null) {
            arrowMeta.setDisplayName("§c§lBack");
            closeArrow.setItemMeta(arrowMeta);
        }
        menu.setItem(44, closeArrow);

        player.openInventory(menu);
    }

    private ItemStack createGlassPane() {
        ItemStack glassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = glassPane.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            glassPane.setItemMeta(meta);
        }
        return glassPane;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().endsWith("'s Recipe")) {
            event.setCancelled(true);

            // Handle Close arrow click
            if (event.getRawSlot() == 44) {
                Player player = (Player) event.getWhoClicked();
                new FXItem(plugin).openMainMenu(player);
            }
        }
    }
}