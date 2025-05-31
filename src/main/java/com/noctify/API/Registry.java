package com.noctify.API;

import com.noctify.Main.Registration.FXFoodBehavior;
import com.noctify.Main.Registration.FXFoodDefinition;
import com.noctify.Main.Registration.FXItemDefinition;
import com.noctify.Main.Registration.PermissionLevel;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface Registry {
    void registerItem(String itemName, FXItemDefinition itemDefinition);
    ItemStack getCustomItem(String itemName);

    void registerCommand(Plugin plugin, String commandName, Class<? extends CommandExecutor> commandClass);
    void registerCommand(Plugin plugin, String commandName, Class<? extends CommandExecutor> commandClass, PermissionLevel requiredLevel);

    void registerCommand(Plugin plugin, String commandName);
    void registerCommand(Plugin plugin, String commandName, PermissionLevel requiredLevel);

    void registerEvent(Plugin plugin, Class<? extends Listener> eventClass);
    void registerEvent(Plugin plugin, String eventName);
    void registerFood(String foodName, FXFoodDefinition definition, FXFoodBehavior behavior);
}