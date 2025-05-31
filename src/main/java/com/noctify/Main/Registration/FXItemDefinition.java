package com.noctify.Main.Registration;

import com.noctify.Main.Registration.FXItemBehavior;
import java.util.List;

public class FXItemDefinition {
    public final String id;
    public final String displayName;
    public final org.bukkit.Material material;
    public final Rarity rarity;
    public final FXItemBehavior behavior;
    public final Integer customModelData;
    public final boolean oneTimeCraft;
    public final boolean unbreakable;
    public final boolean hideFlags;
    public final List<String> lore;
    public final FXItemRecipe recipe;

    public FXItemDefinition(
            String id,
            String displayName,
            org.bukkit.Material material,
            Rarity rarity,
            FXItemBehavior behavior,
            Integer customModelData,
            boolean oneTimeCraft,
            boolean unbreakable,
            boolean hideFlags,
            List<String> lore,
            FXItemRecipe recipe
    ) {
        this.id = id;
        this.displayName = displayName;
        this.material = material;
        this.rarity = rarity;
        this.behavior = behavior;
        this.customModelData = customModelData;
        this.oneTimeCraft = oneTimeCraft;
        this.unbreakable = unbreakable;
        this.hideFlags = hideFlags;
        this.lore = lore;
        this.recipe = recipe;
    }
}