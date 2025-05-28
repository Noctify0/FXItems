package com.noctify.API;

import com.noctify.Custom.ItemRegistry;
import com.noctify.Custom.CommandRegistry;
import com.noctify.Custom.EventRegistry;
import com.noctify.Custom.FoodRegistry;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Registry {
    // Item registration
    public static void registerItem(String itemName, Class<?> itemClass) {
        ItemRegistry.registerItem(itemName, itemClass);
    }

    // Behavior registration
    public static void registerBehavior(Plugin plugin, String itemName, Class<?> behaviorClass) {
        ItemRegistry.registerBehavior(plugin, itemName, behaviorClass);
    }

    // Recipe registration
    public static void addRecipe(Plugin plugin, Class<?> attributesClass) {
        ItemRegistry.addRecipe(plugin, attributesClass);
    }

    // Command registration (by name and class)
    public static void registerCommand(Plugin plugin, String commandName, Class<? extends CommandExecutor> commandClass) {
        CommandRegistry.registerCommand(plugin, commandName, commandClass);
    }

    // Command registration (by name only)
    public static void registerCommand(Plugin plugin, String commandName) {
        CommandRegistry.registerCommand(plugin, commandName);
    }

    // Event registration (by class)
    public static void registerEvent(Plugin plugin, Class<? extends Listener> eventClass) {
        EventRegistry.registerEvent(plugin, eventClass);
    }

    // Event registration (by name)
    public static void registerEvent(Plugin plugin, String eventName) {
        EventRegistry.registerEvent(plugin, eventName);
    }

    // Food registration
    public static void registerFood(Plugin plugin, String foodName, Class<?> foodClass, Class<?> behaviorClass) {
        FoodRegistry.registerFood(plugin, foodName, foodClass, behaviorClass);
    }
}