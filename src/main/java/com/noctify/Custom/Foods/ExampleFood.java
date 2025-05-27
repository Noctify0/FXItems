package com.noctify.Custom.Foods;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExampleFood {

    // Creates the custom food item
    // Check the OtherBehaviors package to see how to implement custom behaviors

    public static ItemStack createItem() {
        ItemStack foodItem = new ItemStack(Material.COOKED_BEEF); // Example material
        ItemMeta meta = foodItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Sample Food");
            meta.setLore(Arrays.asList(
                    "§7This is a sample custom food.",
                    "§7Use this as a template for creating your own."
            ));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            foodItem.setItemMeta(meta);
        }
        return foodItem;
    }

    // Defines the hunger points restored by the food
    public static int getHungerPoints() {
        return 4; // Example value for hunger points
    }

    // Defines the saturation points restored by the food
    public static float getSaturationPoints() {
        return 2.5f; // Example value for saturation
    }

    // Defines any potion effects applied when the food is consumed
    public static List<PotionEffect> getEffects() {
        return Collections.singletonList(
                new PotionEffect(PotionEffectType.SPEED, 200, 1) // Example effect: Speed for 10 seconds
        );
    }

    // If don't want any effects, return an empty list
    //
    // Example:
    //
    // public static List<PotionEffect> getEffects() {
    //     return Collections.emptyList();
    // }

    // Defines the crafting recipe for the food
    public static ShapedRecipe getRecipe(Plugin plugin, NamespacedKey key) {
        ItemStack foodItem = createItem();
        ShapedRecipe recipe = new ShapedRecipe(key, foodItem);
        recipe.shape(" A ", " B ", " C ");
        recipe.setIngredient('A', Material.APPLE);
        recipe.setIngredient('B', Material.BREAD);
        recipe.setIngredient('C', Material.COOKED_BEEF);
        return recipe;
    }
}