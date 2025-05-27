package com.noctify.Custom.OtherBehaviors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

public class ExampleFoodBehavior implements Listener, Consumer<Player> {

    @Override
    public void accept(Player player) {
        // Example behavior: Create a small explosion at the player's location
        Location location = player.getLocation();
        player.getWorld().createExplosion(location, 2.0F, false, false); // Small explosion, no fire, no block damage
    }
}