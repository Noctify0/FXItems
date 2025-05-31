## Wiki:
- https://github.com/Noctify0/FXItems/wiki

# FXItems Plugin

FXItems is a modular, extensible Minecraft plugin for Spigot/Bukkit servers that empowers you to create custom items, foods, commands, events, and gameplay utilities with ease. Designed for both players and developers, FXItems features a robust system for adding new content and customizing gameplay—no core edits or complicated setup required.

---

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Getting Started](#getting-started)
- [Plugin Architecture](#plugin-architecture)
- [Registry Classes](#registry-classes)
- [Auto-Registration System](#auto-registration-system)
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
- [Changelog & Migration Guide](#changelog--migration-guide)
- [Contributing](#contributing)

---

## Features

- **Custom Items:** Easily create new items with unique recipes, names, and behaviors.
- **Custom Foods:** Add new foods with custom hunger/saturation, recipes, and effects.
- **Custom Commands:** Add server/player commands with subcommands and tab-completion.
- **Custom Events:** Listen for and react to in-game actions with custom logic.
- **Extensive Utility Library:** Utilities for cooldowns, mana, effects, projectiles, teleportation, entity size, and more.
- **One-Time Crafting:** Support for unique, legendary items that can only be crafted once per server.
- **Highly Modular & Auto-Registration:** Drop-in new classes for instant extension—no config or manual registration required!
- **Addon/API Support:** Public API for creating your own plugins and integrations.
- **Auto-Registration System:** Just place your classes in the right package—FXItems will auto-detect and register them at startup.

---

## Installation

1. Download the FXItems JAR and place it in your server's `plugins` directory.
2. Restart your server to generate configuration and data folders.
3. _(Optional)_ For development, clone this repository and follow the instructions below to create your own content.

---

## Getting Started

Upon first run, FXItems initializes all registries and loads sample items, foods, commands, and events.  
Content is organized in a modular package structure:

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

See [A | Getting Started & Plugin Overview](https://github.com/Noctify0/FXItems/wiki/A-%7C-Getting-Started-&-Plugin-Overview) for a beginner’s walkthrough.

---

## Plugin Architecture

The core of FXItems is built around four registry classes:

- **ItemRegistry:** Manages custom items and their registration.
- **FoodRegistry:** Handles custom foods, their effects, and recipes.
- **CommandRegistry:** Handles registration and execution of custom commands.
- **EventRegistry:** Registers custom event listeners.

Each registry provides static methods for initializing and registering content.  
Just create a new class and add a registration line in the appropriate registry—no manual config editing!

---

## Registry Classes

- **ItemRegistry:** Registers new items with unique attributes, recipes, and behaviors. Provides retrieval for use in commands, events, etc.
- **FoodRegistry:** Registers foods with custom hunger/saturation, effects, and recipes. Supports custom consumption behaviors.
- **CommandRegistry:** Registers new commands (with class name), supports subcommands and tab-completion.
- **EventRegistry:** Registers custom event listeners for in-game logic and triggers.

---

## Auto-Registration System

**New in v1.2+!**  
FXItems now automatically detects and registers your custom items, foods, commands, and events at startup—no manual registration required!  
Just place your classes in the correct `com.noctify.Custom` sub-package (like `ItemAttributes`, `Foods`, `Commands`, or `Events`).  
FXItems will automatically scan, instantiate, and register them when the server starts.

- See [S | Auto-Registration System](https://github.com/Noctify0/FXItems/wiki/S-%7C-Auto-Registration-System) for details and troubleshooting.
- Manual registration via the API is still supported and can be used alongside auto-registration for advanced cases.

---

## Sample Items, Foods, and Behaviors

Every item, food, command, and event ships with at least one sample in its package:

- `ExampleCommand`: How to implement a command with subcommands and tab completion.
- `ExampleItemBehavior`: How to trigger a custom ability from an item.
- `LegendaryItemCraftListener`: How to restrict certain items to one-time crafting.
- Foods and ItemAttributes: Use or extend the samples to quickly add your own content.

---

## Creating Custom Content

### Custom Items

1. **Create an Item Attribute Class** in `com.noctify.Custom.ItemAttributes`.
2. **(Preferred)** Place it in the correct package for auto-registration, or register in `ItemRegistry.initialize()` for manual control.
3. **(Optional)** Add an Item Behavior in `com.noctify.Custom.ItemBehavior` for custom interaction logic.

### Custom Foods

1. **Create a Food Class** in `com.noctify.Custom.Foods`.
2. **(Preferred)** Place it in the correct package for auto-registration, or register in `FoodRegistry.initialize()` for manual control.
3. **(Optional)** Add a custom behavior in `OtherBehaviors`.

### Custom Item Behaviors

- Implement listeners for abilities, e.g., right-click actions, special cooldowns, or effects.
- Example: See `ExampleItemBehavior` for cooldown-based right-click abilities.

### Custom Commands

- Implement `CommandExecutor` classes in `com.noctify.Custom.Commands`.
- Register with `registerCommand(plugin, "MyCommand", MyCommand.class);` in `CommandRegistry` or just place in the right package.

### Custom Events

- Create Bukkit `Listener` classes in `com.noctify.Custom.Events` and register via `registerEvent(plugin, "WelcomeEvent");` in `EventRegistry`, or just place in the right package.

---

## Utility Classes (Utils)

FXItems includes utility classes for advanced features.  
**As of v1.2+, all utilities are accessible via the public API (`UtilsAPI`).**

- **CooldownUtils:** Per-player ability cooldowns.
- **CustomItemUtils:** Check if an item matches a material and display name.
- **EffectUtils:** Add/remove potion effects while holding items.
- **ManaUtils:** Player mana bar, usage, and regeneration.
- **OneTimeCraftUtils:** Register legendary items that can only be crafted once per server.
- **EntitySizeUtils:** Change entity/player size (requires EntitySize plugin).
- **ProjectileUtils:** Custom projectiles with custom damage, particles, and ModelEngine support.
- **TeleportUtils:** Safe teleportation with particles and sound.

See [Utility Reference](https://github.com/Noctify0/FXItems/wiki) and [Q | FXItems API & Making Addons](https://github.com/Noctify0/FXItems/wiki/Q-%7C-FXItems-API-%28com.noctify.API%29-&-Making-Addons) for full API usage.

---

## Main Plugin Commands

- `/fxreload [config|cooldowns]` — Reload plugin configuration and cooldowns.
- `/fxgive <player> <item> [count]` — Give a custom item to a player.
- `/fxutils cleararmor` — Reset player armor points to zero.
- `/nv` or `/nightvision` — Toggle night vision for a player (if permission).
- `/example` — Sample command with subcommands (info, help, greet).

See [Main Plugin Commands](https://github.com/Noctify0/FXItems/wiki/A-%7C-Getting-Started-&-Plugin-Overview#main-plugin-commands) for full details.

---

## Advanced Topics

- **One-Time Crafting:** Register unique items with `OneTimeCraftUtils`. Only one can ever be crafted on the server.
- **Tab Completion:** Implement `TabCompleter` for your commands.
- **Extending Utils:** All utilities can be used in your own plugins/addons for maximum power.
- **API & Addons:** See [Q | FXItems API & Making Addons](https://github.com/Noctify0/FXItems/wiki/Q-%7C-FXItems-API-%28com.noctify.API%29-&-Making-Addons) for public API usage and guides.
- **Migration Guide:** Upgrading from v1.1? See [T | Migration Guide (v1.1 → v1.2+)](https://github.com/Noctify0/FXItems/wiki/T-%7C-Migration-Guide-(v1.1-%E2%86%92-v1.2+)).

---

## Changelog & Migration Guide

- See [U | Changelog](https://github.com/Noctify0/FXItems/wiki/U-%7C-Changelog) for all recent updates.
- Major new features in v1.2+:
  - **Auto-Registration System:** No more manual registration required!
  - **New API (`RegistryAPI`, `UtilsAPI`):** For all registration and utility access.
  - **Improved Addon Support:** Plugins/addons can now safely register content and access utilities.
  - **Utility Overhaul:** All utilities now accessed via the API, not via direct static calls.
  - **Internal Refactoring:** More robust, documented, and stable than ever.
- See [T | Migration Guide (v1.1 → v1.2+)](https://github.com/Noctify0/FXItems/wiki/T-%7C-Migration-Guide-(v1.1-%E2%86%92-v1.2+)) for upgrade instructions and breaking changes.

---

## Contributing

1. Fork the repository.
2. Add new items, commands, foods, or utilities in the correct package.
3. Register them in the corresponding registry initialization method or just use auto-registration.
4. Submit a pull request!

---

## License

MIT License

---

## Credits

Developed by **Noctify0**  
Special thanks to the Bukkit/Spigot community and all open-source contributors!

---

Happy coding—and have fun customizing your Minecraft server!
