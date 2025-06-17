package com.noctify.Main.Registration;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public class FXArmorDefinition {
    public final String id;
    public final String displayName;
    public final ArmorType type;
    public final ArmorMaterial material;
    public final int armorPoints;
    public final int durability;
    public final FXArmorBehavior behavior;
    public final Integer customModelData;
    public final boolean unbreakable;
    public final boolean hideFlags;
    public final boolean glowing;
    public final List<String> lore;
    public final FXItemRecipe recipe;

    public FXArmorDefinition(
            String id,
            String displayName,
            ArmorType type,
            ArmorMaterial material,
            int armorPoints,
            Integer customModelData,
            int durability,
            FXArmorBehavior behavior,
            boolean unbreakable,
            boolean hideFlags,
            boolean glowing,
            List<String> lore,
            FXItemRecipe recipe
    ) {
        this.id = id;
        this.displayName = displayName;
        this.type = type;
        this.material = material;
        this.armorPoints = armorPoints;
        this.customModelData = customModelData;
        this.durability = durability;
        this.behavior = behavior;
        this.unbreakable = unbreakable;
        this.hideFlags = hideFlags;
        this.glowing = glowing;
        this.lore = lore;
        this.recipe = recipe;
    }
}