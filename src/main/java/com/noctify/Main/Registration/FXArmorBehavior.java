// src/main/java/com/noctify/Main/Registration/FXArmorBehavior.java
package com.noctify.Main.Registration;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class FXArmorBehavior {
    public boolean onEquipped(Player player, ItemStack armor) { return false; }
    public boolean onUnequipped(Player player, ItemStack armor) { return false; }
    public boolean onInventoryClick(Player player, InventoryClickEvent event, ItemStack armor) { return false; }
    public boolean whileWearing(Player player, ItemStack armor) { return false; }
    // Add more hooks as needed
}