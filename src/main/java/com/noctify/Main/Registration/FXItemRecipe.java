package com.noctify.Main.Registration;

import org.bukkit.inventory.ItemStack;
import java.util.List;

public class FXItemRecipe {
    public final List<ItemStack> ingredients; // 9 items, row-major order
    public final boolean shaped;
    public final int amount;
    public final boolean oneTimeCraft; // Add this field

    public FXItemRecipe(List<ItemStack> ingredients, boolean shaped, int amount, boolean oneTimeCraft) {
        this.ingredients = ingredients;
        this.shaped = shaped;
        this.amount = amount;
        this.oneTimeCraft = oneTimeCraft;
    }

    public boolean isOneTimeCraft() {
        return oneTimeCraft;
    }
}