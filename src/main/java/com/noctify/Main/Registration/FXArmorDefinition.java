// src/main/java/com/noctify/Main/Registration/FXArmorDefinition.java
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
    public final boolean oneTimeCraft;
    public final boolean unbreakable;
    public final boolean hideFlags;
    public final List<String> lore;
    public final FXItemRecipe recipe;
    public final int amount;
    public final boolean shaped;

    public FXArmorDefinition(
            String id,
            String displayName,
            ArmorType type,
            ArmorMaterial material,
            int armorPoints,
            int durability,
            FXArmorBehavior behavior,
            Integer customModelData,
            boolean oneTimeCraft,
            boolean unbreakable,
            boolean hideFlags,
            List<String> lore,
            FXItemRecipe recipe,
            int amount,
            boolean shaped
    ) {
        this.id = id;
        this.displayName = displayName;
        this.type = type;
        this.material = material;
        this.armorPoints = armorPoints;
        this.durability = durability;
        this.behavior = behavior;
        this.customModelData = customModelData;
        this.oneTimeCraft = oneTimeCraft;
        this.unbreakable = unbreakable;
        this.hideFlags = hideFlags;
        this.lore = lore;
        this.recipe = recipe;
        this.amount = amount;
        this.shaped = shaped;
    }
}