/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.dreamfirestudios.glitchsmpplugin.PlayerCommand;

import com.dreamfirestudios.dreamcommand.Annotations.PCMethod;
import com.dreamfirestudios.dreamcommand.Annotations.PCTab;
import com.dreamfirestudios.dreamcommand.Enums.TabType;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.API.GlitchSMPPluginAPI;
import com.dreamfirestudios.glitchsmpplugin.Core.Scheduler;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginMessages;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginPermissions;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginConfig;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginMessagesConfig;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginPermissionsConfigs;
import com.dreamfirestudios.glitchsmpplugin.SmartInvs.GlitchSMPPluginCoreMenu;
import org.bukkit.entity.Player;

/**
 * <summary>Player commands entrypoint.</summary>
 * <remarks>
 * <ul>
 *   <li>Commands are exposed via DreamCommand annotations on public methods.</li>
 *   <li>Permission checks are delegated to {@code GlitchSMPPluginPermissionsConfigs}.</li>
 *   <li>Any Bukkit interaction is marshalled to the main thread using {@link Scheduler}.</li>
 *   <li>Behavior intentionally matches the original code; only documentation was expanded.</li>
 * </ul>
 * </remarks>
 * <example>
 * <code>
 * /glitchsmpplugin                 // open admin GUI (with permission)
 * /glitchsmpplugin enable true     // enable system
 * /glitchsmpplugin serialize ID    // serialize held item as "ID"
 * /glitchsmpplugin configs reset   // reset configs
 * /glitchsmpplugin configs reload  // reload configs
 * </code>
 * </example>
 */
@PulseAutoRegister
public final class GlitchSMPPluginPlayerCommand {

    /** <summary>Primary command literal.</summary> */
    public static final String COMMAND_NAME = "glitchsmpplugin";
    /** <summary>Alternate command aliases (none by default).</summary> */
    public static final String[] COMMAND_ALIASES = {};
    /** <summary>Debug switch for command layer (reserved; not used).</summary> */
    public static final boolean COMMAND_DEBUG = false;

    /** <summary>Create a scheduler bound to the active plugin.</summary> */
    private Scheduler scheduler() { return new Scheduler(GlitchSMPPlugin.GetGlitchSMPPlugin()); }

    /** <summary>Default constructor.</summary> */
    public GlitchSMPPluginPlayerCommand() { }

    /**
     * <summary>Open admin GUI.</summary>
     * <param name="player">Invoking player</param>
     * <remarks>Requires {@link GlitchSMPPluginPermissions#AdminConsole}.</remarks>
     * <example><code>/glitchsmpplugin</code></example>
     */
    @PCMethod({})
    public void TimeStealCoreMethod(Player player) {
        GlitchSMPPluginAPI.OpenAdminMenu(player);
    }

    /**
     * <summary>Enable/disable the system.</summary>
     * <param name="player">Invoking player</param>
     * <param name="state">Desired state; <c>true</c> enables, <c>false</c> disables</param>
     * <remarks>
     * Persists the config asynchronously and then sends a confirmation message on the main thread.
     * Requires {@link GlitchSMPPluginPermissions#EnableSystem}.
     * </remarks>
     * <example><code>/glitchsmpplugin enable true</code></example>
     */
    @PCMethod({"enable"})
    public void TimeStealCoreEnableMethod(Player player, boolean state) {
        GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginPermissionsConfigs.class, config -> {
            if (!config.DoesPlayerHavePermission(GlitchSMPPluginPermissions.EnableSystem, player, true, DreamMessageSettings.all())) return;
            GlitchSMPPluginAPI.GlitchSMPPluginEnableSystem(cfg -> {
                GlitchSMPPluginMessagesConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginMessagesConfig.class, messageConfig ->
                        scheduler().main(() -> messageConfig.SendMessageToPlayer(state ? GlitchSMPPluginMessages.ConsoleEnabledSystem : GlitchSMPPluginMessages.ConsoleDisableSystem, player, DreamMessageSettings.all()))
                );
            }, state);
        });
    }

    /**
     * <summary>Serialize item in hand.</summary>
     * <param name="player">Invoking player</param>
     * <param name="itemName">Identifier under which to store the held item</param>
     * <remarks>
     * Requires {@link GlitchSMPPluginPermissions#SerializeItem}. The item in the player's main hand is persisted to the serializable items config.
     * </remarks>
     * <example><code>/glitchsmpplugin serialize STARTER_SWORD</code></example>
     */
    @PCMethod({"serialize"})
    @PCTab(pos = 1, type = TabType.PureData, data = "ITEM ID")
    public void TimeStealCoreSerializeItemMethod(Player player, String itemName) {
        GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginPermissionsConfigs.class, config -> {
            if (!config.DoesPlayerHavePermission(GlitchSMPPluginPermissions.SerializeItem, player, true, DreamMessageSettings.all())) return;
            GlitchSMPPluginAPI.GlitchSMPPluginSerializeItem(x -> {
                GlitchSMPPluginMessagesConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginMessagesConfig.class, messageConfig ->
                        scheduler().main(() -> messageConfig.SendMessageToPlayer(GlitchSMPPluginMessages.PlayerSerializedItem, player, DreamMessageSettings.all(), itemName))
                );
            }, itemName, player.getInventory().getItemInMainHand());
        });
    }

    /**
     * <summary>Reset configs.</summary>
     * <param name="player">Invoking player</param>
     * <remarks>
     * No-ops when the system is disabled. Requires {@link GlitchSMPPluginPermissions#ResetConfigs}.
     * Dispatches the reset and then informs the player.
     * </remarks>
     * <example><code>/glitchsmpplugin configs reset</code></example>
     */
    @PCMethod({"configs", "reset"})
    public void TimeStealCoreConfigsResetMethod(Player player) {
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginPermissionsConfigs.class, permsConfig -> {
                if (!permsConfig.DoesPlayerHavePermission(GlitchSMPPluginPermissions.ResetConfigs, player, true, DreamMessageSettings.all())) return;
                GlitchSMPPluginAPI.GlitchSMPPluginResetConfigs(DreamMessageSettings.all());
                GlitchSMPPluginMessagesConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginMessagesConfig.class, messageConfig ->
                        scheduler().main(() -> messageConfig.SendMessageToPlayer(GlitchSMPPluginMessages.PlayerResetConfig, player, DreamMessageSettings.all()))
                );
            });
        });
    }

    /**
     * <summary>Reload configs.</summary>
     * <param name="player">Invoking player</param>
     * <remarks>
     * No-ops when the system is disabled. Requires {@link GlitchSMPPluginPermissions#ReloadConfigs}.
     * Triggers a reload and then informs the player.
     * </remarks>
     * <example><code>/glitchsmpplugin configs reload</code></example>
     */
    @PCMethod({"configs", "reload"})
    public void TimeStealCoreReloadMethod(Player player) {
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginPermissionsConfigs.class, permsConfig -> {
                if (!permsConfig.DoesPlayerHavePermission(GlitchSMPPluginPermissions.ReloadConfigs, player, true, DreamMessageSettings.all())) return;
                GlitchSMPPluginAPI.GlitchSMPPluginReloadConfigs(DreamMessageSettings.all());
                GlitchSMPPluginMessagesConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginMessagesConfig.class, messageConfig ->
                        scheduler().main(() -> messageConfig.SendMessageToPlayer(GlitchSMPPluginMessages.PlayerReloadedConfig, player, DreamMessageSettings.all()))
                );
            });
        });
    }
}