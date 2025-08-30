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
package com.dreamfirestudios.glitchsmpplugin.PulseConfig;

import com.dreamfirestudios.dreamconfig.Abstract.StaticEnumPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamcore.DreamChat.DreamChat;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageFormatter;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.Core.Scheduler;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginMessages;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginPermissions;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <summary>Message templates with safe main-thread dispatch.</summary>
 * <remarks>
 * Stores raw strings and provides helpers to send via DreamChat with formatting.
 * </remarks>
 * <example>
 * <code>
 * GlitchSMPPluginMessagesConfig.ReturnStaticAsync(plugin,
 *   GlitchSMPPluginMessagesConfig.class, cfg -&gt; {
 *       cfg.SendMessageToConsole(GlitchSMPPluginMessages.PlayerReloadedConfig, DreamMessageSettings.all());
 *   });
 * </code>
 * </example>
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class GlitchSMPPluginMessagesConfig
        extends StaticEnumPulseConfig<GlitchSMPPluginMessagesConfig, GlitchSMPPluginMessages, String> {

    private Scheduler scheduler() { return new Scheduler(GlitchSMPPlugin.GetGlitchSMPPlugin()); }

    /** <summary>Plugin binding.</summary> */
    @Override public JavaPlugin mainClass() { return GlitchSMPPlugin.GetGlitchSMPPlugin(); }

    /** <summary>Key enum type.</summary> */
    @Override protected Class<GlitchSMPPluginMessages> getKeyClass() { return GlitchSMPPluginMessages.class; }

    /** <summary>Value type.</summary> */
    @Override protected Class<String> getValueClass() { return String.class; }

    /** <summary>Defaults to enum templates.</summary> */
    @Override protected String getDefaultValueFor(GlitchSMPPluginMessages key) { return key.GetTemplate(); }

    /** <summary>Store at root (no subfolder).</summary> */
    @Override public boolean useSubFolder() { return false; }

    /**
     * <summary>Prefixes plugin name to arguments for templates expecting it.</summary>
     * <param name="args">Arguments to append after plugin name.</param>
     * <returns>Augmented argument array.</returns>
     */
    private static Object[] withPlugin(Object... args) {
        String pluginName = GlitchSMPPlugin.class.getSimpleName();
        Object[] full = new Object[(args == null ? 0 : args.length) + 1];
        full[0] = pluginName;
        if (args != null && args.length > 0) System.arraycopy(args, 0, full, 1, args.length);
        return full;
    }

    /**
     * <summary>Formats and flattens a template to plain text.</summary>
     * <param name="template">Template string.</param>
     * <param name="settings">Message settings.</param>
     * <param name="fmtArgs">Format args.</param>
     * <returns>Plain text.</returns>
     */
    private static String formatPlain(String template, DreamMessageSettings settings, Object... fmtArgs) {
        var comp = DreamMessageFormatter.format(String.format(template, fmtArgs), settings);
        return PlainTextComponentSerializer.plainText().serialize(comp);
    }

    /**
     * <summary>Formats with player-aware placeholders and flattens to text.</summary>
     * <param name="template">Template string.</param>
     * <param name="p">Player context.</param>
     * <param name="settings">Message settings.</param>
     * <param name="fmtArgs">Format args.</param>
     * <returns>Plain text.</returns>
     */
    private static String formatPlain(String template, Player p, DreamMessageSettings settings, Object... fmtArgs) {
        var comp = DreamMessageFormatter.format(String.format(template, fmtArgs), p, settings);
        return PlainTextComponentSerializer.plainText().serialize(comp);
    }

    /**
     * <summary>Broadcasts a message to all players.</summary>
     * <param name="msg">Message key.</param>
     * <param name="settings">Message settings.</param>
     * <param name="args">Optional format args (plugin name is auto-prepended).</param>
     */
    public void SendMessageToBroadcast(GlitchSMPPluginMessages msg, DreamMessageSettings settings, Object... args){
        final var template = getDefaultValueFor(msg);
        if (template == null || template.isEmpty()) return;
        scheduler().main(() -> DreamChat.BroadcastMessage(
                formatPlain(template, settings, withPlugin(args)), settings));
    }

    /**
     * <summary>Sends a message to all players that match a permission.</summary>
     * <param name="msg">Message key.</param>
     * <param name="perm">Permission enum.</param>
     * <param name="settings">Message settings.</param>
     * <param name="args">Optional format args.</param>
     */
    public void SendMessageToPlayerPermission(GlitchSMPPluginMessages msg, GlitchSMPPluginPermissions perm, DreamMessageSettings settings, Object... args) {
        scheduler().main(() -> {
            for (var player : Bukkit.getOnlinePlayers()) {
                GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(),
                        GlitchSMPPluginPermissionsConfigs.class, cfg -> {
                            if (cfg.DoesPlayerHavePermission(perm, player, false, settings)) {
                                SendMessageToPlayer(msg, player, settings, args);
                            }
                        });
            }
        });
    }

    /**
     * <summary>Sends a message to a specific player.</summary>
     * <param name="msg">Message key.</param>
     * <param name="player">Recipient.</param>
     * <param name="settings">Message settings.</param>
     * <param name="args">Optional format args.</param>
     */
    public void SendMessageToPlayer(GlitchSMPPluginMessages msg, Player player, DreamMessageSettings settings, Object... args){
        if (player == null || msg == null) return;
        final var template = getDefaultValueFor(msg);
        if (template == null || template.isEmpty()) return;
        scheduler().main(() -> DreamChat.SendMessageToPlayer(
                player, formatPlain(template, player, settings, withPlugin(args)), settings));
    }

    /**
     * <summary>Sends a message into a Bukkit conversation context.</summary>
     * <param name="msg">Message key.</param>
     * <param name="player">Optional player for context placeholders.</param>
     * <param name="ctx">Conversation context.</param>
     * <param name="settings">Message settings.</param>
     * <param name="args">Optional format args.</param>
     */
    public void SendMessageToContext(GlitchSMPPluginMessages msg, Player player, ConversationContext ctx, DreamMessageSettings settings, Object... args) {
        if (ctx == null || msg == null) return;
        final var template = getDefaultValueFor(msg);
        if (template == null || template.isEmpty()) return;
        scheduler().main(() -> ctx.getForWhom()
                .sendRawMessage(formatPlain(template, player, settings, withPlugin(args))));
    }

    /**
     * <summary>Sends a message to the console.</summary>
     * <param name="msg">Message key.</param>
     * <param name="settings">Message settings.</param>
     * <param name="args">Optional format args.</param>
     */
    public void SendMessageToConsole(GlitchSMPPluginMessages msg, DreamMessageSettings settings, Object... args){
        if (msg == null) return;
        final var template = getDefaultValueFor(msg);
        if (template == null || template.isEmpty()) return;
        scheduler().main(() -> DreamChat.SendMessageToConsole(
                formatPlain(template, settings, withPlugin(args)), settings));
    }
}