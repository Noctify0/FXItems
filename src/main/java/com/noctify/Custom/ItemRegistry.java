package com.noctify.Custom;

import com.noctify.Custom.ItemAttributes.*;
import com.noctify.Custom.ItemBehavior.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemRegistry {

    public static void initialize(Plugin plugin) {

        // --------------------------------------------------------------------------
        // Register Custom items
        //
        // For these to work you have to first create a class for each item
        // in com.noctify.Custom.ItemAttributes package
        // A sample ItemAttribute class will be provided in the package
        //
        // Sample ItemRegistry Implementation:
        // registerItem("item_name", NameOfItemAttributesClass.class);
        //



        // --------------------------------------------------------------------------


        // --------------------------------------------------------------------------
        // Register your items behaviors
        // Make sure you create an Item Behavior Class in com.noctify.Custom.ItemBehavior
        // A sample will be provided in the package
        //
        // Sample Implementation:
        // registerBehavior(plugin, "item_name", NameOfItemBehaviorClass.class);
        //



        // --------------------------------------------------------------------------


        // --------------------------------------------------------------------------
        // Add your custom items recipes to the game
        //
        // Make sure your ItemClass has a ShapedRecipe call inside it
        // A sample recipe call looks like this:
        //
        // -------------------------------------------------------------------------------------------
        //
        // public static ShapedRecipe getRecipe(Plugin plugin, NamespacedKey key) {
        //        ItemStack defaultPlayerHead = new ItemStack(Material.PLAYER_HEAD);
        //
        //        ItemStack heartBlade = createItem();
        //        ShapedRecipe recipe = new ShapedRecipe(key, heartBlade);
        //        recipe.shape("PRP", "ANA", "PRP");
        //        recipe.setIngredient('P', new RecipeChoice.ExactChoice(PlayerHead.createItem()));
        //        recipe.setIngredient('A', Material.GOLDEN_APPLE);
        //        recipe.setIngredient('N', Material.NETHERITE_SWORD);
        //        recipe.setIngredient('R', Material.RED_DYE);
        //
        //        // Use the plugin instance to register the item as one-time craftable
        //        OneTimeCraftUtils utils = new OneTimeCraftUtils(plugin);
        //        utils.registerCraft("heart_blade", heartBlade);
        //
        //        return recipe;
        // }
        //
        // -------------------------------------------------------------------------------------------
        //
        // Sample Implementation:
        // addRecipe(plugin, ItemName.class);
        //



        // --------------------------------------------------------------------------
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

    public static Set<String> getItemIds() {
        return FXItems.keySet();
    }

    public static ItemStack getCustomItem(String id) {
        return FXItems.get(id.toLowerCase());
    }

    // Add this method to the ItemRegistry class
    public static void addRecipe(Plugin plugin, Class<?> attributesClass) {
        try {
            String itemName = attributesClass.getSimpleName().toLowerCase();
            NamespacedKey keyBase = NamespacedKey.minecraft(itemName);
            java.lang.reflect.Method[] methods = attributesClass.getDeclaredMethods();
            int recipeIndex = 1;

            for (java.lang.reflect.Method method : methods) {
                if (method.getReturnType() == ShapedRecipe.class &&
                        method.getParameterCount() == 2 &&
                        method.getParameterTypes()[0] == Plugin.class &&
                        method.getParameterTypes()[1] == NamespacedKey.class) {

                    NamespacedKey key = new NamespacedKey(keyBase.getNamespace(), keyBase.getKey() + "_" + recipeIndex);
                    Object recipe = method.invoke(null, plugin, key);
                    Bukkit.addRecipe((org.bukkit.inventory.Recipe) recipe);

                    Bukkit.broadcastMessage("§aSuccessfully loaded recipe: " + key.getKey());
                    recipeIndex++;
                }
            }
        } catch (Exception e) {
            Bukkit.broadcastMessage("§cFailed to add recipe for attributes class: " + attributesClass.getName());
            e.printStackTrace();
        }
    }

    public static void registerBehavior(Plugin plugin, String itemName, Class<?> behaviorClass) {
        try {
            Object behaviorInstance = behaviorClass.getConstructor(Plugin.class).newInstance(plugin);
            Bukkit.getPluginManager().registerEvents((org.bukkit.event.Listener) behaviorInstance, plugin);

            Bukkit.broadcastMessage("§aSuccessfully loaded behavior for item: " + itemName);
        } catch (Exception e) {
            Bukkit.broadcastMessage("§cFailed to register behavior for item: " + itemName);
            e.printStackTrace();
        }
    }

    public static void registerItem(String itemName, Class<?> itemClass) {
        try {
            // Use reflection to invoke the createItem method
            ItemStack item = (ItemStack) itemClass.getMethod("createItem").invoke(null);
            FXItems.put(itemName.toLowerCase(), item);
            Bukkit.broadcastMessage("§aSuccessfully registered item: " + itemName);
        } catch (Exception e) {
            Bukkit.broadcastMessage("§cFailed to register item: " + itemName);
            e.printStackTrace();
        }
    }

    private static final Map<String, ItemStack> FXItems = new HashMap<>();

    // Utility method to capitalize the first letter of the item name
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}