package com.noctify.API;

import com.noctify.Main.Registration.FXFoodBehavior;
import com.noctify.Main.Registration.FXFoodDefinition;
import com.noctify.Main.Registration.FXItemDefinition;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface Registry {
    void registerItem(String itemName, FXItemDefinition itemDefinition);
    ItemStack getCustomItem(String itemName);
    void registerCommand(Plugin plugin, String commandName, Class<? extends CommandExecutor> commandClass);
    void registerCommand(Plugin plugin, String commandName);
    void registerEvent(Plugin plugin, Class<? extends Listener> eventClass);
    void registerEvent(Plugin plugin, String eventName);
    void registerFood(String foodName, FXFoodDefinition definition, FXFoodBehavior behavior);
}