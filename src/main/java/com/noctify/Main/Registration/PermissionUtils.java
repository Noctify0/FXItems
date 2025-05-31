package com.noctify.Main.Registration;

import org.bukkit.entity.Player;

public class PermissionUtils {
    public static PermissionLevel getPermissionLevel(Player player) {
        if (player.hasPermission("fxitems.owner")) {
            return PermissionLevel.ADMIN;
        }
        return PermissionLevel.PLAYER;
    }
}