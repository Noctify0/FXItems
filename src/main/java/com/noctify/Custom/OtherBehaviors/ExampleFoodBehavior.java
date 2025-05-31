package com.noctify.Custom.OtherBehaviors;

import com.noctify.Main.Registration.FXFoodBehavior;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class ExampleFoodBehavior extends FXFoodBehavior {

    @Override
    public boolean beforeConsume(Player player, PlayerItemConsumeEvent event, ItemStack item) {
        return false;
    }

    @Override
    public boolean afterConsume(Player player, ItemStack item) {
        Location location = player.getLocation();
        player.getWorld().createExplosion(location, 2.0F, false, false);
        return false; // or true if you want to indicate the event was handled
    }

    @Override
    public boolean rightClickAction(Player player, PlayerInteractEvent event, ItemStack food) { return false; }

    @Override
    public boolean leftClickAction(Player player, PlayerInteractEvent event, ItemStack food) { return false; }

    @Override
    public boolean shiftRightClickAction(Player player, PlayerInteractEvent event, ItemStack food) { return false; }

    @Override
    public boolean shiftLeftClickAction(Player player, PlayerInteractEvent event, ItemStack food) { return false; }

    @Override
    public boolean onFoodHeld(Player player, ItemStack food) { return false; }

    @Override
    public boolean activeEffect(Player player, ItemStack food) { return false; }
}