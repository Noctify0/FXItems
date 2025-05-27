package com.noctify.Main.Utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TeleportUtils {

    /**
     * Teleports a player to a target location with optional distance limit, direction preservation, sound, and particles.
     *
     * @param player The player to teleport.
     * @param targetLocation The location to teleport to.
     * @param maxDistance The maximum allowed distance for teleportation.
     * @param preserveDirection Whether to preserve the player's original yaw and pitch.
     * @param playSound Whether to play a sound effect during teleportation.
     * @param spawnParticles Whether to spawn particles at the teleportation location.
     * @return True if the teleportation was successful, false otherwise.
     */
    public static boolean teleportPlayer(Player player, Location targetLocation, int maxDistance, boolean preserveDirection, boolean playSound, boolean spawnParticles) {
        Location playerLocation = player.getLocation();

        // Check if the target location is within the allowed distance
        if (playerLocation.distance(targetLocation) > maxDistance) {
            player.sendMessage("§cTarget location is too far away! Maximum distance: " + maxDistance + " blocks.");
            return false;
        }

        // Preserve the player's direction if specified
        if (preserveDirection) {
            targetLocation.setYaw(playerLocation.getYaw());
            targetLocation.setPitch(playerLocation.getPitch());
        }

        // Teleport the player
        player.teleport(targetLocation);

        // Play teleportation effects if enabled
        if (playSound) {
            player.playSound(playerLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        }
        if (spawnParticles) {
            player.spawnParticle(Particle.PORTAL, targetLocation, 50, 0.5, 0.5, 0.5, 0.1);
        }

        return true;
    }
}