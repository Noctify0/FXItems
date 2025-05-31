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

public class RegistryAPI implements Registry {
    @Override
    public void registerItem(String itemName, FXItemDefinition itemDefinition) {
        ItemRegistry.registerItem(itemName, itemDefinition);
    }

    @Override
    public ItemStack getCustomItem(String itemName) {
        return ItemRegistry.getCustomItem(itemName);
    }

    @Override
    public void registerCommand(Plugin plugin, String commandName, Class<? extends CommandExecutor> commandClass) {
        CommandRegistry.registerCommand(plugin, commandName, commandClass);
    }

    @Override
    public void registerCommand(Plugin plugin, String commandName) {
        CommandRegistry.registerCommand(plugin, commandName);
    }

    @Override
    public void registerEvent(Plugin plugin, Class<? extends Listener> eventClass) {
        EventRegistry.registerEvent(plugin, eventClass);
    }

    @Override
    public void registerEvent(Plugin plugin, String eventName) {
        EventRegistry.registerEvent(plugin, eventName);
    }

    @Override
    public void registerFood(String foodName, FXFoodDefinition definition, FXFoodBehavior behavior) {
        FoodRegistry.registerFood(foodName, definition, behavior);
    }
}