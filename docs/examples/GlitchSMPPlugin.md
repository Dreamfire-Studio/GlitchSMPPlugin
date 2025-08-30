# GlitchSMPPlugin

**GlitchSMPPlugin** is a Dreamfire Studio Minecraft plugin that introduces powerful **"Glitch Tokens"** — consumable items that grant temporary abilities such as teleportation, freezing enemies, duplicating totems, invisibility, and more.

It is built on **DreamCore**, **DreamCommand**, and **DreamSmartInvs**, following the standard Dreamfire project template.

---

## ✨ Features

- **Glitch Tokens**
    - Each token represents a unique ability (`GlitchKey`).
    - Tokens are unstackable (unless specified) and glow for visibility.
    - Custom crafting recipes (shaped) produce tokens.

- **Abilities**
    - Each `GlitchKey` maps to an ability implementation in `GlitchAbilities`.
    - Examples:
        - `CRASH` → Kick a target with “CONNECTION THROTTLED”.
        - `FREEZE` → Root a player in place for 30s, blocking items & pearls.
        - `GLIDE` → Launch forward and glide infinitely until landing.
        - `DREAM` → Loot multiplier and XP boost on mob kills.
        - `IMMUNITY` → Temporary damage immunity.
        - `TELEPORT` → Blink to the block you’re looking at.
        - …and more.

- **Guards (State Managers)**
    - Each timed effect uses a guard (e.g. `FreezeGuard`, `ImmunityGuard`).
    - Guards are reusable and handle enabling, expiry, and checking.

- **Diffuser System**
    - The `DIFFUSER` token disables all other glitches for 30s.
    - Broadcasts to all players when activated.

- **SmartInvs Menu**
    - `/glitchtoken` opens a **Glitch Token Menu** (1×9).
    - Slots 3–5 allow players to save/load tokens into their **loadout**.
    - Backed by `GlitchLoadoutPdcStore` (Player PDC persistence).

- **Permissions**
    - Defined in `GlitchSMPPluginPermissions` enum.
    - Examples:
        - `GlitchSMPPlugin.Admin.ReloadConfigs`
        - `GlitchSMPPlugin.Player.GetGlitchItem`
        - `GlitchSMPPlugin.Player.GiveGlitchItem`
        - `GlitchSMPPlugin.Player.GlitchTokens`

---

## 📦 Installation

1. Place the plugin JAR into your server’s `/plugins` folder.
2. Ensure you also have:
    - **DreamCore**
    - **DreamCommand**
    - **ProtocolLib** (for invisibility equipment masking)
3. Restart the server.

---

## 🕹️ Commands

### `/glitchtoken` (`/gt`)
Main command for players.

- `/glitchtoken`  
  Opens the Glitch Token Menu (if system enabled).

- `/glitchtoken get <GlitchKey>`  
  Give yourself a token. Requires `GlitchSMPPlugin.GetGlitchItem`.

- `/glitchtoken give <GlitchKey> <player>`  
  Give 1 token to another player. Requires `GlitchSMPPlugin.GiveGlitchItem`.

- `/glitchtoken give <GlitchKey> <player> <amount>`  
  Give a stack (1 / 8 / 16 / 32 / 64). Requires `GlitchSMPPlugin.GiveGlitchItem`.

Tab completion is provided for:
- All `GlitchKey` values.
- Online player names.
- Common stack amounts.

---

## 🎮 Using Tokens

- **Activation**  
  Right-click with a token in hand to consume it.  
  The plugin checks:
    1. If the token is valid (via `GlitchItemKeys`).
    2. If Diffuser is active (may block).
    3. Fires a cancellable `GlitchUseEvent` (so other plugins can veto).
    4. Dispatches to the ability (`GlitchAbilityExecutor.handle`).

- **Duration & Cooldowns**  
  Most effects last ~30s. Guards handle the timer.  
  Some (like Glide) end when a condition is met (e.g. touching ground).

- **Visual & Audio FX**  
  Each ability plays particles and sounds to provide player feedback.

---

## 🛠️ Developer Notes

- **Events**
    - `GlitchUseEvent` (cancellable) is fired before an ability executes.
    - Other plugins can listen and cancel use (e.g. region protection).

- **Extending**
    - Add new abilities by:
        1. Creating a `GlitchKey` enum entry.
        2. Implementing a token (`AbstractGlitchToken` subclass).
        3. Adding a shaped recipe (`AbstractGlitchShapedRecipe` subclass).
        4. Writing an ability handler in `GlitchAbilities`.
        5. Extending guards/listeners if needed.

- **Guards**
    - Implemented as reusable utility classes (stored in `Util`).
    - Pattern: `enable(id, duration)`, `isActive(id)`, `remaining(id)`.

- **SmartInvs Menu**
    - Persists player-selected tokens into their PDC.
    - Provides drag/drop, swap, and right-click to clear.
    - Only tokens can be placed in slots (rejects other items).

---

## 🔑 Permission Overview

| Permission Key | Purpose |
|----------------|---------|
| `ReloadConfigs` | Reload plugin configs |
| `ResetConfigs`  | Reset plugin configs |
| `EnableSystem`  | Toggle system enable/disable |
| `SerializeItem` | Serialize items |
| `AdminConsole`  | Admin-only console actions |
| `GetGlitchItem` | Get token for yourself |
| `GiveGlitchItem`| Give tokens to others |
| `GlitchTokens`  | Access the token menu |

---

## 📚 Example: Custom Event Listener

```java
@EventHandler
public void onUse(GlitchUseEvent e) {
    if (e.getGlitchKey() == GlitchKey.TELEPORT 
        && !canTeleportHere(e.getPlayer())) {
        e.setCancelled(true); // veto teleport
    }
}