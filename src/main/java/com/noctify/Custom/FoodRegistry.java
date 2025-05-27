package com.noctify.Custom;

import com.noctify.Custom.Foods.*;
import com.noctify.Custom.OtherBehaviors.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

public class FoodRegistry {

    public static void initialize(Plugin plugin) {

        // Register custom foods
        // For these to work you have to first create a class for each food
        // If you want custom behavior for the food, create a class in com.noctify.Custom.OtherBehaviors
        // Example classes will be provided in the packages
        //
        // Sample FoodRegistry Implementation:
        // registerFood(plugin, "food_name", ExampleFood.class, ExampleFoodBehavior.class);
        //

        registerFood(plugin, "ExampleFood", ExampleFood.class, ExampleFoodBehavior.class);

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

    public static void registerFood(Plugin plugin, String foodName, Class<?> foodClass, Class<?> behaviorClass) {
        try {
            // Extract attributes from the food class
            Method createItem = foodClass.getMethod("createItem");
            Method getHungerPoints = foodClass.getMethod("getHungerPoints");
            Method getSaturationPoints = foodClass.getMethod("getSaturationPoints");
            Method getEffects = foodClass.getMethod("getEffects");
            Method getRecipe = foodClass.getMethod("getRecipe", Plugin.class, NamespacedKey.class);

            ItemStack foodItem = (ItemStack) createItem.invoke(null);
            int hungerPoints = (int) getHungerPoints.invoke(null);
            float saturationPoints = (float) getSaturationPoints.invoke(null);
            List<PotionEffect> effects = (List<PotionEffect>) getEffects.invoke(null);

            NamespacedKey key = new NamespacedKey(plugin, foodName.toLowerCase());
            ShapedRecipe recipe = (ShapedRecipe) getRecipe.invoke(null, plugin, key);

            // Register crafting recipe
            Bukkit.addRecipe(recipe);

            // Instantiate custom behavior if provided
            final Consumer<Player> customBehavior;
            if (behaviorClass != null) {
                customBehavior = (Consumer<Player>) behaviorClass.getConstructor().newInstance();
            } else {
                customBehavior = null;
            }

            // Register food behavior
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onPlayerConsume(PlayerItemConsumeEvent event) {
                    Player player = event.getPlayer();
                    ItemStack consumedItem = event.getItem();

                    if (consumedItem.isSimilar(foodItem)) {
                        // Apply effects
                        if (effects != null) {
                            for (PotionEffect effect : effects) {
                                player.addPotionEffect(effect);
                            }
                        }

                        // Restore hunger and saturation
                        player.setFoodLevel(Math.min(20, player.getFoodLevel() + hungerPoints));
                        player.setSaturation(Math.min(20, player.getSaturation() + saturationPoints));

                        // Execute custom behavior if provided
                        if (customBehavior != null) {
                            customBehavior.accept(player);
                        }
                    }
                }
            }, plugin);

            // Log success message
            Bukkit.broadcastMessage("§aSuccessfully registered food: " + foodName);
        } catch (Exception e) {
            // Log error message
            Bukkit.broadcastMessage("§cFailed to register food: " + foodName);
            e.printStackTrace();
        }
    }
}