package com.noctify.Custom;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import com.noctify.Custom.Events.*;

public class EventRegistry {

    // Register your custom events here.

    public static void initialize(Plugin plugin) {

        //
        // This is pretty simple and straightforward
        // You create an Event class in the com.noctify.Custom.Events package
        // and then you register it here.
        //
        // Example Registry Implementation:
        // registerEvent(plugin, "event_name");
        //

        registerEvent(plugin, "ExampleEvent");

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

    public static void registerEvent(Plugin plugin, String eventName) {
        try {
            // Construct the event class name
            String eventClassName = "com.noctify.Custom.Events." + capitalize(eventName);

            // Load the event class
            Class<?> eventClass = Class.forName(eventClassName);

            // Instantiate the event class
            Object eventInstance = eventClass.getConstructor().newInstance();

            // Register the event as a listener
            plugin.getServer().getPluginManager().registerEvents((org.bukkit.event.Listener) eventInstance, plugin);

            // Send success message
            Bukkit.broadcastMessage("§aSuccessfully registered event: " + eventName);
        } catch (Exception e) {
            // Send error message
            Bukkit.broadcastMessage("§cFailed to register event: " + eventName);
            e.printStackTrace();
        }
    }

    // Utility method to capitalize the first letter of the event name
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}