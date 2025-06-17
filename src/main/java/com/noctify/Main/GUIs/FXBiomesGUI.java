package com.noctify.Main.GUIs;

import com.noctify.Custom.BiomeRegistry;
import com.noctify.Main.Registration.BiomeType;
import com.noctify.Main.Registration.FXBiomeDefinition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class FXBiomesGUI {
    private final JavaPlugin plugin;

    public FXBiomesGUI(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 45, "§8§lCustom Biomes");

        // Fill with gray panes
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        if (meta != null) { meta.setDisplayName(" "); pane.setItemMeta(meta); }
        for (int i = 0; i < 45; i++) gui.setItem(i, pane);

        // Biome icons start at slot 10, skip border
        int[] slots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34};
        int i = 0;
        for (FXBiomeDefinition def : BiomeRegistry.getAllBiomes()) {
            if (i >= slots.length) break;
            ItemStack icon = new ItemStack(getMaterialForBiomeType(def.biomeType));
            ItemMeta im = icon.getItemMeta();
            if (im != null) {
                im.setDisplayName(def.displayName);
                im.setLore(List.of(
                        "§7Type: §f" + def.biomeType,
                        "§7Dimension: §f" + def.dimension,
                        "§7Grass Color: §a#" + Integer.toHexString(def.grassColor),
                        "§7Leaf Color: §a#" + Integer.toHexString(def.leafColor),
                        "§7Spawn Chance: §e" + def.spawnChance
                ));
                icon.setItemMeta(im);
            }
            gui.setItem(slots[i++], icon);
        }
        player.openInventory(gui);
    }

    private Material getMaterialForBiomeType(BiomeType type) {
        return switch (type) {
            case PLAINS, MEADOW -> Material.GRASS_BLOCK;
            case FOREST, DARK_FOREST, BIRCH_FOREST -> Material.OAK_LOG;
            case TAIGA -> Material.SPRUCE_LOG;
            case JUNGLE -> Material.JUNGLE_LOG;
            case SAVANNA -> Material.ACACIA_LOG;
            case DESERT -> Material.SAND;
            case SWAMP -> Material.VINE;
            case MOUNTAINS, FROZEN_PEAKS -> Material.STONE;
            case RIVER, OCEAN, BEACH -> Material.WATER_BUCKET;
            case MUSHROOM_FIELDS -> Material.RED_MUSHROOM_BLOCK;
            case NETHER_WASTES -> Material.NETHERRACK;
            case SOUL_SAND_VALLEY -> Material.SOUL_SAND;
            case CRIMSON_FOREST -> Material.CRIMSON_STEM;
            case WARPED_FOREST -> Material.WARPED_STEM;
            case BASALT_DELTAS -> Material.BASALT;
            case END_MIDLANDS, END_HIGHLANDS, END_BARRENS, END_CITY -> Material.END_STONE;
            case DRIPSTONE_CAVES -> Material.POINTED_DRIPSTONE;
            case LUSH_CAVES -> Material.AZALEA_LEAVES;
            case GROVE -> Material.SNOW_BLOCK;
            default -> Material.GRASS_BLOCK;
        };
    }
}