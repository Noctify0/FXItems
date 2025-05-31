package com.noctify.Custom;

import com.noctify.Custom.OtherBehaviors.ExampleFoodBehavior;
import com.noctify.Main.Registration.FXFoodDefinition;
import com.noctify.Main.Registration.FXFoodBehavior;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class FoodRegistry implements Listener {

    public static void initialize(Plugin plugin) {

        // Register custom foods
        // For these to work you have to first create a class for each food
        // If you want custom behavior for the food, create a class in com.noctify.Custom.OtherBehaviors
        // Example classes will be provided in the packages
        //
        // Sample FoodRegistry Implementation:
        // registerFood(plugin, "food_name", ExampleFood.class, ExampleFoodBehavior.class);
        //

        registerFood(
                "example_food",
                new FXFoodDefinition(
                        "§6Sample Food",
                        4,
                        2.5f,
                        Collections.singletonList(
                                new PotionEffect(PotionEffectType.SPEED, 200, 1)
                        ),
                        Arrays.asList(
                                "§7This is a sample custom food.",
                                "§7Use this as a template for creating your own."
                        ), // lore
                        Arrays.asList(
                                new ItemStack(Material.APPLE),        // Slot 0 (top-left)
                                null,                                 // Slot 1
                                null,                                 // Slot 2
                                null,                                 // Slot 3
                                new ItemStack(Material.BREAD),        // Slot 4 (center)
                                null,                                 // Slot 5
                                null,                                 // Slot 6
                                null,                                 // Slot 7
                                new ItemStack(Material.COOKED_BEEF)   // Slot 8 (bottom-right)
                        ),
                        1001,
                        "sample_food"
                ),
                new ExampleFoodBehavior() // Use the existing behavior file
        );

    }

    //
    // ----------------------------------------------------------------------------------------|
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    // DO NOT edit anything below.                                                             |
    // Unless you absolutely know what you are doing.                                          |
    //                                                                                         |
    //                                                                           ?              |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    // ----------------------------------------------------------------------------------------|
    //

    private static final Map<String, ItemStack> foodItems = new HashMap<>();
    private static final Map<String, FXFoodDefinition> foodDefinitions = new HashMap<>();
    private static final Map<String, FXFoodBehavior> foodBehaviors = new HashMap<>();

    public static void registerFood(String id, FXFoodDefinition definition, FXFoodBehavior behavior) {
        // Build the ItemStack from the definition
        ItemStack item = new ItemStack(Material.COOKED_BEEF); // Or use a field in FXFoodDefinition for material
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(definition.displayName);
            meta.setLore(definition.lore);
            meta.setCustomModelData(definition.customModelData);
            item.setItemMeta(meta);
        }
        // Call custom item stack creation logic
        if (behavior != null) {
            behavior.onItemStackCreate(item);
        }
        foodItems.put(id, item);
        foodDefinitions.put(id, definition);
        if (behavior != null) foodBehaviors.put(id, behavior);

        // Register the recipe if present
        if (definition.recipe != null && definition.recipe.size() == 9) {
            org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey(
                    Bukkit.getPluginManager().getPlugins()[0], id.toLowerCase()
            );
            org.bukkit.inventory.ShapedRecipe recipe = new org.bukkit.inventory.ShapedRecipe(key, item);
            recipe.shape("ABC", "DEF", "GHI");
            char[] chars = {'A','B','C','D','E','F','G','H','I'};
            for (int i = 0; i < 9; i++) {
                ItemStack ingredient = definition.recipe.get(i);
                if (ingredient != null && ingredient.getType() != Material.AIR) {
                    recipe.setIngredient(chars[i], ingredient.getType());
                }
            }
            Bukkit.addRecipe(recipe);
            Bukkit.getLogger().info("[FXItems] Registered food recipe for: " + id);
        }
    }

    public static ItemStack getFoodItem(String id) {
        return foodItems.get(id);
    }

    public static FXFoodDefinition getFoodDefinition(String id) {
        return foodDefinitions.get(id);
    }

    public static FXFoodBehavior getFoodBehavior(String id) {
        return foodBehaviors.get(id);
    }

    public static Set<String> getFoodIds() {
        return foodItems.keySet();
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack consumed = event.getItem();
        for (String id : foodItems.keySet()) {
            ItemStack reg = foodItems.get(id);
            if (reg != null && consumed.isSimilar(reg)) {
                FXFoodDefinition def = foodDefinitions.get(id);
                FXFoodBehavior behavior = foodBehaviors.get(id);

                boolean cancel = false;
                if (behavior != null) {
                    cancel = behavior.beforeConsume(player, event, consumed);
                }
                if (cancel) {
                    event.setCancelled(true);
                    return;
                }

                if (def != null) {
                    player.setFoodLevel(Math.min(20, player.getFoodLevel() + def.hungerPoints));
                    player.setSaturation(Math.min(20, player.getSaturation() + def.saturationPoints));
                    if (def.effects != null) {
                        for (PotionEffect effect : def.effects) {
                            player.addPotionEffect(effect);
                        }
                    }
                }

                if (behavior != null) {
                    behavior.afterConsume(player, consumed);
                }
                break;
            }
        }
    }
}