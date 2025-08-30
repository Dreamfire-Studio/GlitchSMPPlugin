# GlitchSMPPlugin

**Version:** `1.21.8-r0.1`
**Author:** Dreamfire Studio, Joshua Filer
**License:** MIT License

---

## Overview

The **GlitchSMPPlugin** is a feature-rich Minecraft plugin built for Bukkit/Spigot/Paper servers that introduces unique *Glitch Tokens*. Each token unlocks a powerful ability that can drastically alter gameplay. Players can craft, collect, and use these tokens through an integrated **SmartInvs-powered menu** backed by DreamCore’s `DreamSmartInvs` framework.

### Key Features

* **Glitch Tokens:** Items that trigger special abilities (e.g., teleportation, duplication, inventory scrambling).
* **Crafting Recipes:** Each Glitch Token has its own custom recipe.
* **Persistent Loadouts:** Tokens persist using Player Persistent Data Containers (PDC).
* **Guards:** Built-in state guards prevent stacking or misuse of glitches.
* **Integration:** Uses DreamCore utilities (`DreamSmartInvs`, `DreamItems`, `DreamCommand`, `DreamRaycast`).

---

## Documentation

For the full developer and gameplay documentation, see the [official wiki page](https://www.dreamfirestudio.net/wiki/Dreamfire-Studio/GlitchSMPPlugin/DreamCore-1.21.8-r0.1/md_docs_2examples_2GlitchSMPPlugin.html#autotoc_md15).

You can also watch tutorials and showcases on our [YouTube channel](https://www.youtube.com/@Dreamfire_Studio).

Archived original Bukkit post: [Bukkit.org Thread (Wayback Machine)](https://web.archive.org/web/20250328150513/https://www.bukkit.org/threads/glitch-smp-plugin.505046)

---

## Installation

### Download

Download the latest release here:
👉 [GitHub Releases](https://github.com/Dreamfire-Studio/GlitchSMPPlugin/releases/tag/1.21.8-r0.1)

Place the `GlitchSMPPlugin.jar` into your server’s `plugins/` folder, then restart the server.

### Maven

```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/Dreamfire-Studio/GlitchSMPPlugin</url>
</repository>

<dependency>
  <groupId>com.dreamfirestudios</groupId>
  <artifactId>glitchsmpplugin-1.21.8-r0.1</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

---

## Commands

* `/glitchtoken` or `/gt` → Opens the Glitch Token menu.
* `/glitchtoken get <key>` → Get a Glitch Token.
* `/glitchtoken give <key> <player> [amount]` → Give tokens to a player.

---

## Permissions

| Permission Node                  | Description                       |
| -------------------------------- | --------------------------------- |
| `GlitchSMPPlugin.AdminConsole`   | Allows admin-only console actions |
| `GlitchSMPPlugin.ReloadConfigs`  | Reload configuration files        |
| `GlitchSMPPlugin.ResetConfigs`   | Reset configuration files         |
| `GlitchSMPPlugin.EnableSystem`   | Enable/disable system             |
| `GlitchSMPPlugin.SerializeItem`  | Serialize items                   |
| `GlitchSMPPlugin.GetGlitchItem`  | Retrieve a Glitch Token           |
| `GlitchSMPPlugin.GiveGlitchItem` | Give a Glitch Token               |
| `GlitchSMPPlugin.GlitchTokens`   | General use of Glitch Tokens      |

---

## Glitch Token Menu

The **Glitch Token Menu** is a SmartInvs-based GUI (1×9).

* Active slots: Columns 3, 4, 5.
* Only Glitch Tokens can be placed in these slots.
* Persistent across logins via PDC.

Right-click → clears slot.
Left-click → pick up, place, or swap tokens.

---

## Glitch Tokens & Recipes

Each Glitch Token is crafted with a unique recipe. Tokens are stored under IDs like `glitch_token:<key>`.

### Crash Glitch

* **Recipe:** Blackstone + Redstone + Deepslate
* **Effect:** Kicks target player (“CONNECTION THROTTLED”) for 15s.

### Redstone Glitch

* **Recipe:** Redstone + Redstone Torch
* **Effect:** Manipulates redstone temporarily (future expansion).

### Dream Glitch

* **Recipe:** Emeralds + Player Head
* **Effect:** Boosted loot tables, extra drops, bonus XP.

### Dupe Glitch

* **Recipe:** Potion + Totem of Undying
* **Effect:** Duplicate potions/totems (max stack 2).

### Inventory Glitch

* **Recipe:** Chest + Barrel + Dropper
* **Effect:** Scrambles opponent’s inventory.

### Item Disable Glitch

* **Recipe:** Iron Sword + Iron Ingot + Netherite Scrap
* **Effect:** Disables opponent’s sword use.

### Herobrine Glitch

* **Recipe:** Netherrack + Glowstone Dust + Nether Star
* **Effect:** Speed II + lightning strike chance.

### Freeze Glitch

* **Recipe:** Ice + Packed Ice + Blue Ice
* **Effect:** Freezes player in place for 30s.

### Effect Amplify Glitch

* **Recipe:** Glowstone Dust + Brewing Stand
* **Effect:** Upgrades level I potion effects → level II.

### Immunity Glitch

* **Recipe:** Shield + Potion + Turtle Scute
* **Effect:** Grants temporary damage immunity.

### Teleport Glitch

* **Recipe:** Ender Pearl + Deepslate + Ender Eye
* **Effect:** Blink teleport to gaze point.

### Glide Glitch

* **Recipe:** Phantom Membrane + Elytra + Slime Block
* **Effect:** Launch + glide (Slow Falling).

### Invisibility Glitch

* **Recipe:** Fermented Spider Eye + Nether Wart + Brewing Stand
* **Effect:** Invisibility + equipment masking.

### Diffuser Glitch

* **Recipe:** Tinted Glass + Redstone Torch + Beacon
* **Effect:** Disables all glitches globally for 30s.

### Morph Glitch

* **Recipe:** Amethyst Shard + Spyglass + Armor Stand
* **Effect:** Morph ability (future expansion).

---

## Items List

| Token                 | ID                            | Description                   |
| --------------------- | ----------------------------- | ----------------------------- |
| Crash Glitch          | `glitch_token:crash`          | Kick target player for 15s    |
| Redstone Glitch       | `glitch_token:redstone`       | Experimental redstone effects |
| Dream Glitch          | `glitch_token:dream`          | Boost loot and XP             |
| Dupe Glitch           | `glitch_token:dupe`           | Duplicate Totems/Potions      |
| Inventory Glitch      | `glitch_token:inventory`      | Scramble target inventory     |
| Item Disable Glitch   | `glitch_token:item_disable`   | Disable sword use             |
| Herobrine Glitch      | `glitch_token:herobrine`      | Speed + lightning             |
| Freeze Glitch         | `glitch_token:freeze`         | Freeze opponent for 30s       |
| Effect Amplify Glitch | `glitch_token:effect_amplify` | Amplify potion effects        |
| Immunity Glitch       | `glitch_token:immunity`       | Temporary immunity            |
| Teleport Glitch       | `glitch_token:teleport`       | Blink teleport                |
| Glide Glitch          | `glitch_token:glide`          | Launch + glide                |
| Invisibility Glitch   | `glitch_token:invisibility`   | Invisibility + equipment mask |
| Diffuser Glitch       | `glitch_token:diffuser`       | Disable all glitches for 30s  |
| Morph Glitch          | `glitch_token:morph`          | Morph ability (WIP)           |

---

## Links

* 🌐 [Dreamfire Studio Website](https://www.dreamfirestudio.net)
* 🎥 [YouTube Channel](https://www.youtube.com/@Dreamfire_Studio)
* 📖 [Wiki Documentation](https://www.dreamfirestudio.net/wiki/Dreamfire-Studio/GlitchSMPPlugin/DreamCore-1.21.8-r0.1/md_docs_2examples_2GlitchSMPPlugin.html#autotoc_md15)
* 📦 [GitHub Releases](https://github.com/Dreamfire-Studio/GlitchSMPPlugin/releases/tag/1.21.8-r0.1)

---

## Contributing

Contributions are welcome! Feel free to fork the repo, open issues, or submit pull requests.

---

## License

This project is licensed under the [MIT License](LICENSE).