// src/main/java/com/noctify/API/RegistryAPI.java
package com.noctify.API;

import com.noctify.Custom.ItemRegistry;
import com.noctify.Custom.ArmorRegistry;
import com.noctify.Custom.CommandRegistry;
import com.noctify.Custom.EventRegistry;
import com.noctify.Custom.FoodRegistry;
import com.noctify.Custom.BiomeRegistry;
import com.noctify.Main.Registration.FXFoodBehavior;
import com.noctify.Main.Registration.FXFoodDefinition;
import com.noctify.Main.Registration.FXItemDefinition;
import com.noctify.Main.Registration.FXArmorDefinition;
import com.noctify.Main.Registration.FXBiomeDefinition;
import com.noctify.Main.Registration.PermissionLevel;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;

public class RegistryAPI {

    // Item registry
    public static void registerItem(String itemName, FXItemDefinition itemDefinition) {
        ItemRegistry.registerItem(itemName, itemDefinition);
    }

    public static ItemStack getCustomItem(String itemName) {
        return ItemRegistry.getCustomItem(itemName);
    }

    // Armor registry
    public static void registerArmor(String armorName, FXArmorDefinition armorDefinition) {
        ArmorRegistry.registerArmor(armorName, armorDefinition);
    }

    public static ItemStack getCustomArmor(String armorName) {
        return ArmorRegistry.getCustomArmor(armorName);
    }

    // Command registry
    public static void registerCommand(Plugin plugin, String commandName, Class<? extends CommandExecutor> commandClass) {
        CommandRegistry.registerCommand(plugin, commandName, commandClass);
    }

    public static void registerCommand(Plugin plugin, String commandName, Class<? extends CommandExecutor> commandClass, PermissionLevel requiredLevel) {
        CommandRegistry.registerCommand(plugin, commandName, commandClass, requiredLevel);
    }

    public static void registerCommand(Plugin plugin, String commandName) {
        CommandRegistry.registerCommand(plugin, commandName, PermissionLevel.PLAYER);
    }

    public static void registerCommand(Plugin plugin, String commandName, PermissionLevel requiredLevel) {
        CommandRegistry.registerCommand(plugin, commandName, requiredLevel);
    }

    // Event registry
    public static void registerEvent(Plugin plugin, Class<? extends Listener> eventClass) {
        EventRegistry.registerEvent(plugin, eventClass);
    }

    public static void registerEvent(Plugin plugin, String eventName) {
        EventRegistry.registerEvent(plugin, eventName);
    }

    // Food registry
    public static void registerFood(String foodName, FXFoodDefinition definition, FXFoodBehavior behavior) {
        FoodRegistry.registerFood(foodName, definition, behavior);
    }

    // Biome registry
    public static void registerBiome(String biomeName, FXBiomeDefinition biomeDefinition) {
        BiomeRegistry.registerBiome(biomeName, biomeDefinition);
    }

    public static FXBiomeDefinition getBiomeDefinition(String biomeName) {
        return BiomeRegistry.getBiomeDefinition(biomeName);
    }

    public static FXBiomeDefinition getBiomeDefinitionAt(Location location) {
        return BiomeRegistry.getBiomeDefinitionAt(location);
    }
}