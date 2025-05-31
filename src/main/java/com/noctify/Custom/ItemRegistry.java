package com.noctify.Custom;

import com.noctify.Main.Registration.FXItemDefinition;
import com.noctify.Main.Utils.OneTimeCraftUtils;
import com.noctify.Main.Registration.FXItemRecipe;
import com.noctify.Custom.ItemBehavior.ExampleItemBehavior;
import com.noctify.Main.Registration.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.*;

public class ItemRegistry implements Listener {

    // Map for item key to class
    private static final Map<String, Class<?>> itemClassMap = new HashMap<>();

    //
    // New method to initialize the item registry
    // No need for a separate ItemRegistry class now
    //
    // Sample item initialization method:
    //
    // registerItem("example_sword", new FXItemDefinition(
    //                "example_sword", // item ID
    //                "§aExample Sword", // display name
    //                Material.DIAMOND_SWORD, // material type
    //                new ExampleItemBehavior(), // custom behavior class
    //                null, // custom model data (null for none)
    //                true, // one-time craft
    //                true, // set unbreakable
    //                true, // hide flags: enchantments, attributes, etc.
    //                Arrays.asList( // List of lore lines
    //                        "§7A sword for demonstration purposes.",
    //                        "",
    //                        "§6ʟᴇɢᴇɴᴅᴀʀʏ",
    //                        "§fAbilities:",
    //                        "§fExample Power: §7Right Click to activate.",
    //                        "§830s cooldown"
    //                ), // Recipe for crafting the item
    //                new FXItemRecipe(Arrays.asList(
    //                        new ItemStack(Material.AIR),
    //                        new ItemStack(Material.EMERALD),
    //                        new ItemStack(Material.AIR),
    //                        new ItemStack(Material.DIAMOND_BLOCK),
    //                        new ItemStack(Material.STICK),
    //                        new ItemStack(Material.DIAMOND_BLOCK),
    //                        new ItemStack(Material.AIR),
    //                        new ItemStack(Material.STICK),
    //                        new ItemStack(Material.AIR)
    //                ), true, 1, true)
    //        ));
    //
    // If you want to add a custom item to the recipe
    // Use this line:
    //
    // new ItemRegistry().getCustomItem("example_item_id")
    //
    // Instead of: (for example)
    //
    // new ItemStack(Material.EMERALD),
    //

    public static void initialize(Plugin plugin) {
        registerItem("example_sword", new FXItemDefinition(
                "example_sword",
                "§aExample Sword",
                Material.DIAMOND_SWORD,
                Rarity.LEGENDARY,
                new ExampleItemBehavior(),
                null,
                true,
                true,
                true,
                Arrays.asList(
                        "§7A sword for demonstration purposes.",
                        "",
                        "§fAbilities:",
                        "§fExample Power: §7Right Click to activate.",
                        "§830s cooldown"
                ),
                new FXItemRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.EMERALD),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.DIAMOND_BLOCK),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.DIAMOND_BLOCK),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.AIR)
                ), true, 1, true)
        ));
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

    public static void registerItem(String id, FXItemDefinition def) {
        items.put(id.toLowerCase(), def);
        Bukkit.getLogger().info("[FXItems] Registered item: " + def.id);

        // Register one-time craft items
        if (def.oneTimeCraft && oneTimeCraftUtils != null) {
            oneTimeCraftUtils.registerCraft(id, getCustomItem(id));
        }

        if (def.behavior != null) {
            registerItemClass(id, def.behavior.getClass());
        }

        if (def.recipe != null) {
            for (ItemStack ingredient : def.recipe.ingredients) {
                if (ingredient != null && ingredient.hasItemMeta() && ingredient.getItemMeta().hasDisplayName()) {
                    for (String customId : items.keySet()) {
                        FXItemDefinition customDef = items.get(customId);
                        if (customDef != null &&
                                customDef.displayName.equals(ingredient.getItemMeta().getDisplayName()) &&
                                customDef.material == ingredient.getType()) {
                            if (!Bukkit.getRecipesFor(new ItemStack(customDef.material)).stream()
                                    .anyMatch(r -> r.getResult().getItemMeta() != null &&
                                            r.getResult().getItemMeta().getDisplayName().equals(customDef.displayName))) {
                                registerItem(customId, customDef);
                            }
                        }
                    }
                }
            }

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

    public static FXItemRecipe getRecipe(String id) {
        FXItemDefinition def = getItemDefinition(id);
        return (def != null) ? def.recipe : null;
    }

    public static ItemStack getCustomItem(String id) {
        FXItemDefinition def = getItemDefinition(id);
        if (def == null) return null;
        return createItemStackFromDefinition(def);
    }

    private static final Map<String, FXItemDefinition> items = new HashMap<>();

    public static Set<String> getItemIds() {
        return items.keySet();
    }

    private static final List<Class<?>> registeredItemClasses = new ArrayList<>();

    public static Collection<Class<?>> getRegisteredItemClasses() {
        return registeredItemClasses;
    }

    public static void registerItemClass(Class<?> itemClass) {
        registeredItemClasses.add(itemClass);
    }

    public static FXItemDefinition getItemDefinition(String id) {
        return items.get(id.toLowerCase());
    }

    private static ItemStack createItemStackFromDefinition(FXItemDefinition def) {
        ItemStack item = new ItemStack(def.material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(def.displayName);
            List<String> lore = new ArrayList<>();
            if (def.lore != null) {
                lore.addAll(def.lore);
            }
            if (def.rarity != null && rarityLoreMap.containsKey(def.rarity)) {
                lore.add("");
                lore.add(rarityLoreMap.get(def.rarity));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static void registerItemClass(String key, Class<?> clazz) {
        itemClassMap.put(key.toLowerCase(), clazz);
        registeredItemClasses.add(clazz);
    }

    public static void registerBehavior(org.bukkit.plugin.Plugin plugin, String itemName, Class<?> behaviorClass) {
        // No-op or implement if needed
    }

    public static Class<?> getItemClass(String key) {
        return itemClassMap.get(key.toLowerCase());
    }

    public static FXItemDefinition getDefinition(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return null;
        String displayName = item.getItemMeta().getDisplayName();
        for (FXItemDefinition def : items.values()) {
            if (def.displayName.equals(displayName) && def.material == item.getType()) {
                return def;
            }
        }
        return null;
    }

    private static Map<Rarity, String> rarityLoreMap = new HashMap<>();

    public static void loadRarityLang(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "RarityLang.yml");
        if (!file.exists()) {
            plugin.saveResource("RarityLang.yml", false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Rarity rarity : Rarity.values()) {
            String key = rarity.name().toLowerCase();
            String lore = config.getString(key, "§7" + rarity.name());
            rarityLoreMap.put(rarity, lore);
        }
    }

    public static void initOneTimeCraftUtils(Plugin plugin) {
        oneTimeCraftUtils = new com.noctify.Main.Utils.OneTimeCraftUtils(plugin);
    }

    private static com.noctify.Main.Utils.OneTimeCraftUtils oneTimeCraftUtils;
}