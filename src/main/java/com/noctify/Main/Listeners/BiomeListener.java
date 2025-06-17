// src/main/java/com/noctify/Main/Listeners/BiomeListener.java
package com.noctify.Main.Listeners;

import com.noctify.Custom.BiomeRegistry;
import com.noctify.Main.Registration.FXBiomeDefinition;
import com.noctify.Main.Registration.FXBiomeBehavior;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class BiomeListener implements Listener {

    private final JavaPlugin plugin;
    private final Map<String, Integer> structureCount = new HashMap<>();

    public BiomeListener(JavaPlugin plugin) {
        this.plugin = plugin;
        startParticleTask();
    }

    // Surface populators, structure spawns, custom events
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        World world = chunk.getWorld();
        if (!world.getEnvironment().name().equalsIgnoreCase("NORMAL")) return; // Only overworld for now

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int wx = (chunk.getX() << 4) + x;
                int wz = (chunk.getZ() << 4) + z;
                Location loc = new Location(world, wx, world.getHighestBlockYAt(wx, wz), wz);
                FXBiomeDefinition def = BiomeRegistry.getBiomeDefinitionAt(loc);
                if (def == null) continue;

                // Surface populators
                for (String pop : def.behavior.getSurfacePopulators()) {
                    if (pop.equals("OAK_TREE")) {
                        loc.getBlock().setType(Material.OAK_LOG);
                        loc.clone().add(0,1,0).getBlock().setType(Material.OAK_LEAVES);
                    } else if (pop.equals("TALL_GRASS")) {
                        loc.getBlock().setType(Material.TALL_GRASS);
                    } else if (pop.equals("POPPY")) {
                        loc.getBlock().setType(Material.POPPY);
                    }
                    // Add more as needed
                }

                // Structure spawns
                for (Map.Entry<String, FXBiomeBehavior.StructureSpawnInfo> entry : def.behavior.getStructureSpawns().entrySet()) {
                    String struct = entry.getKey();
                    FXBiomeBehavior.StructureSpawnInfo info = entry.getValue();
                    String key = def.id + ":" + chunk.getX() + "," + chunk.getZ() + ":" + struct;
                    int count = structureCount.getOrDefault(key, 0);
                    if (count < info.maxPerBiome && Math.random() < info.rarity) {
                        // Example: spawn a well (replace with your structure logic)
                        loc.getBlock().setType(Material.STONE);
                        structureCount.put(key, count + 1);
                    }
                }

                // Custom events
                for (FXBiomeBehavior.CustomBiomeEvent customEvent : def.behavior.getCustomEvents()) {
                    customEvent.runEvent(chunk, def);
                }
            }
        }
    }

    // Mob spawning control
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Location loc = event.getLocation();
        FXBiomeDefinition def = BiomeRegistry.getBiomeDefinitionAt(loc);
        if (def != null) {
            Map<String, FXBiomeBehavior.MobSpawnInfo> mobSpawns = def.behavior.getMobSpawning();
            FXBiomeBehavior.MobSpawnInfo info = mobSpawns.get(event.getEntityType().name());
            if (info == null || Math.random() > info.spawnChance) {
                event.setCancelled(true);
            } else {
                // Enforce group size
                int count = (int) loc.getWorld().getNearbyEntities(loc, 16, 8, 16, e -> e.getType() == event.getEntityType()).size();
                if (count >= info.maxGroup) event.setCancelled(true);
            }
        }
    }

    // Weather control
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        Location loc = world.getSpawnLocation();
        FXBiomeDefinition def = BiomeRegistry.getBiomeDefinitionAt(loc);
        if (def != null) {
            FXBiomeBehavior.EnvironmentSettings env = def.behavior.getEnvironment();
            if (env != null && Math.random() > env.rainChance) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        World world = event.getWorld();
        Location loc = world.getSpawnLocation();
        FXBiomeDefinition def = BiomeRegistry.getBiomeDefinitionAt(loc);
        if (def != null) {
            FXBiomeBehavior.EnvironmentSettings env = def.behavior.getEnvironment();
            if (env != null && Math.random() > env.thunderChance) {
                event.setCancelled(true);
            }
        }
    }

    // Particle effects in custom biomes
    private void startParticleTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Player player : world.getPlayers()) {
                        FXBiomeDefinition def = BiomeRegistry.getBiomeDefinitionAt(player.getLocation());
                        if (def != null) {
                            FXBiomeBehavior.EnvironmentSettings env = def.behavior.getEnvironment();
                            if (env != null && env.particleEffect != null) {
                                try {
                                    Particle particle = Particle.valueOf(env.particleEffect);
                                    player.spawnParticle(particle, player.getLocation(), 10, 0.5, 1, 0.5, 0.01);
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20, 40);
    }
}