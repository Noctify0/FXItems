package com.noctify.Main.Exceptions;

import org.bukkit.entity.Player;

public class RecipeException extends Exception {
    public RecipeException(Player player, String itemId) {
        super("§cNo recipe found for this item: " + itemId);
        player.sendMessage("§cNo recipe found for this item.");
    }
}