// File: src/main/java/com/noctify/Main/Registration/FXFoodDefinition.java
package com.noctify.Main.Registration;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import java.util.List;

public class FXFoodDefinition {
    public final String displayName;
    public final int hungerPoints;
    public final float saturationPoints;
    public final List<PotionEffect> effects;
    public final List<String> lore;
    public final List<ItemStack> recipe; // 3x3 grid, 9 elements
    public final int customModelData;
    public final String itemModel;

    public FXFoodDefinition(
            String displayName,
            int hungerPoints,
            float saturationPoints,
            List<PotionEffect> effects,
            List<String> lore,
            List<ItemStack> recipe,
            int customModelData,
            String itemModel
    ) {
        this.displayName = displayName;
        this.hungerPoints = hungerPoints;
        this.saturationPoints = saturationPoints;
        this.effects = effects;
        this.lore = lore;
        this.recipe = recipe;
        this.customModelData = customModelData;
        this.itemModel = itemModel;
    }
}