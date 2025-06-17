// src/main/java/com/noctify/Main/Registration/FXBiomeBehavior.java
package com.noctify.Main.Registration;

import java.util.List;
import java.util.Map;

public abstract class FXBiomeBehavior {
    public abstract Map<String, MobSpawnInfo> getMobSpawning();
    public abstract List<String> getSurfacePopulators();
    public abstract Map<String, StructureSpawnInfo> getStructureSpawns();
    public abstract EnvironmentSettings getEnvironment();
    public abstract List<CustomBiomeEvent> getCustomEvents();

    public static class MobSpawnInfo {
        public final double spawnChance;
        public final int minGroup;
        public final int maxGroup;
        public MobSpawnInfo(double spawnChance, int minGroup, int maxGroup) {
            this.spawnChance = spawnChance;
            this.minGroup = minGroup;
            this.maxGroup = maxGroup;
        }
    }

    public static class StructureSpawnInfo {
        public final double rarity;
        public final int maxPerBiome;
        public StructureSpawnInfo(double rarity, int maxPerBiome) {
            this.rarity = rarity;
            this.maxPerBiome = maxPerBiome;
        }
    }

    public static class EnvironmentSettings {
        public final double rainChance;
        public final double thunderChance;
        public final String particleEffect;
        public final int skyColor;      // New
        public final float fogLevel;    // New

        public EnvironmentSettings(double rainChance, double thunderChance, String particleEffect, int skyColor, float fogLevel) {
            this.rainChance = rainChance;
            this.thunderChance = thunderChance;
            this.particleEffect = particleEffect;
            this.skyColor = skyColor;
            this.fogLevel = fogLevel;
        }
    }

    public interface CustomBiomeEvent {
        void runEvent(org.bukkit.Chunk chunk, FXBiomeDefinition def);
    }
}