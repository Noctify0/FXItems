package com.noctify.Custom.BiomeBehavior;

import com.noctify.Main.Registration.FXBiomeBehavior;
import java.util.*;

public class ExampleBiomeBehavior extends FXBiomeBehavior {
    @Override
    public Map<String, MobSpawnInfo> getMobSpawning() {
        Map<String, MobSpawnInfo> mobs = new HashMap<>();
        mobs.put("ZOMBIE", new MobSpawnInfo(0.8, 2, 5));
        mobs.put("CREEPER", new MobSpawnInfo(0.2, 1, 2));
        return mobs;
    }

    @Override
    public List<String> getSurfacePopulators() {
        return Arrays.asList("OAK_TREE", "GRASS", "EMERALD_ORE");
    }

    @Override
    public Map<String, StructureSpawnInfo> getStructureSpawns() {
        Map<String, StructureSpawnInfo> structures = new HashMap<>();
        structures.put("VILLAGE", new StructureSpawnInfo(0.05, 1));
        return structures;
    }

    @Override
    public EnvironmentSettings getEnvironment() {
        return new EnvironmentSettings(0.3, 0.05, "WHITE_ASH", 0xd7d49c, 0.25f);
    }

    @Override
    public List<CustomBiomeEvent> getCustomEvents() {
        return Collections.emptyList();
    }
}