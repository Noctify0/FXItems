// File: src/main/java/com/noctify/Custom/ItemBehavior/ExampleItemBehavior.java
package com.noctify.Custom.ItemBehavior;

import com.noctify.Main.Utils.CooldownUtils;
import com.noctify.Main.Utils.CustomItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ExampleItemBehavior implements Listener {

    public ExampleItemBehavior(Plugin plugin) {
        // Constructor must be present to register the listener
        // Can be left blank
    }

    @EventHandler
    public void onPlayerUseExampleItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // The next line checks if the item is a custom item with specific attributes,
        // So the ability will only trigger for this specific item
        // and not for any other item with the same material
        // Read the syntax of the line to know how to use it

        if (!CustomItemUtils.isCustomItem(item, Material.DIAMOND_SWORD, "&aExample Sword")) {
            return;
        }

        // Ability activation logic
        // Using Right Click Action to trigger the ability
        // You can change the action to LEFT_CLICK_AIR or LEFT_CLICK_BLOCK if needed
        // To make abilities you will have to know how to code in Java and work with Bukkit API
        // If you don't know how to code, you can ask AI to help you with the code, ChatGPT is a good option

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            UUID playerId = player.getUniqueId();
            String ability = "ExamplePower";
            int cooldownTime = 30;

            if (CooldownUtils.isOnCooldown(playerId, ability)) {
                double timeLeft = CooldownUtils.getRemainingCooldown(playerId, ability);
                CooldownUtils.sendCooldownMessage(player, ability, timeLeft);
                return;
            }

            player.sendMessage("§bYou activated Example Power!");
            CooldownUtils.setCooldown(playerId, ability, cooldownTime);
        }
    }
}