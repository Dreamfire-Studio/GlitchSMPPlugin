# GlitchSMPPlugin Manual

**GlitchSMPPlugin** is a Dreamfire Studio plugin for Minecraft SMP servers.
It introduces **Glitch Tokens** — custom craftable items that grant **temporary supernatural abilities** when consumed.

Built on **DreamCore**, **DreamCommand**, and **DreamSmartInvs**, this plugin is fully integrated into the Dreamfire ecosystem and follows the standard reusable component design.

---

## ✨ Core Concepts

* **Glitch Tokens**

    * Custom items with glowing effect & lore.
    * Crafted via shaped recipes (see full list below).
    * When consumed, fire a `GlitchUseEvent` (cancellable).
    * Dispatch through `GlitchAbilityExecutor` to per-token logic.

* **Glitch Abilities**

    * Implemented under `GlitchAbilities`.
    * Examples: teleportation, inventory scrambling, duplicating totems.
    * Most abilities last \~30s and show particle/sound FX.

* **Guards**

    * State managers for abilities (`FreezeGuard`, `ImmunityGuard`, `GlideGuard`, etc.).
    * Track effect timers using `ConcurrentHashMap` or `AtomicReference`.

* **Diffuser**

    * Special token that **disables all other glitches** for 30s.
    * Broadcasts globally when used.

* **SmartInvs Menu**

    * `/glitchtoken` opens a **1×9 Glitch Token Menu**.
    * Slots 3–5 are active loadout slots.
    * Persisted per-player in **PDC** (via `GlitchLoadoutPdcStore`).

---

## 📦 Installation

1. Copy the JAR into `/plugins`.
2. Ensure you have:

    * [DreamCore](https://github.com/DreamfireStudios/DreamCore)
    * DreamCommand
    * ProtocolLib (for invisibility equipment masking)
3. Restart your server.

---

## 🕹️ Commands

### `/glitchtoken` (`/gt`)

* `/gt` → Open the Glitch Token Menu.
* `/gt get <GlitchKey>` → Get yourself a token.
* `/gt give <GlitchKey> <player>` → Give token to player.
* `/gt give <GlitchKey> <player> <amount>` → Give multiple tokens.

Tab completions:

* `GlitchKey` names
* Online players
* Amounts: 1 / 8 / 16 / 32 / 64

---

## 🛡️ Permission Keys

| Key                                     | Purpose         |
| --------------------------------------- | --------------- |
| `GlitchSMPPlugin.Admin.ReloadConfigs`   | Reload configs  |
| `GlitchSMPPlugin.Admin.ResetConfigs`    | Reset configs   |
| `GlitchSMPPlugin.Admin.EnableSystem`    | Toggle plugin   |
| `GlitchSMPPlugin.Admin.SerializeItem`   | Debug serialize |
| `GlitchSMPPlugin.Player.GetGlitchItem`  | Get tokens      |
| `GlitchSMPPlugin.Player.GiveGlitchItem` | Give tokens     |
| `GlitchSMPPlugin.Player.GlitchTokens`   | Open token menu |

---

## ⚗️ All Glitch Tokens

Below is a **complete list of every token**, its **crafting recipe**, and **ability effect**.

---

### 🔴 Crash Glitch

* **Key:** `CRASH`
* **Recipe:**

  ```
  B R B
  R D R
  B R B
  ```

    * `B` = Blackstone
    * `R` = Redstone
    * `D` = Deepslate
* **Ability:** Kick the nearest player in sight with *“CONNECTION THROTTLED”*. Target cannot rejoin for 15s.
* **Guard:** `CrashGuard`

---

### 🔺 Redstone Glitch

* **Key:** `REDSTONE`
* **Recipe:**

  ```
  R T R
  T R T
  R T R
  ```

    * `R` = Redstone
    * `T` = Redstone Torch
* **Ability:** Redstone-based disruption (future expansion).
* **Guard:** Diffuser

---

### 🌙 Dream Glitch

* **Key:** `DREAM`
* **Recipe:**

  ```
  E E E
  E H E
  E E E
  ```

    * `E` = Emerald
    * `H` = Player Head
* **Ability:** Mob drops & XP boosted; random bonus rerolls.
* **Guard:** `DreamGuard`

---

### 🌀 Dupe Glitch

* **Key:** `DUPE`
* **Recipe:**

  ```
  P T P
  T P T
  P T P
  ```

    * `P` = Potion
    * `T` = Totem of Undying
* **Ability:** Duplicates held item (Totem or Potion) → max stack 2.
* **Guard:** Diffuser

---

### 📦 Inventory Glitch

* **Key:** `INVENTORY`
* **Recipe:**

  ```
  C B C
  B D B
  C B C
  ```

    * `C` = Chest
    * `B` = Barrel
    * `D` = Dropper
* **Ability:** Scrambles target’s inventory (hotbar + main).
* **Guard:** Diffuser

---

### ⛔ Item Disable Glitch

* **Key:** `ITEM_DISABLE`
* **Recipe:**

  ```
  I S I
  S N S
  I S I
  ```

    * `I` = Iron Ingot
    * `S` = Iron Sword
    * `N` = Netherite Scrap
* **Ability:** Target cannot use swords for 30s.
* **Guard:** `ItemDisableGuard`

---

### 👻 Herobrine Glitch

* **Key:** `HEROBRINE`
* **Recipe:**

  ```
  N G N
  G S G
  N G N
  ```

    * `N` = Netherrack
    * `G` = Glowstone Dust
    * `S` = Nether Star
* **Ability:** Grants Speed II, chance to call lightning on damage.
* **Guard:** `HerobrineGuard`

---

### ❄️ Freeze Glitch

* **Key:** `FREEZE`
* **Recipe:**

  ```
  I P I
  P B P
  I P I
  ```

    * `I` = Ice
    * `P` = Packed Ice
    * `B` = Blue Ice
* **Ability:** Freezes target player for 30s (movement & actions blocked).
* **Guard:** `FreezeGuard`

---

### ✨ Effect Amplify Glitch

* **Key:** `EFFECT_AMPLIFY`
* **Recipe:**

  ```
  G G G
  G B G
  G G G
  ```

    * `G` = Glowstone Dust
    * `B` = Brewing Stand
* **Ability:** Upgrades active potion effects from level I → II.
* **Guard:** Diffuser

---

### 🛡️ Immunity Glitch

* **Key:** `IMMUNITY`
* **Recipe:**

  ```
  S P S
  P T P
  S P S
  ```

    * `S` = Shield
    * `P` = Potion
    * `T` = Turtle Scute
* **Ability:** Grants full damage immunity for 30s.
* **Guard:** `ImmunityGuard`

---

### 🌀 Teleport Glitch

* **Key:** `TELEPORT`
* **Recipe:**

  ```
  E D E
  D P D
  E D E
  ```

    * `E` = Ender Pearl
    * `D` = Deepslate
    * `P` = Ender Eye
* **Ability:** Blink teleport to gaze point (≤20 blocks).
* **Guard:** Diffuser

---

### 🪽 Glide Glitch

* **Key:** `GLIDE`
* **Recipe:**

  ```
  M E M
  E S E
  M E M
  ```

    * `M` = Phantom Membrane
    * `E` = Elytra
    * `S` = Slime Block
* **Ability:** Launch forward/upward, apply Slow Falling until landing.
* **Guard:** `GlideGuard`

---

### 🫥 Invisibility Glitch

* **Key:** `INVISIBILITY`
* **Recipe:**

  ```
  F N F
  N B N
  F N F
  ```

    * `F` = Fermented Spider Eye
    * `N` = Nether Wart
    * `B` = Brewing Stand
* **Ability:** Full invisibility (armor hidden via ProtocolLib mask).
* **Guard:** `InvisibilityGuard`

---

### ☢️ Diffuser Glitch

* **Key:** `DIFFUSER`
* **Recipe:**

  ```
  G T G
  T B T
  G T G
  ```

    * `G` = Tinted Glass
    * `T` = Redstone Torch
    * `B` = Beacon
* **Ability:** Blocks all glitch usage server-wide for 30s.
* **Guard:** `DiffuserGuard`

---

### 🪞 Morph Glitch

* **Key:** `MORPH`
* **Recipe:**

  ```
  A S A
  S C S
  A S A
  ```

    * `A` = Amethyst Shard
    * `S` = Spyglass
    * `C` = Armor Stand
* **Ability:** Placeholder for transformation-based ability.
* **Guard:** Diffuser

---

## 📖 Summary

* GlitchSMPPlugin adds **14 craftable glitch tokens**.
* Each grants unique, temporary abilities.
* Effects are tracked using guards.
* Integrated with **DreamSmartInvs** for persistent GUI loadouts.
* Fully event-driven (abilities fire cancellable `GlitchUseEvent`).

This makes GlitchSMPPlugin highly modular, extendable, and easy to integrate with other Dreamfire Studio plugins.
