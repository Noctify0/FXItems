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
    private static String fxItemsGuiTitle;
    private FileConfiguration langConfig;
    private File oneTimeCraftsFile;
    private FileConfiguration oneTimeCraftsConfig;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        File rarityLangFile = new File(getDataFolder(), "RarityLang.yml");
        if (!rarityLangFile.exists()) {
            saveResource("RarityLang.yml", false);
        }
        FileConfiguration rarityLangConfig = YamlConfiguration.loadConfiguration(rarityLangFile);

        File langFile = new File(getDataFolder(), "Lang.yml");
        if (!langFile.exists()) {
            saveResource("Lang.yml", false);
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        oneTimeCraftsFile = new File(getDataFolder(), "OneTimeCrafts.yml");
        if (!oneTimeCraftsFile.exists()) {
            saveResource("OneTimeCrafts.yml", false);
        }
        oneTimeCraftsConfig = YamlConfiguration.loadConfiguration(oneTimeCraftsFile);

        fxItemsGuiTitle = langConfig.getString("CraftingGUI.fxitems_gui_title", "§8§lFXItems");

        ItemRegistry.loadRarityLang(this);
        ItemRegistry.initOneTimeCraftUtils(this);
        Bukkit.getPluginManager().registerEvents(new ItemRegistry(), this);
        Bukkit.getPluginManager().registerEvents(new com.noctify.Main.GUIs.FXItem(this), this);

        ItemRegistry.initialize(this);
        CommandRegistry.initialize(this);
        EventRegistry.initialize(this);
        FoodRegistry.initialize(this);
        ArmorRegistry.initialize(this);

        if (!EntitySizeUtils.ENABLED) {
            getLogger().warning("EntitySizeUtils is disabled because the EntitySize plugin is missing.");
        }

        if (!ProjectileUtils.isModelEngineEnabled()) {
            getLogger().warning("ModelEngine is missing. Custom model projectiles in ProjectileUtils are disabled.");
        }

        LegendaryItemCraftListener craftListener = new LegendaryItemCraftListener(this, oneTimeCraftsFile, oneTimeCraftsConfig);
        Bukkit.getPluginManager().registerEvents(craftListener, this);
        Bukkit.getPluginManager().registerEvents(new FXItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new FXFoodListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FXArmorListener(), this);

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
        getCommand("fxhelp").setExecutor(this);
        getCommand("fxhelp").setTabCompleter(this);

        getLogger().info("Custom items and behaviors registered successfully!");
        getLogger().info(ChatColor.GREEN + "FXItems has been enabled!");
    }

    public File getOneTimeCraftsFile() { return oneTimeCraftsFile; }

    public FileConfiguration getOneTimeCraftsConfig() { return oneTimeCraftsConfig; }

    public static String getFxItemsGuiTitle() { return fxItemsGuiTitle; }

    public static FXItems getInstance() {
        return instance;
    }

    private String getHelpMessage() {
        String msg = langConfig.getString("Commands.help_message", "&cHelp message not set.");
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Override
    public void onDisable() {
        getLogger().info("FXItems disabled!");
    }

    private String getLangMessage(String path) {
        String msg = langConfig.getString("Commands." + path, "&cMessage not set: " + path);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getLangMessage("only_players"));
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
                    player.sendMessage(getLangMessage("no_permission"));
                    return true;
                }

                if (args.length == 0) {
                    reloadConfig();

                    // Reload Lang.yml
                    File langFile = new File(getDataFolder(), "Lang.yml");
                    if (!langFile.exists()) {
                        saveResource("Lang.yml", false);
                    }
                    langConfig = YamlConfiguration.loadConfiguration(langFile);
                    fxItemsGuiTitle = langConfig.getString("CraftingGUI.fxitems_gui_title", "§8§lFXItems");

                    // Reload RarityLang.yml
                    File rarityLangFile = new File(getDataFolder(), "RarityLang.yml");
                    if (!rarityLangFile.exists()) {
                        saveResource("RarityLang.yml", false);
                    }
                    ItemRegistry.loadRarityLang(this);

                    com.noctify.Main.Utils.CooldownUtils.clearAllCooldowns();
                    player.sendMessage(getLangMessage("config_reloaded"));
                } else if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "config" -> {
                            reloadConfig();
                            player.sendMessage(getLangMessage("config_reloaded"));
                        }
                        case "cooldowns" -> {
                            com.noctify.Main.Utils.CooldownUtils.clearAllCooldowns();
                            player.sendMessage(getLangMessage("cooldowns_reset"));
                        }
                        default -> player.sendMessage(getLangMessage("invalid_argument"));
                    }
                } else {
                    player.sendMessage(getLangMessage("usage_fxreload"));
                }
            }
            case "fxgive" -> {
                if (!player.isOp()) {
                    player.sendMessage(getLangMessage("no_permission"));
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(getLangMessage("usage_fxgive"));
                    return true;
                }

                String targetSelector = args[0];
                String itemId = args[1].toLowerCase();
                int count = 1;

                if (args.length >= 3) {
                    try {
                        count = Integer.parseInt(args[2]);
                        if (count <= 0) {
                            player.sendMessage(getLangMessage("count_positive"));
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(getLangMessage("invalid_count"));
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
                    baseItem = FoodRegistry.getFoodItem(itemId);
                }
                if (baseItem == null) {
                    baseItem = ArmorRegistry.getCustomArmor(itemId);
                }
                if (baseItem == null) {
                    player.sendMessage(ChatColor.RED + "Item, food, or armor with ID '" + itemId + "' not found!");
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
                    player.sendMessage(getLangMessage("no_permission"));
                    return true;
                }

                if (args.length == 0) {
                    player.sendMessage(getLangMessage("usage_fxutils"));
                    return true;
                }

                String subcommand = args[0].toLowerCase();
                switch (subcommand) {
                    case "cleararmor" -> {
                        AttributeInstance armorAttribute = player.getAttribute(Attribute.ARMOR);
                        if (armorAttribute != null) {
                            armorAttribute.setBaseValue(0);
                            player.sendMessage(getLangMessage("clear_armor_success"));
                        } else {
                            player.sendMessage(getLangMessage("clear_armor_fail"));
                        }
                    }
                    default -> player.sendMessage(getLangMessage("unknown_subcommand"));
                }
            }
            case "fxhelp" -> {
                sender.sendMessage(getHelpMessage().split("\n"));
                return true;
            }
            case "nv", "nightvision" -> {
                boolean hasNightVision = player.hasMetadata("night_vision");

                if (hasNightVision) {
                    player.removeMetadata("night_vision", FXItems.getInstance());
                    player.removePotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION);
                    player.sendMessage(getLangMessage("night_vision_disabled"));
                } else {
                    player.setMetadata("night_vision", new org.bukkit.metadata.FixedMetadataValue(FXItems.getInstance(), true));
                    player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
                    player.sendMessage(getLangMessage("night_vision_enabled"));
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
                    Set<String> suggestions = new HashSet<>();
                    suggestions.addAll(ItemRegistry.getItemIds());
                    suggestions.addAll(FoodRegistry.getFoodIds());
                    suggestions.addAll(ArmorRegistry.getArmorIds()); // Add this line
                    return new ArrayList<>(suggestions);
                }
                else if (args.length == 3) {
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
            case "fxhelp" -> {
                return List.of();
            }
        }
        return null;
    }
}