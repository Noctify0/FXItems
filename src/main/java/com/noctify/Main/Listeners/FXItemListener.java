package com.noctify.Main.Listeners;

import com.noctify.Main.Registration.FXItemDefinition;
import com.noctify.Custom.ItemRegistry;
import com.noctify.Main.Registration.FXItemBehavior;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class FXItemListener implements Listener {

    private FXItemDefinition getCustomItem(ItemStack item) {
        return ItemRegistry.getDefinition(item); // Now uses the helper in ItemRegistry
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        FXItemDefinition def = getCustomItem(item);
        if (def == null) return;
        FXItemBehavior behavior = def.behavior;

        boolean isShift = player.isSneaking();
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
                if (isShift ? behavior.shiftRightClickAirAction(player, item) : behavior.rightClickAirAction(player, item))
                    event.setCancelled(true);
                break;
            case RIGHT_CLICK_BLOCK:
                if (isShift ? behavior.shiftRightClickBlockAction(player, event, event.getClickedBlock(), item)
                        : behavior.rightClickBlockAction(player, event, event.getClickedBlock(), item))
                    event.setCancelled(true);
                break;
            case LEFT_CLICK_AIR:
                if (isShift ? behavior.shiftLeftClickAirAction(player, item) : behavior.leftClickAirAction(player, item))
                    event.setCancelled(true);
                break;
            case LEFT_CLICK_BLOCK:
                if (isShift ? behavior.shiftLeftClickBlockAction(player, event, event.getClickedBlock(), item)
                        : behavior.leftClickBlockAction(player, event, event.getClickedBlock(), item))
                    event.setCancelled(true);
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        FXItemDefinition def = getCustomItem(item);
        if (def == null) return;
        FXItemBehavior behavior = def.behavior;
        if (behavior.hitEntityAction(player, null, event.getRightClicked(), item)) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        FXItemDefinition def = getCustomItem(item);
        if (def == null) return;
        FXItemBehavior behavior = def.behavior;
        if (behavior.clickedInInventoryAction(player, event, item, event.getCursor())) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();
        FXItemDefinition def = getCustomItem(item);
        if (def == null) return;
        FXItemBehavior behavior = def.behavior;
        if (behavior.hitEntityAction(player, event, event.getEntity(), item)) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        FXItemDefinition def = getCustomItem(item);
        if (def == null) return;
        FXItemBehavior behavior = def.behavior;
        if (behavior.breakBlockAction(player, event, event.getBlock(), item)) event.setCancelled(true);
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (item == null) return;
        FXItemDefinition def = getCustomItem(item);
        if (def == null) return;
        FXItemBehavior behavior = def.behavior;
        behavior.onItemHeld(player, item);
    }
}