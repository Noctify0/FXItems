package com.noctify.API;

import com.noctify.Custom.ItemRegistry;
import com.noctify.Custom.CommandRegistry;
import com.noctify.Custom.EventRegistry;
import com.noctify.Custom.FoodRegistry;
import com.noctify.Main.Registration.FXFoodBehavior;
import com.noctify.Main.Registration.FXFoodDefinition;
import com.noctify.Main.Registration.FXItemDefinition;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Registry {

    // Register a custom item with its definition
    public static void registerItem(String itemName, FXItemDefinition itemDefinition) {
        ItemRegistry.registerItem(itemName, itemDefinition);
    }

    // Optionally, get a custom item by ID
    public static org.bukkit.inventory.ItemStack getCustomItem(String itemName) {
        return ItemRegistry.getCustomItem(itemName);
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
    public static void registerFood(String foodName, FXFoodDefinition definition, FXFoodBehavior behavior) {
        FoodRegistry.registerFood(foodName, definition, behavior);
    }
}