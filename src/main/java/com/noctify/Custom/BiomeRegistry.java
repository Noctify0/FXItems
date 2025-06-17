package com.noctify.Custom;

import com.noctify.Main.Registration.*;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.*;

public class BiomeRegistry {
    private static final Map<String, FXBiomeDefinition> biomes = new HashMap<>();
    private static final List<FXBiomeDefinition> biomeList = new ArrayList<>();
    private static double totalRarity = 0.0;

    public static void initialize() {
        // Register all your biomes here
        registerBiome("wasteland", new FXBiomeDefinition(
                "wasteland",
                "§fWasteland",
                BiomeType.PLAINS,
                "overworld",
                new com.noctify.Custom.BiomeBehavior.ExampleBiomeBehavior(),
                0x7a5230,
                0x3a4f3f,
                0xd7d49c,
                0.25f,
                0.03
        ));
        // Add more biomes by calling registerBiome(...) with new behavior classes
    }

    public static void registerBiome(String id, FXBiomeDefinition def) {
        biomes.put(id.toLowerCase(), def);
        biomeList.add(def);
        totalRarity += def.spawnChance;
    }

    public static FXBiomeDefinition getBiomeDefinition(String id) {
        return biomes.get(id.toLowerCase());
    }

    public static Set<String> getBiomeIds() {
        return biomes.keySet();
    }

    public static Collection<FXBiomeDefinition> getAllBiomes() {
        return biomes.values();
    }

    public static FXBiomeDefinition getBiomeDefinitionAt(Location loc) {
        double noise = getNoise(loc.getBlockX(), loc.getBlockZ(), loc.getWorld().getSeed());
        double threshold = 0.0;
        for (FXBiomeDefinition def : biomeList) {
            threshold += def.spawnChance / totalRarity;
            if (noise < threshold) {
                return def;
            }
        }
        return biomeList.isEmpty() ? null : biomeList.get(biomeList.size() - 1);
    }

    private static double getNoise(int x, int z, long seed) {
        long hash = seed + x * 341873128712L + z * 132897987541L;
        Random rand = new Random(hash);
        return rand.nextDouble();
    }
}