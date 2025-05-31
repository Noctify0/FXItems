package com.noctify.Main;

import com.maximde.entitysize.EntitySize;
import com.noctify.Custom.*;
import com.noctify.Main.Listeners.*;
import com.noctify.Main.Utils.EntitySizeUtils;
import com.noctify.Main.Utils.ProjectileUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;

import java.util.*;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class FXItems extends JavaPlugin implements Listener {

    private static FXItems instance;
    private File craftedItemsFile;
    private FileConfiguration craftedItemsConfig;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        File rarityLangFile = new File(getDataFolder(), "RarityLang.yml");
        if (!rarityLangFile.exists()) {
            saveResource("RarityLang.yml", false);
        }
        FileConfiguration rarityLangConfig = YamlConfiguration.loadConfiguration(rarityLangFile);

        ItemRegistry.loadRarityLang(this);
        ItemRegistry.initOneTimeCraftUtils(this);
        Bukkit.getPluginManager().registerEvents(new ItemRegistry(), this);

        ItemRegistry.initialize(this);
        CommandRegistry.initialize(this);
        EventRegistry.initialize(this);
        FoodRegistry.initialize(this);

        if (!EntitySizeUtils.ENABLED) {
            getLogger().warning("EntitySizeUtils is disabled because the EntitySize plugin is missing.");
        }

        if (!ProjectileUtils.isModelEngineEnabled()) {
            getLogger().warning("ModelEngine is missing. Custom model projectiles in ProjectileUtils are disabled.");
        }

        LegendaryItemCraftListener craftListener = new LegendaryItemCraftListener(this);
        Bukkit.getPluginManager().registerEvents(craftListener, this);
        Bukkit.getPluginManager().registerEvents(new FXItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new FXFoodListener(this), this);

        // Register command executors and tab completers
        getCommand("fxgive").setExecutor(this);
        getCommand("fxgive").setTabCompleter(this);
        getCommand("fxreload").setExecutor(this);
        getCommand("fxreload").setTabCompleter(this);
        getCommand("fxutils").setExecutor(this);
        getCommand("fxutils").setTabCompleter(this);
        getCommand("nv").setExecutor(this);
        getCommand("nightvision").setExecutor(this);
        getCommand("fxitems").setExecutor(this);

        getLogger().info("Custom items and behaviors registered successfully!");
        getLogger().info(ChatColor.GREEN + "FXItems has been enabled!");
    }

    public static FXItems getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        getLogger().info("FXItems disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        String commandName = command.getName().toLowerCase();
        switch (commandName) {
            case "fxitems" -> {
                new com.noctify.Main.GUIs.FXItem(this).openMainMenu(player);
                return true;
            }
            case "fxreload" -> {
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                    return true;
                }

                if (args.length == 0) {
                    reloadConfig();
                    com.noctify.Main.Utils.CooldownUtils.clearAllCooldowns();
                    player.sendMessage(ChatColor.GREEN + "FXItems reloaded! Config and Items have been reloaded.");
                } else if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "config" -> {
                            reloadConfig();
                            player.sendMessage(ChatColor.GREEN + "Config reloaded!");
                        }
                        case "cooldowns" -> {
                            com.noctify.Main.Utils.CooldownUtils.clearAllCooldowns();
                            player.sendMessage(ChatColor.GREEN + "All cooldowns have been reset!");
                        }
                        default -> player.sendMessage(ChatColor.RED + "Invalid argument. Use /fxreload [config|cooldowns].");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /fxreload [config|cooldowns]");
                }
            }
            case "fxgive" -> {
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /fxgive {player} {item} [count]");
                    return true;
                }

                String targetSelector = args[0];
                String itemId = args[1].toLowerCase();
                int count = 1;

                if (args.length >= 3) {
                    try {
                        count = Integer.parseInt(args[2]);
                        if (count <= 0) {
                            player.sendMessage(ChatColor.RED + "Count must be a positive number.");
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Invalid count. Please provide a valid number.");
                        return true;
                    }
                }

                List<Player> targets = resolvePlayers(sender, targetSelector);
                if (targets.isEmpty()) {
                    player.sendMessage(ChatColor.RED + "No players found for selector: " + targetSelector);
                    return true;
                }

                ItemStack baseItem = ItemRegistry.getCustomItem(itemId);
                if (baseItem == null) {
                    baseItem = FoodRegistry.getFoodItem(itemId); // Try food registry if not found in item registry
                }
                if (baseItem == null) {
                    player.sendMessage(ChatColor.RED + "Item or food with ID '" + itemId + "' not found!");
                    return true;
                }

                for (Player target : targets) {
                    ItemStack item = baseItem.clone();
                    item.setAmount(count);
                    target.getInventory().addItem(item);
                    target.sendMessage(ChatColor.GOLD + "You have received " + count + "x " + itemId + "!");
                }

                player.sendMessage(ChatColor.GREEN + "Gave " + count + "x " + itemId + " to " + targets.size() + " player(s).");
                return true;
            }
            case "fxutils" -> {
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                    return true;
                }

                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Usage: /fxutils <subcommand>");
                    return true;
                }

                String subcommand = args[0].toLowerCase();
                switch (subcommand) {
                    case "cleararmor" -> {
                        AttributeInstance armorAttribute = player.getAttribute(Attribute.ARMOR);
                        if (armorAttribute != null) {
                            armorAttribute.setBaseValue(0); // Reset armor points to 0
                            player.sendMessage(ChatColor.GREEN + "Your armor points have been reset to 0!");
                        } else {
                            player.sendMessage(ChatColor.RED + "Unable to reset armor points.");
                        }
                    }
                    default -> player.sendMessage(ChatColor.RED + "Unknown subcommand. Available: cleararmor");
                }
            }
            case "nv", "nightvision" -> {
                boolean hasNightVision = player.hasMetadata("night_vision");

                if (hasNightVision) {
                    // Disable night vision
                    player.removeMetadata("night_vision", FXItems.getInstance());
                    player.removePotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION); // Remove night vision effect
                    player.sendMessage(ChatColor.RED + "Night vision disabled.");
                } else {
                    // Enable night vision
                    player.setMetadata("night_vision", new org.bukkit.metadata.FixedMetadataValue(FXItems.getInstance(), true));
                    player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false)); // Add night vision effect
                    player.sendMessage(ChatColor.GREEN + "Night vision enabled.");
                }
                return true;
            }
            default -> {
                return false;
            }
        }

        return true;
    }

    private List<Player> resolvePlayers(CommandSender sender, String selector) {
        List<Player> players = new ArrayList<>();

        switch (selector) {
            case "@a": // All players
                players.addAll(Bukkit.getOnlinePlayers());
                break;
            case "@p": // Nearest player
                if (sender instanceof Player player) {
                    Player nearest = player.getWorld().getPlayers().stream()
                            .filter(p -> !p.equals(player))
                            .min((p1, p2) -> Double.compare(p1.getLocation().distance(player.getLocation()), p2.getLocation().distance(player.getLocation())))
                            .orElse(null);
                    if (nearest != null) players.add(nearest);
                }
                break;
            case "@s": // Command sender
                if (sender instanceof Player player) {
                    players.add(player);
                }
                break;
            case "@r": // Random player
                List<? extends Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                if (!onlinePlayers.isEmpty()) {
                    Player randomPlayer = onlinePlayers.get(new Random().nextInt(onlinePlayers.size()));
                    players.add(randomPlayer);
                }
                break;
            case "@e": // All entities (filtering only players for this example)
                if (sender instanceof Player player) {
                    players.addAll(player.getWorld().getPlayers());
                }
                break;
            default: // Specific player name
                Player target = Bukkit.getPlayer(selector);
                if (target != null) players.add(target);
                break;
        }

        return players;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName().toLowerCase();

        switch (commandName) {
            case "fxgive" -> {
                if (args.length == 1) {
                    // Suggest player selectors and online player names
                    List<String> suggestions = new ArrayList<>(List.of("@a", "@p", "@s", "@r"));
                    Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
                    return suggestions;
                } else if (args.length == 2) {
                    // Suggest both custom item IDs and custom food IDs
                    Set<String> suggestions = new HashSet<>();
                    suggestions.addAll(ItemRegistry.getItemIds());
                    suggestions.addAll(FoodRegistry.getFoodIds());
                    return new ArrayList<>(suggestions);
                } else if (args.length == 3) {
                    // Suggest common counts
                    return List.of("1", "10", "64");
                }
            }
            case "fxreload" -> {
                if (args.length == 1) {
                    return List.of("config", "cooldowns");
                }
            }
            case "fxutils" -> {
                if (args.length == 1) {
                    return List.of("cleararmor");
                }
            }
        }
        return null;
    }
}