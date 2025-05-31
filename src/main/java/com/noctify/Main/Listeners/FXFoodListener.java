package com.noctify.Main.Listeners;

import com.noctify.Custom.FoodRegistry;
import com.noctify.Main.Registration.FXFoodBehavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class FXFoodListener implements Listener {

    private final Plugin plugin;

    public FXFoodListener(Plugin plugin) {
        this.plugin = plugin;
    }

    private String getFoodId(ItemStack item) {
        if (item == null) return null;
        for (String id : FoodRegistry.getFoodIds()) {
            ItemStack reg = FoodRegistry.getFoodItem(id);
            if (reg != null && item.isSimilar(reg)) {
                return id;
            }
        }
        return null;
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack food = event.getItem();
        String id = getFoodId(food);
        if (id == null) return;
        FXFoodBehavior behavior = FoodRegistry.getFoodBehavior(id);
        if (behavior != null) {
            if (behavior.beforeConsume(player, event, food)) {
                event.setCancelled(true);
                return;
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                behavior.afterConsume(player, food);
            }, 1L);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack food = event.getItem();
        String id = getFoodId(food);
        if (id == null) return;
        FXFoodBehavior behavior = FoodRegistry.getFoodBehavior(id);
        if (behavior == null) return;

        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                if (player.isSneaking()) {
                    behavior.shiftRightClickAction(player, event, food);
                } else {
                    behavior.rightClickAction(player, event, food);
                }
                break;
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                if (player.isSneaking()) {
                    behavior.shiftLeftClickAction(player, event, food);
                } else {
                    behavior.leftClickAction(player, event, food);
                }
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onFoodHeld(PlayerItemHeldEvent event) {
        Player player = (Player) event.getPlayer();
        ItemStack food = player.getInventory().getItem(event.getNewSlot());
        String id = getFoodId(food);
        if (id == null) return;
        FXFoodBehavior behavior = FoodRegistry.getFoodBehavior(id);
        if (behavior != null) {
            behavior.onFoodHeld(player, food);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack food = player.getInventory().getItemInMainHand();
        String id = getFoodId(food);
        if (id == null) return;
        FXFoodBehavior behavior = FoodRegistry.getFoodBehavior(id);
        if (behavior != null) {
            behavior.activeEffect(player, food);
        }
    }
}