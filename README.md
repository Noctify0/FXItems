# FXItems Plugin

FXItems is a modular, extensible Minecraft plugin for Spigot/Bukkit servers that allows you to create **custom items**, **foods**, **commands**, **events**, and **gameplay utilities** with ease. It is designed to be both user- and developer-friendly, providing a robust system for adding new content and customizing server gameplay.

---

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Getting Started](#getting-started)
- [Plugin Architecture](#plugin-architecture)
  - [Registry Classes](#registry-classes)
  - [Sample Items, Foods, and Behaviors](#sample-items-foods-and-behaviors)
- [Creating Custom Content](#creating-custom-content)
  - [Custom Items](#custom-items)
  - [Custom Foods](#custom-foods)
  - [Custom Item Behaviors](#custom-item-behaviors)
  - [Custom Commands](#custom-commands)
  - [Custom Events](#custom-events)
- [Utility Classes (Utils)](#utility-classes-utils)
- [Main Plugin Commands](#main-plugin-commands)
- [Advanced Topics](#advanced-topics)
- [Contributing](#contributing)

---

## Features

- **Custom Items**: Easily create new items with custom recipes, names, and behaviors.
- **Custom Foods**: Add new food items with hunger/saturation values, recipes, and effects.
- **Custom Commands**: Add server/player commands with tab-completion and subcommands.
- **Custom Events**: Add event listeners for in-game actions and custom logic.
- **Extensive Utility Library**: Utilities for cooldowns, mana, effects, projectiles, teleportation, and more.
- **One-Time Crafting**: Support for items that can only be crafted once per server.
- **Highly Modular**: Drop-in new classes for instant plugin extension.

---

## Installation

1. **Download the JAR** and place it in your server's `plugins` directory.
2. **Restart your server** to generate the default configuration and directories.
3. **(Optional)**: If you want to develop your own custom content, clone this repository and follow the instructions below.

---

## Getting Started

When the plugin runs for the first time, it initializes all registries and loads sample items, foods, commands, and events. All code is organized in a modular package structure:

```
src/main/java/com/noctify/
    Custom/
        Commands/
        Events/
        Foods/
        ItemAttributes/
        ItemBehavior/
        OtherBehaviors/
    Main/
        Commands/
        Listeners/
        Utils/
```

---

## Plugin Architecture

The core of FXItems is centered on four registry classes:

- **ItemRegistry**: Manages custom items and their registration.
- **FoodRegistry**: Manages custom foods, their effects, and recipes.
- **CommandRegistry**: Handles registration and execution of custom commands.
- **EventRegistry**: Registers custom event listeners.

Each registry provides static methods for initializing and registering new content. All you need to do is create a new class and add a registration line in the appropriate registry.

---

### Registry Classes

#### ItemRegistry

- Registers new items with unique attributes and recipes.
- Associates item behaviors (custom ability/event handlers).
- Stores all custom items for retrieval and use.

#### FoodRegistry

- Registers new food items with custom hunger, saturation, effects, and recipes.
- Registers custom behaviors when food is consumed.

#### CommandRegistry

- Registers new commands by class name.
- Supports subcommands and tab-completion.

#### EventRegistry

- Registers custom event listeners for in-game actions.

---

## Sample Items, Foods, and Behaviors

Every custom item/food/command/event is provided as a sample in its respective package.

- **ExampleCommand**: Shows how to implement a command with subcommands and tab completion.
- **ExampleItemBehavior**: Shows how to implement a custom ability triggered by item interaction.
- **LegendaryItemCraftListener**: Shows how to restrict certain items to one-time crafting.
- **Foods and ItemAttributes**: (You should provide your own, or extend the samples.)

---

## Dependencies

FXItems requires the following dependencies to function correctly:

### Required

- **Spigot or PaperMC** (Minecraft server API)  
  *FXItems is built for Spigot and compatible PaperMC servers. You must be running a Spigot or PaperMC-based server.*

- **Java 17 or newer**  
  *The plugin requires Java 17+ for compilation and runtime.*

- **EntitySize**  
  *Entity resizing features will not work without this (`EntitySizeUtils`). The plugin works fine without it, but those features will be disabled.*

### Optional (for extended features)

- **ModelEngine**  
  *Required only if you want to use custom model projectiles with the ModelEngine API. If not installed, standard projectiles will be used instead.*

- **PlaceholderAPI**  
  *Required only if you use placeholders in your custom extensions or want integration with PlaceholderAPI.*

> **Note:**  
> **Core features** (custom items, foods, commands, events, and utilities) require only Spigot/PaperMC and Java 17+.

---

## Creating Custom Content

### Custom Items

To create a new custom item:

1. **Create an Item Attribute Class** in `com.noctify.Custom.ItemAttributes`:

```java
package com.noctify.Custom.ItemAttributes;

import com.noctify.Main.Utils.OneTimeCraftUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class ExampleItem {

    public static ItemStack createItem() {

        // Create the item name, lore and properties

        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Set the display name
            meta.setDisplayName("§aExample Sword");
            // Set the lore
            meta.setLore(Arrays.asList(
                    "§7A sword for demonstration purposes.",
                    "",
                    "§6ʟᴇɢᴇɴᴅᴀʀʏ",
                    "§fAbilities:",
                    "§fExample Power: §7Right Click to activate.",
                    "§830s cooldown"
            ));
            // Set the item to be unbreakable
            meta.setUnbreakable(true);
            // Hide the unbreakable flag
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

            // Set a custom item model for the item
            NamespacedKey modelKey = NamespacedKey.minecraft("example_sword");
            meta.setItemModel(modelKey);
            // Apply the custom attributes to the item
            item.setItemMeta(meta);
        }
        return item;
    }

    // Create a crafting recipe for the item
    public static ShapedRecipe getRecipe(Plugin plugin, NamespacedKey key) {
        ItemStack exampleItem = createItem();
        ShapedRecipe recipe = new ShapedRecipe(key, exampleItem);
        // Define the shape of the recipe
        recipe.shape(" E ", "DSD", " S ");
        // Set the ingredients for the recipe
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('S', Material.STICK);

        // If you want the item to only be able to be crafted once, you can use the OneTimeCraftUtils
        // This can especially be useful in competetive smp servers where you want to limit the number of powerful items
        // Make sure to import the OneTimeCraftUtils class from the Main.Utils package
        // import com.noctify.Main.Utils.OneTimeCraftUtils;
        //
        // Register the item as a one-time craftable item syntax:
        // It does this will the following two lines:
        //
        // OneTimeCraftUtils utils = new OneTimeCraftUtils(plugin);
        // utils.registerCraft("example_item", exampleItem);

        return recipe;
    }
}
```

2. **Register the Item in ItemRegistry**:
   In `ItemRegistry.initialize()`:
```java
registerItem("example_item", ExampleItem.class);
addRecipe(plugin, ExampleItem.class);
```

3. **(Optional) Add an Item Behavior**:
   Create a class in `com.noctify.Custom.ItemBehavior` and register it:
```java
registerBehavior(plugin, "MyCustomSword", MyCustomSwordBehavior.class);
```

  Example Behavior:
```java
package com.noctify.Custom.ItemBehavior;

import com.noctify.Main.Utils.CooldownUtils;
import com.noctify.Main.Utils.CustomItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ExampleItemBehavior implements Listener {

    public ExampleItemBehavior(Plugin plugin) {
        // Constructor must be present to register the listener
        // Can be left blank
    }

    @EventHandler
    public void onPlayerUseExampleItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // The next line checks if the item is a custom item with specific attributes,
        // So the ability will only trigger for this specific item
        // and not for any other item with the same material
        // Read the syntax of the line to know how to use it

        if (!CustomItemUtils.isCustomItem(item, Material.DIAMOND_SWORD, "&aExample Sword")) {
            return;
        }

        // Ability activation logic
        // Using Right Click Action to trigger the ability
        // You can change the action to LEFT_CLICK_AIR or LEFT_CLICK_BLOCK if needed
        // To make abilities you will have to know how to code in Java and work with Bukkit API
        // If you don't know how to code, you can ask AI to help you with the code, ChatGPT is a good option

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            UUID playerId = player.getUniqueId();
            String ability = "ExamplePower";
            int cooldownTime = 30;

            if (CooldownUtils.isOnCooldown(playerId, ability)) {
                double timeLeft = CooldownUtils.getRemainingCooldown(playerId, ability);
                CooldownUtils.sendCooldownMessage(player, ability, timeLeft);
                return;
            }

            player.sendMessage("§bYou activated Example Power!");
            CooldownUtils.setCooldown(playerId, ability, cooldownTime);
        }
    }
}
```

---

### Custom Foods

To create a new food item:

1. **Create a Food Class** in `com.noctify.Custom.Foods`:

```java
package com.noctify.Custom.Foods;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExampleFood {

    // Creates the custom food item
    // Check the OtherBehaviors package to see how to implement custom behaviors

    public static ItemStack createItem() {
        ItemStack foodItem = new ItemStack(Material.COOKED_BEEF); // Example material
        ItemMeta meta = foodItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Sample Food");
            meta.setLore(Arrays.asList(
                    "§7This is a sample custom food.",
                    "§7Use this as a template for creating your own."
            ));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            foodItem.setItemMeta(meta);
        }
        return foodItem;
    }

    // Defines the hunger points restored by the food
    public static int getHungerPoints() {
        return 4; // Example value for hunger points
    }

    // Defines the saturation points restored by the food
    public static float getSaturationPoints() {
        return 2.5f; // Example value for saturation
    }

    // Defines any potion effects applied when the food is consumed
    public static List<PotionEffect> getEffects() {
        return Collections.singletonList(
                new PotionEffect(PotionEffectType.SPEED, 200, 1) // Example effect: Speed for 10 seconds
        );
    }

    // If don't want any effects, return an empty list
    //
    // Example:
    //
    // public static List<PotionEffect> getEffects() {
    //     return Collections.emptyList();
    // }

    // Defines the crafting recipe for the food
    public static ShapedRecipe getRecipe(Plugin plugin, NamespacedKey key) {
        ItemStack foodItem = createItem();
        ShapedRecipe recipe = new ShapedRecipe(key, foodItem);
        recipe.shape(" A ", " B ", " C ");
        recipe.setIngredient('A', Material.APPLE);
        recipe.setIngredient('B', Material.BREAD);
        recipe.setIngredient('C', Material.COOKED_BEEF);
        return recipe;
    }
}
```

2. **Register the Food in FoodRegistry**:
   In `FoodRegistry.initialize()`:
```java
registerFood(plugin, "ExampleFood", ExampleFood.class, ExampleFoodBehavior.class);
```
You can also add a custom behavior by providing a behavior class in `OtherBehaviors`.

---

### Custom Food Behaviors

Custom item behaviors are event listeners that give special abilities to your items.
Put in package:
package com.noctify.Custom.OtherBehaviors;

**Example Food Behavior:**
```java
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
```

---

### Custom Commands

To create a new command:

1. **Create a Command Class** in `com.noctify.Custom.Commands`:

```java
package com.noctify.Custom.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage("Hello from MyCommand!");
        }
        return true;
    }
}
```

2. **Register the Command** in `CommandRegistry.initialize()`:
```java
registerCommand(plugin, "MyCommand", MyCommand.class);
```

---

### Custom Events

To add a new event listener:

1. **Create a Listener Class** in `com.noctify.Custom.Events`:

```java
package com.noctify.Custom.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class WelcomeEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Welcome to the server!");
    }
}
```

2. **Register the Event** in `EventRegistry.initialize()`:
```java
registerEvent(plugin, "WelcomeEvent");
```

---

## Utility Classes (Utils)

FXItems ships with powerful utility classes to make development easier:

### CooldownUtils

- Manage per-player cooldowns for abilities.
- Example:
```java
// Set a cooldown
CooldownUtils.setCooldown(player.getUniqueId(), "SomeAbility", 30);
// Check if on cooldown
if (CooldownUtils.isOnCooldown(player.getUniqueId(), "SomeAbility")) { /* ... */ }
// Get remaining cooldown
double seconds = CooldownUtils.getRemainingCooldown(player.getUniqueId(), "SomeAbility");
```

### CustomItemUtils

- Check if an item matches a material and display name.
- Example:
```java
if (CustomItemUtils.isCustomItem(item, Material.DIAMOND_SWORD, "&aMagic Sword")) { /* ... */ }
```

### EffectUtils

- Add or remove potion effects while a player is holding a specific item.
- Example:
```java
EffectUtils.addEffectWhileHolding(player, Material.BLAZE_ROD, PotionEffectType.FIRE_RESISTANCE, 200, 1);
EffectUtils.removeEffectTask(player);
```

### ManaUtils

- Give players a mana bar and manage mana usage and regeneration.
- Example:
```java
ManaUtils.useMana(player, 10); // Spend 10 mana
ManaUtils.setMana(player, 100); // Set mana to max
int mana = ManaUtils.getMana(player);
```

### OneTimeCraftUtils

- Register items that can only be crafted once per server.
- The `LegendaryItemCraftListener` uses this utility to enforce unique crafting.

### EntitySizeUtils

- Change entity or player size if the `EntitySize` plugin is present.
- Example:
```java
EntitySizeUtils.setSize(player, 2.0f); // Double the size
EntitySizeUtils.resetSize(player);     // Reset to normal
```

### ProjectileUtils

- Create custom projectiles with custom damage, particles, and behaviors.
- Example:
```java
ProjectileUtils.createCustomProjectile(plugin, player, Particle.FLAME, 2, true, false, 1, false, 2.0, 8.0, false, EntityType.ARROW, false, "", 60, 0);
```

### TeleportUtils

- Teleport players safely, with particle and sound effects.
- Example:
```java
TeleportUtils.teleportPlayer(player, targetLocation, 40, true, true, true);
```

---

## Main Plugin Commands

FXItems provides several built-in commands for server operators and players:

- `/fxreload [config|cooldowns]` — Reload plugin configuration and cooldowns.
- `/fxgive <player> <item> [count]` — Give a custom item to a player.
- `/fxutils cleararmor` — Reset player armor points to zero.
- `/nv` or `/nightvision` — Toggle night vision for a player (if permissions).
- `/example` — Sample command with subcommands (`info`, `help`, `greet`).

---

## Advanced Topics

### One-Time Crafting

You can make any custom item "legendary" by registering it with `OneTimeCraftUtils`. Only one of these items can ever be crafted on the server. See `LegendaryItemCraftListener` and the samples for implementation.

### Tab Completion

Commands can implement the `TabCompleter` interface for improved usability and suggestions.

### Extending Utility Classes

All utils are static or singleton, so you can use them anywhere in your plugin logic.

---

## Contributing

1. Fork the repository.
2. Add your new items, commands, foods, or utilities in the appropriate package.
3. Register them in the corresponding registry's `initialize()` method.
4. Submit a pull request!

---

## License

MIT License

---

## Credits

- Developed by [Noctify0](https://github.com/Noctify0)
- Special thanks to Bukkit/Spigot community and all open-source contributors!

---

**Happy coding and have fun customizing your Minecraft server!**
