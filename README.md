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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MyCustomSword {
    public static ItemStack createItem() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aMy Epic Sword");
        item.setItemMeta(meta);
        return item;
    }
    // Optionally add getRecipe(), getLore(), etc.
}
```

2. **Register the Item in ItemRegistry**:
   In `ItemRegistry.initialize()`:
```java
registerItem("MyCustomSword", MyCustomSword.class);
addRecipe(plugin, MyCustomSword.class);
```

3. **(Optional) Add an Item Behavior**:
   Create a class in `com.noctify.Custom.ItemBehavior` and register it:
```java
registerBehavior(plugin, "MyCustomSword", MyCustomSwordBehavior.class);
```

---

### Custom Foods

To create a new food item:

1. **Create a Food Class** in `com.noctify.Custom.Foods`:

```java
package com.noctify.Custom.Foods;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.List;
import java.util.Collections;

public class MagicApple {
    public static ItemStack createItem() {
        ItemStack item = new ItemStack(Material.APPLE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§bMagic Apple");
        item.setItemMeta(meta);
        return item;
    }
    public static int getHungerPoints() { return 6; }
    public static float getSaturationPoints() { return 9.6f; }
    public static List<PotionEffect> getEffects() {
        return Collections.singletonList(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
    }
    // Add getRecipe(Plugin, NamespacedKey) as shown in samples
}
```

2. **Register the Food in FoodRegistry**:
   In `FoodRegistry.initialize()`:
```java
registerFood(plugin, "MagicApple", MagicApple.class, null);
```
You can also add a custom behavior by providing a behavior class in `OtherBehaviors`.

---

### Custom Item Behaviors

Custom item behaviors are event listeners that give special abilities to your items.

**Example: Right-click ability with cooldown**
```java
package com.noctify.Custom.ItemBehavior;

import com.noctify.Main.Utils.CooldownUtils;
import com.noctify.Main.Utils.CustomItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import java.util.UUID;

public class MySwordBehavior implements Listener {
    public MySwordBehavior(Plugin plugin) {}
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!CustomItemUtils.isCustomItem(event.getItem(), Material.DIAMOND_SWORD, "&aMy Epic Sword")) return;
        if (CooldownUtils.isOnCooldown(player.getUniqueId(), "MySwordAbility")) {
            CooldownUtils.sendCooldownMessage(player, "MySwordAbility", CooldownUtils.getRemainingCooldown(player.getUniqueId(), "MySwordAbility"));
            return;
        }
        // Ability logic here
        player.sendMessage("§bSword power activated!");
        CooldownUtils.setCooldown(player.getUniqueId(), "MySwordAbility", 30);
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
