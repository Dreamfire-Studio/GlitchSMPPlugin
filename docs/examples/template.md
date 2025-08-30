# Dreamfire Studios Plugin Template

This document explains the **core plugin template** structure used across all Dreamfire Studios projects. The template is designed for **clarity, reusability, and industry-level practices**.

---

## 📂 Package Structure

### `com.dreamfirestudios.__project_name_lower__`

Main plugin package. Contains the **plugin entry class** and overall orchestration.

* **`__project_name__`** → Main plugin entrypoint (`JavaPlugin`). Handles lifecycle events.
* **API** → Public-facing static APIs for enabling/disabling systems, resetting configs, and exposing helpers.
* **Core** → Shared utilities like logging, schedulers, event bus, and permission helpers.
* **Enums** → All plugin-specific enums for permissions, messages, inventory items, and feature flags.
* **Events** → Custom Bukkit events (ConfigReload, ConfigReset, SystemToggle, etc.).
* **PlayerCommand** → Player-facing command handlers.
* **ServerCommand** → Console/server command handlers.
* **PulseConfig** → All configuration files (StaticPulseConfig, StaticEnumPulseConfig).
* **SmartInvs** → Inventory GUIs built with SmartInvs.

---

## ⚙️ Core Concepts

### ✅ **Pulse Config System**

* Based on **DreamConfig**.
* Uses `StaticPulseConfig` and `StaticEnumPulseConfig`.
* Auto-registered with `@PulseAutoRegister`.
* Provides `ReturnStaticAsync` for safe async access and `SaveDreamConfig` for persistence.

**Examples:**

* `__project_name__Config` → Stores main flags like `systemEnabled` and `debugConfig`.
* `__project_name__FeatureFlagsConfig` → Boolean feature toggles.
* `__project_name__MessagesConfig` → Template-driven messages.
* `__project_name__PermissionsConfigs` → Permission formats and checks.

---

### 🎮 **Command System**

* Uses **DreamCommand** annotations:

    * `@PCMethod` → Command entrypoint.
    * `@PCTab` → Tab completion.
    * `@PCOP` → Operator-only commands.
* Commands are split into **PlayerCommand** (in-game) and **ServerCommand** (console).

**Examples:**

* `/__project_name_lower__ enable true`
* `/__project_name_lower__ configs reload`
* `/__project_name_lower__ serialize ITEM_ID`

---

### 🧩 **Events**

* Custom Bukkit events extend `__project_name__Event`.
* Ensures **thread-safety** (always dispatched on main thread).
* Includes debug logging if enabled.

**Examples:**

* `__project_name__ConfigReloadEvent`
* `__project_name__ConfigResetEvent`
* `__project_name__SystemToggleEvent`

---

### 🛠 **Core Utilities**

* `Scheduler` → Wrapper for main/async task dispatch.
* `Log` → Logger with debug flag support.
* `EventBus` → Fires events on correct thread.
* `Preconditions` → Guard helper.
* `PermissionStrings` → Builds formatted permission nodes.
* `Result` → Algebraic result type (`Ok` / `Err`).

---

### 🎨 **SmartInvs Menus**

* UI menus are built via **SmartInvs** with enum-driven items.
* Items come from `__project_name__InventoryItems` and `__project_name__InventoryItemsConfig`.
* Helpers (`__project_name__SmartInvsItems`) provide easy clickable item creation.

**Example Menu:**

* System Enable Toggle
* Reload Configs
* Reset Configs

---

## 🔑 Enums

Enums are used extensively for **clarity, safety, and reusability**:

* `__project_name__FeatureFlagKey` → Core feature toggles.
* `__project_name__Messages` → All console/player messages.
* `__project_name__InventoryItems` → Prebuilt GUI items.
* `__project_name__PermissionLevel` → Logical permission tiers (Admin, Player).
* `__project_name__Permissions` → Permission format strings + errors.

---

## 🏗 Best Practices

* **Thread Safety** → Always use `Scheduler.main()` when interacting with Bukkit APIs.
* **Event-driven** → Toggle states fire `SystemToggleEvent`, config reloads fire `ConfigReloadEvent`, etc.
* **Enums + Configs** → Centralized, safe, and auto-documented.
* **Separation of Concerns** → API vs Core vs Config vs Command vs SmartInvs.
* **Documentation** → All classes include `<summary>`, `<remarks>`, and `<example>` for DocFX.

---

## 🚀 Quick Start

1. Create a new plugin package: `com.dreamfirestudios.myplugin`.
2. Copy the **template structure**.
3. Replace `__project_name__` placeholders with your plugin name.
4. Register enums in `onEnable()`.
5. Add your custom features as Enums, Configs, and Commands.

---

## 📌 Example Lifecycle

1. Player runs `/myplugin enable true`.
2. `PlayerCommand` calls `__project_name__API.EnableSystem()`.
3. Config updated → Saved → `SystemToggleEvent` fired.
4. EventBus dispatches → Listeners update state.
5. Messages sent to player/console via `MessagesConfig`.

---

## ✅ Summary

This template provides:

* Robust **config system**
* Clean **command handling**
* Safe **event framework**
* Reusable **menus/UI**
* Centralized **permissions/messages/features**

It is a **scalable foundation** for all Dreamfire Studios plugins.