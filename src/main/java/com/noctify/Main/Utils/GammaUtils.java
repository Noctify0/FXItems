package com.noctify.Main.Utils;

import com.noctify.Main.FXItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GammaUtils implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        if (!player.hasPermission("smp.nightvision")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        if (label.equalsIgnoreCase("nv") || label.equalsIgnoreCase("nightvision")) {
            boolean isNightVisionEnabled = player.hasMetadata("night_vision");

            if (isNightVisionEnabled) {
                // Disable night vision
                player.removeMetadata("night_vision", FXItems.getInstance());
                player.removePotionEffect(PotionEffectType.NIGHT_VISION); // Remove night vision effect
                player.sendMessage(ChatColor.RED + "ɴɪɢʜᴛ ᴠɪꜱɪᴏɴ ᴅɪꜱᴀʙʟᴇᴅ.");
            } else {
                // Enable night vision
                player.setMetadata("night_vision", new FixedMetadataValue(FXItems.getInstance(), true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false)); // Add night vision effect
                player.sendMessage(ChatColor.GREEN + "ɴɪɢʜᴛ ᴠɪꜱɪᴏɴ ᴇɴᴀʙʟᴇᴅ.");
            }
            return true;
        }

        return false;
    }
}