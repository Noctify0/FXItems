package com.noctify.Main.Registration;

public class FXBiomeDefinition {
    public final String id;
    public final String displayName;
    public final BiomeType biomeType;
    public final String dimension;
    public final FXBiomeBehavior behavior;
    public final int grassColor;
    public final int leafColor;
    public final int skyColor;
    public final float fogLevel;
    public final double spawnChance;

    public FXBiomeDefinition(
            String id,
            String displayName,
            BiomeType biomeType,
            String dimension,
            FXBiomeBehavior behavior,
            int grassColor,
            int leafColor,
            int skyColor,
            float fogLevel,
            double spawnChance
    ) {
        this.id = id;
        this.displayName = displayName;
        this.biomeType = biomeType;
        this.dimension = dimension;
        this.behavior = behavior;
        this.grassColor = grassColor;
        this.leafColor = leafColor;
        this.skyColor = skyColor;
        this.fogLevel = fogLevel;
        this.spawnChance = spawnChance;
    }
}