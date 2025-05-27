package com.noctify.Custom.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class ExampleEvent extends Event implements Listener {

    // This is required for all custom events to manage event handlers
    private static final HandlerList HANDLERS = new HandlerList();

    // The player involved in this event
    private final Player player;

    // A custom message for this event
    private final String message;

    // Constructor to create the event with a player and a message
    public ExampleEvent(Player player, String message) {
        this.player = player; // Set the player
        this.message = message; // Set the message
    }

    // Get the player who triggered the event
    public Player getPlayer() {
        return player;
    }

    // Get the custom message for this event
    public String getMessage() {
        return message;
    }

    // This method is required to return the handler list for this event
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    // This is a static method to get the handler list (used by Bukkit)
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}