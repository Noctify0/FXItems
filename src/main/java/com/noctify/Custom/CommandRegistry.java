package com.noctify.Custom;

import com.noctify.Custom.Commands.*;
import com.noctify.Main.Registration.PermissionLevel;
import com.noctify.Main.Registration.PermissionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class CommandRegistry {

    // Register any Custom Commands here.

    public static void initialize(Plugin plugin) {
        //
        // Create a behavior class for the command in com.noctify.Custom.Commands
        // Subcommands and onTabComplete are supported as well, add their implementation in your commands main class.
        // An Example command class will be provided in the Commands package.
        //
        // Sample CommandRegistry Implementation:
        // registerCommand(plugin, "ping");
        //
        // WARNING: The Commands class name will be the commands name as well:
        // Code: command.class
        // InGame: /command
        //

        registerCommand(plugin, "ExampleCommand", ExampleCommand.class);

    }

    //
    // ----------------------------------------------------------------------------------------|
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    // DO NOT edit anything below.                                                             |
    // Unless you absolutely know what you are doing.                                          |
    //                                                                                         |
    //                                                                           ?              |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    //                                                                                         |
    // ----------------------------------------------------------------------------------------|
    //

    public static void registerCommand(Plugin plugin, String commandName, PermissionLevel requiredLevel) {
        try {
            String commandClassName = "com.noctify.Custom.Commands." + capitalize(commandName) + "Command";
            Class<?> commandClass = Class.forName(commandClassName);
            if (!CommandExecutor.class.isAssignableFrom(commandClass)) {
                warn(plugin, commandClassName + " does not implement CommandExecutor.");
                return;
            }
            registerCommand(plugin, commandName, (Class<? extends CommandExecutor>) commandClass, requiredLevel);
        } catch (Exception e) {
            warn(plugin, "Failed to register command: " + commandName);
            e.printStackTrace();
        }
    }

    // Register by explicit class, defaulting to PLAYER permission
    public static void registerCommand(Plugin plugin, String commandName, Class<? extends CommandExecutor> commandClass) {
        registerCommand(plugin, commandName, commandClass, PermissionLevel.PLAYER);
    }

    public static void registerCommand(Plugin plugin, String commandName, Class<? extends CommandExecutor> commandClass, PermissionLevel requiredLevel) {
        try {
            CommandExecutor commandInstance = commandClass.getConstructor().newInstance();
            Command command = new BukkitCommand(commandName) {
                @Override
                public boolean execute(org.bukkit.command.CommandSender sender, String label, String[] args) {
                    if (sender instanceof Player) {
                        PermissionLevel playerLevel = PermissionUtils.getPermissionLevel((Player) sender);
                        if (playerLevel.ordinal() < requiredLevel.ordinal()) {
                            sender.sendMessage("§cYou do not have permission to use this command.");
                            return true;
                        }
                    }
                    return commandInstance.onCommand(sender, this, label, args);
                }
            };
            CommandMap commandMap = getCommandMap();
            if (commandMap != null) {
                commandMap.register(plugin.getName(), command);
                String successMessage = "Successfully registered command: /" + commandName;
                plugin.getLogger().info(successMessage);
                Bukkit.broadcastMessage("§a" + successMessage);
            }
        } catch (Exception e) {
            warn(plugin, "Failed to register command: " + commandName);
            e.printStackTrace();
        }
    }

    private static CommandMap getCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static void warn(Plugin plugin, String message) {
        plugin.getLogger().warning(message);
        Bukkit.broadcastMessage("§c" + message);
    }
}