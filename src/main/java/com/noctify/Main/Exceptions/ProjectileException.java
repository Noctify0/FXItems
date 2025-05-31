package com.noctify.Main.Exceptions;

import org.bukkit.command.CommandSender;

public class ProjectileException extends Exception {
    public ProjectileException(String message, CommandSender sender) {
        super(message);
        if (sender != null) {
            sender.sendMessage(message);
        }
    }

    public ProjectileException(String message) {
        super(message);
    }
}