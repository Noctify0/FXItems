package com.noctify.Custom.ArmorBehavior;

import com.noctify.Main.Registration.FXArmorBehavior;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ExampleArmorBehavior extends FXArmorBehavior {
    @Override
    public boolean onEquipped(Player player, ItemStack armor) {
        player.sendMessage("You equipped the Example Armor!");
        return false;
    }

    @Override
    public boolean whileWearing(Player player, ItemStack armor) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100000000, 1));
        return true;
    }
}