package com.noctify.Main.Registration;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class FXFoodBehavior {
    public void onItemStackCreate(ItemStack item) {}

    public boolean beforeConsume(Player player, PlayerItemConsumeEvent event, ItemStack food) { return false; }
    public boolean afterConsume(Player player, ItemStack food) { return false; }
    public boolean rightClickAction(Player player, PlayerInteractEvent event, ItemStack food) { return false; }
    public boolean leftClickAction(Player player, PlayerInteractEvent event, ItemStack food) { return false; }
    public boolean shiftRightClickAction(Player player, PlayerInteractEvent event, ItemStack food) { return false; }
    public boolean shiftLeftClickAction(Player player, PlayerInteractEvent event, ItemStack food) { return false; }
    public boolean onFoodHeld(Player player, ItemStack food) { return false; }
    public boolean activeEffect(Player player, ItemStack food) { return false; }
}