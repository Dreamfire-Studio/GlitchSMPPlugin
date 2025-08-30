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
package com.dreamfirestudios.glitchsmpplugin.API;

import com.dreamfirestudios.dreamconfig.DreamConfig;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.glitchsmpplugin.Core.Scheduler;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginPermissions;
import com.dreamfirestudios.glitchsmpplugin.Event.GlitchSMPPluginConfigReloadEvent;
import com.dreamfirestudios.glitchsmpplugin.Event.GlitchSMPPluginConfigResetEvent;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginConfig;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginPermissionsConfigs;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginSerializableItems;
import com.dreamfirestudios.glitchsmpplugin.SmartInvs.GlitchSMPPluginCoreMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * /// <summary>
 * Public, static API surface for <em>GlitchSMPPlugin</em> plugin features.
 * Exposes helpers to enable/disable the system, serialize items into the plugin
 * configuration, and reset or reload configs with appropriate Bukkit main-thread
 * dispatch and plugin events.
 * /// </summary>
 *
 * /// <remarks>
 * <ul>
 *   <li>This class is <strong>final</strong> and has a private constructor: use static members only.</li>
 *   <li>All config mutations are persisted via {@code SaveDreamConfig(...)} and callbacks are
 *   executed on the server main thread using {@link Scheduler#main(Runnable)}.</li>
 *   <li>Behavior is preserved from the original implementation; only documentation and
 *   defensive null checks were added.</li>
 * </ul>
 * /// </remarks>
 *
 * /// <example>
 * <code>
 * GlitchSMPPluginAPI.GlitchSMPPluginEnableSystem(cfg -&gt; {
 *     // system toggled; access the config safely on main thread
 * }, true);
 * </code>
 * /// </example>
 */
public final class GlitchSMPPluginAPI {

    /**
     * /// <summary>Hidden constructor to enforce static usage.</summary>
     */
    private GlitchSMPPluginAPI() { }

    /**
     * /// <summary>
     * Lazily creates a {@link Scheduler} bound to the active <em>GlitchSMPPlugin</em> plugin instance.
     * /// </summary>
     *
     * /// <returns>A scheduler for main-thread dispatch.</returns>
     */
    private static Scheduler scheduler() {
        return new Scheduler(GlitchSMPPlugin.GetGlitchSMPPlugin());
    }

    /**
     * /// <summary>
     * Explicitly enables or disables the system feature flag in {@link GlitchSMPPluginConfig},
     * persists the config, then invokes a success callback on the Bukkit main thread.
     * /// </summary>
     *
     * /// <param name="onSuccess">Callback invoked with the updated config after save, on main thread.</param>
     * /// <param name="state">Desired state for {@code systemEnabled}.</param>
     *
     * /// <remarks>
     * Uses {@code ReturnStaticAsync} for asynchronous config access, then schedules the callback
     * onto the main thread after {@code SaveDreamConfig}.
     * /// </remarks>
     *
     * /// <example>
     * <code>
     * GlitchSMPPluginAPI.GlitchSMPPluginEnableSystem(cfg -&gt; {
     *     // Now enabled/disabled as requested
     * }, true);
     * </code>
     * /// </example>
     */
    public static void GlitchSMPPluginEnableSystem(final Consumer<GlitchSMPPluginConfig> onSuccess, final boolean state) {
        Objects.requireNonNull(onSuccess, "onSuccess");
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            config.systemEnabled = state;
            config.SaveDreamConfig(GlitchSMPPlugin.GetGlitchSMPPlugin(), _ignored ->
                    scheduler().main(() -> onSuccess.accept(config))
            );
        });
    }

    /**
     * /// <summary>
     * Toggles the system feature flag in {@link GlitchSMPPluginConfig},
     * persists the config, then invokes a success callback on the Bukkit main thread.
     * /// </summary>
     *
     * /// <param name="onSuccess">Callback invoked with the updated config after save, on main thread.</param>
     *
     * /// <remarks>
     * Equivalent to: {@code GlitchSMPPluginEnableSystem(onSuccess, !config.systemEnabled)}.
     * /// </remarks>
     *
     * /// <example>
     * <code>
     * GlitchSMPPluginAPI.GlitchSMPPluginEnableSystem(cfg -&gt; {
     *     // Toggled from previous state
     * });
     * </code>
     * /// </example>
     */
    public static void GlitchSMPPluginEnableSystem(final Consumer<GlitchSMPPluginConfig> onSuccess) {
        Objects.requireNonNull(onSuccess, "onSuccess");
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            config.systemEnabled = !config.systemEnabled;
            config.SaveDreamConfig(GlitchSMPPlugin.GetGlitchSMPPlugin(), _ignored ->
                    scheduler().main(() -> onSuccess.accept(config))
            );
        });
    }

    /**
     * /// <summary>
     * Serializes and stores an {@link ItemStack} under a provided identifier within
     * {@link GlitchSMPPluginSerializableItems}, persists the config, then invokes a success
     * callback on the Bukkit main thread.
     * /// </summary>
     *
     * /// <param name="onSuccess">Callback invoked with the updated serializable-items config on main thread.</param>
     * /// <param name="id">Unique identifier key for the serialized item.</param>
     * /// <param name="itemStack">The {@link ItemStack} to store.</param>
     *
     * /// <remarks>
     * This method does not validate uniqueness of {@code id}; calling code should ensure a stable key policy.
     * /// </remarks>
     *
     * /// <example>
     * <code>
     * ItemStack item = ...;
     * GlitchSMPPluginAPI.GlitchSMPPluginSerializeItem(itemsCfg -&gt; {
     *     // Item now persisted under "starter_sword"
     * }, "starter_sword", item);
     * </code>
     * /// </example>
     */
    public static void GlitchSMPPluginSerializeItem(
            final Consumer<GlitchSMPPluginSerializableItems> onSuccess,
            final String id,
            final ItemStack itemStack
    ) {
        Objects.requireNonNull(onSuccess, "onSuccess");
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(itemStack, "itemStack");

        GlitchSMPPluginSerializableItems.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginSerializableItems.class, config -> {
            config.AddItemStack(id, itemStack);
            config.SaveDreamConfig(GlitchSMPPlugin.GetGlitchSMPPlugin(), _ignored ->
                    scheduler().main(() -> onSuccess.accept(config))
            );
        });
    }

    /**
     * /// <summary>
     * Resets the plugin's static configuration state and fires {@link GlitchSMPPluginConfigResetEvent},
     * if and only if the system is enabled.
     * /// </summary>
     *
     * /// <param name="dreamMessageSettings">Message settings used while (re)registering config.</param>
     *
     * /// <remarks>
     * Performs a no-op when {@code systemEnabled == false}. All actions are dispatched on the main thread.
     * /// </remarks>
     *
     * /// <example>
     * <code>
     * DreamMessageSettings msgs = DreamMessageSettings.Default();
     * GlitchSMPPluginAPI.GlitchSMPPluginResetConfigs(msgs);
     * </code>
     * /// </example>
     */
    public static void GlitchSMPPluginResetConfigs(final DreamMessageSettings dreamMessageSettings) {
        Objects.requireNonNull(dreamMessageSettings, "dreamMessageSettings");
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            scheduler().main(() -> {
                DreamConfig.GetDreamConfig().RegisterStatic(GlitchSMPPlugin.GetGlitchSMPPlugin(), true, dreamMessageSettings);
                new GlitchSMPPluginConfigResetEvent().CallEvent();
            });
        });
    }

    /**
     * /// <summary>
     * Reloads the plugin's static configuration (without wiping existing user-facing values),
     * then fires {@link GlitchSMPPluginConfigReloadEvent}, if the system is enabled.
     * /// </summary>
     *
     * /// <param name="dreamMessageSettings">Message settings used while re-registering config.</param>
     *
     * /// <remarks>
     * Performs a no-op when {@code systemEnabled == false}. All actions are dispatched on the main thread.
     * /// </remarks>
     *
     * /// <example>
     * <code>
     * DreamMessageSettings msgs = DreamMessageSettings.Default();
     * GlitchSMPPluginAPI.GlitchSMPPluginReloadConfigs(msgs);
     * </code>
     * /// </example>
     */
    public static void GlitchSMPPluginReloadConfigs(final DreamMessageSettings dreamMessageSettings) {
        Objects.requireNonNull(dreamMessageSettings, "dreamMessageSettings");
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, cfg -> {
            if (!cfg.systemEnabled) return;
            scheduler().main(() -> {
                DreamConfig.GetDreamConfig().RegisterStatic(GlitchSMPPlugin.GetGlitchSMPPlugin(), false, dreamMessageSettings);
                new GlitchSMPPluginConfigReloadEvent().CallEvent();
            });
        });
    }

    public static void OpenAdminMenu(Player player){
        GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginPermissionsConfigs.class, config -> {
            if (!config.DoesPlayerHavePermission(GlitchSMPPluginPermissions.AdminConsole, player, true, DreamMessageSettings.all())) return;
            scheduler().main(() -> new GlitchSMPPluginCoreMenu(player));
        });
    }
}