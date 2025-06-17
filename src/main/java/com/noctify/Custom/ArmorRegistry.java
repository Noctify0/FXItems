package com.noctify.Custom;

import com.noctify.Main.Registration.*;
import com.noctify.Custom.ArmorBehavior.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ArmorRegistry {

    private static final Map<String, FXArmorDefinition> armors = new HashMap<>();

    public static void initialize(Plugin plugin) {
        // Sample recipe: diamond helmet with emeralds
        registerArmor("example_helmet", new FXArmorDefinition(
                "example_helmet",
                "§aExample Helmet",
                ArmorType.HELMET,
                ArmorMaterial.DIAMOND,
                3,
                null,
                400,
                new ExampleArmorBehavior(),
                true,
                false,
                true,
                Arrays.asList("§7A legendary helmet.", "§fSpecial Ability: Example!"),
                new FXItemRecipe(
                        Arrays.asList(
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.EMERALD),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.DIAMOND_BLOCK),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.DIAMOND_BLOCK),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.AIR)
                        ),
                        true,
                        1,
                        true
                )
        ));
    }

    public static void registerArmor(String id, FXArmorDefinition def) {
        armors.put(id.toLowerCase(), def);
        Bukkit.getLogger().info("[FXItems] Registered armor: " + def.id);

        if (def.recipe != null) {
            NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugins()[0], def.id.toLowerCase());
            ShapedRecipe recipe = new ShapedRecipe(key, createItemStackFromDefinition(def));
            recipe.shape("ABC", "DEF", "GHI");
            char[] chars = {'A','B','C','D','E','F','G','H','I'};
            for (int i = 0; i < 9; i++) {
                ItemStack ingredient = def.recipe.ingredients.get(i);
                if (ingredient != null && ingredient.getType() != Material.AIR) {
                    recipe.setIngredient(chars[i], ingredient.getType());
                }
            }
            Bukkit.addRecipe(recipe);
            Bukkit.getLogger().info("[FXItems] Registered recipe for: " + def.id);
        }
    }

    public static FXArmorDefinition getArmorDefinition(String id) {
        return armors.get(id.toLowerCase());
    }

    public static ItemStack getCustomArmor(String id) {
        FXArmorDefinition def = getArmorDefinition(id);
        if (def == null) return null;
        return createItemStackFromDefinition(def);
    }

    public static FXArmorDefinition getDefinition(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        Integer cmd = item.getItemMeta().hasCustomModelData() ? item.getItemMeta().getCustomModelData() : null;
        for (FXArmorDefinition def : armors.values()) {
            if (def.customModelData != null && def.customModelData.equals(cmd)) {
                return def;
            }
        }
        return null;
    }

    private static ItemStack createItemStackFromDefinition(FXArmorDefinition def) {
        Material mat = getBukkitMaterial(def.type, def.material);
        ItemStack item = new ItemStack(mat, 1); // Always 1, or set as needed
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(def.displayName);
            if (def.lore != null) meta.setLore(def.lore);
            if (def.customModelData != null) meta.setCustomModelData(def.customModelData);
            if (def.unbreakable) meta.setUnbreakable(true);
            // Optionally hide flags, etc.
            item.setItemMeta(meta);
        }
        return item;
    }

    public static Set<String> getArmorIds() {
        return armors.keySet();
    }

    private static Material getBukkitMaterial(ArmorType type, ArmorMaterial mat) {
        switch (type) {
            case HELMET:
                switch (mat) {
                    case LEATHER: return Material.LEATHER_HELMET;
                    case CHAINMAIL: return Material.CHAINMAIL_HELMET;
                    case GOLD: return Material.GOLDEN_HELMET;
                    case IRON: return Material.IRON_HELMET;
                    case DIAMOND: return Material.DIAMOND_HELMET;
                    case NETHERITE: return Material.NETHERITE_HELMET;
                }
                break;
            case CHESTPLATE:
                switch (mat) {
                    case LEATHER: return Material.LEATHER_CHESTPLATE;
                    case CHAINMAIL: return Material.CHAINMAIL_CHESTPLATE;
                    case GOLD: return Material.GOLDEN_CHESTPLATE;
                    case IRON: return Material.IRON_CHESTPLATE;
                    case DIAMOND: return Material.DIAMOND_CHESTPLATE;
                    case NETHERITE: return Material.NETHERITE_CHESTPLATE;
                }
                break;
            case LEGGINGS:
                switch (mat) {
                    case LEATHER: return Material.LEATHER_LEGGINGS;
                    case CHAINMAIL: return Material.CHAINMAIL_LEGGINGS;
                    case GOLD: return Material.GOLDEN_LEGGINGS;
                    case IRON: return Material.IRON_LEGGINGS;
                    case DIAMOND: return Material.DIAMOND_LEGGINGS;
                    case NETHERITE: return Material.NETHERITE_LEGGINGS;
                }
                break;
            case BOOTS:
                switch (mat) {
                    case LEATHER: return Material.LEATHER_BOOTS;
                    case CHAINMAIL: return Material.CHAINMAIL_BOOTS;
                    case GOLD: return Material.GOLDEN_BOOTS;
                    case IRON: return Material.IRON_BOOTS;
                    case DIAMOND: return Material.DIAMOND_BOOTS;
                    case NETHERITE: return Material.NETHERITE_BOOTS;
                }
                break;
        }
        return Material.LEATHER_HELMET;
    }
}