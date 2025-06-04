package com.noctify.Main.Listeners;

import com.noctify.Custom.ArmorRegistry;
import com.noctify.Main.Registration.FXArmorDefinition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FXArmorListener implements Listener {

    public FXArmorListener() {
        // Schedule whileWearing checks every 2 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (ItemStack armor : player.getInventory().getArmorContents()) {
                        FXArmorDefinition def = ArmorRegistry.getDefinition(armor);
                        if (def != null && def.behavior != null) {
                            def.behavior.whileWearing(player, armor);
                        }
                    }
                }
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), 20, 40); // 1s delay, 2s period
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        int rawSlot = event.getRawSlot();
        if (rawSlot < 0 || rawSlot > 39) return;

        ItemStack oldArmor = event.getCurrentItem();
        ItemStack newArmor = event.getCursor();

        FXArmorDefinition oldDef = ArmorRegistry.getDefinition(oldArmor);
        FXArmorDefinition newDef = ArmorRegistry.getDefinition(newArmor);

        // Call onUnequipped
        if (oldDef != null && oldDef.behavior != null) {
            oldDef.behavior.onUnequipped(player, oldArmor);
        }
        // Call onEquipped
        if (newDef != null && newDef.behavior != null) {
            newDef.behavior.onEquipped(player, newArmor);
        }
        // Call onInventoryClick for custom armor
        if (newDef != null && newDef.behavior != null) {
            newDef.behavior.onInventoryClick(player, event, newArmor);
        }
    }
}