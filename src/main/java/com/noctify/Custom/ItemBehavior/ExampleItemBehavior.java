package com.noctify.Custom.ItemBehavior;

import com.noctify.Main.Utils.CooldownUtils;
import com.noctify.Main.Utils.CustomItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.noctify.Main.Registration.*;

public class ExampleItemBehavior extends FXItemBehavior {

    @Override
    public boolean rightClickAirAction(Player player, ItemStack item) {
        return activateExamplePower(player, item);
    }

    @Override
    public boolean rightClickBlockAction(Player player, org.bukkit.event.player.PlayerInteractEvent event, org.bukkit.block.Block block, ItemStack item) {
        return activateExamplePower(player, item);
    }

    private boolean activateExamplePower(Player player, ItemStack item) {
        if (!CustomItemUtils.isCustomItem(item, Material.DIAMOND_SWORD, "§aExample Sword")) {
            return false;
        }
        String ability = "ExamplePower";
        int cooldownTime = 30;
        if (CooldownUtils.isOnCooldown(player.getUniqueId(), ability)) {
            double timeLeft = CooldownUtils.getRemainingCooldown(player.getUniqueId(), ability);
            CooldownUtils.sendCooldownMessage(player, ability, timeLeft);
            return true;
        }
        player.sendMessage("§bYou activated Example Power!");
        CooldownUtils.setCooldown(player.getUniqueId(), ability, cooldownTime);
        return true;
    }
}