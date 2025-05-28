package com.noctify.Custom.ItemAttributes;

import com.noctify.Main.Utils.OneTimeCraftUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class ExampleItem {

    public static ItemStack createItem() {

        // Create the item name, lore and properties

        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Set the display name
            meta.setDisplayName("§aExample Sword");
            // Set the lore
            meta.setLore(Arrays.asList(
                    "§7A sword for demonstration purposes.",
                    "",
                    "§6ʟᴇɢᴇɴᴅᴀʀʏ",
                    "§fAbilities:",
                    "§fExample Power: §7Right Click to activate.",
                    "§830s cooldown"
            ));
            // Set the item to be unbreakable
            meta.setUnbreakable(true);
            // Hide the unbreakable flag
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

            // Set a custom item model for the item
            NamespacedKey modelKey = NamespacedKey.minecraft("example_sword");
            meta.setItemModel(modelKey);
            // Apply the custom attributes to the item
            item.setItemMeta(meta);
        }
        return item;
    }

    // Create a crafting recipe for the item
    public static ShapedRecipe getRecipe(Plugin plugin, NamespacedKey key) {
        ItemStack exampleItem = createItem();
        ShapedRecipe recipe = new ShapedRecipe(key, exampleItem);
        // Define the shape of the recipe
        recipe.shape(" E ", "DSD", " S ");
        // Set the ingredients for the recipe
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('S', Material.STICK);

        // If you want the item to only be able to be crafted once, you can use the OneTimeCraftUtils
        // This can especially be useful in competetive smp servers where you want to limit the number of powerful items
        // Make sure to import the OneTimeCraftUtils class from the Main.Utils package
        // import com.noctify.Main.Utils.OneTimeCraftUtils;
        //
        // Register the item as a one-time craftable item syntax:
        // It does this will the following two lines:
        //
        // OneTimeCraftUtils utils = new OneTimeCraftUtils(plugin);
        // utils.registerCraft("example_item", exampleItem);

        OneTimeCraftUtils utils = new OneTimeCraftUtils(plugin);
        utils.registerCraft("example_item", exampleItem);

        return recipe;
    }
}