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
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcore.DreamLuckPerms.DreamLuckPerms;
import com.dreamfirestudios.glitchsmpplugin.Core.PermissionStrings;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginPermissionLevel;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginPermissions;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <summary>Permission formats + checks.</summary>
 * <remarks>
 * Uses a format string (e.g. <c>"%s.%s.ReloadConfigs"</c>) and resolves with plugin + level.
 * </remarks>
 * <example>
 * <code>
 * GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(plugin,
 *   GlitchSMPPluginPermissionsConfigs.class, cfg -&gt; {
 *       boolean ok = cfg.DoesPlayerHavePermission(GlitchSMPPluginPermissions.ReloadConfigs, player, true, DreamMessageSettings.all());
 *   });
 * </code>
 * </example>
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class GlitchSMPPluginPermissionsConfigs
        extends StaticEnumPulseConfig<GlitchSMPPluginPermissionsConfigs, GlitchSMPPluginPermissions, String> {

    /** <summary>Plugin binding.</summary> */
    @Override public JavaPlugin mainClass() { return GlitchSMPPlugin.GetGlitchSMPPlugin(); }

    /** <summary>Key enum type.</summary> */
    @Override protected Class<GlitchSMPPluginPermissions> getKeyClass() { return GlitchSMPPluginPermissions.class; }

    /** <summary>Value type.</summary> */
    @Override protected Class<String> getValueClass() { return String.class; }

    /** <summary>Defaults to the enum-provided format.</summary> */
    @Override
    protected String getDefaultValueFor(GlitchSMPPluginPermissions key) {
        return key.getPermissionFormat();
    }

    /** <summary>Store at root (no subfolder).</summary> */
    @Override public boolean useSubFolder() { return false; }

    /**
     * <summary>Checks whether a player has the resolved permission at any known level.</summary>
     * <param name="perm">Permission key (format provider).</param>
     * <param name="player">Player to check.</param>
     * <param name="sendError">Whether to send the error message if missing.</param>
     * <param name="settings">Chat settings for error message.</param>
     * <returns><c>true</c> if allowed as Admin or Player; otherwise <c>false</c>.</returns>
     * <remarks>
     * Resolves Admin first, then Player. If not allowed and <c>sendError</c> is true, sends .
     * </remarks>
     */
    public boolean DoesPlayerHavePermission(GlitchSMPPluginPermissions perm, Player player, boolean sendError, DreamMessageSettings settings) {
        if (perm == null || player == null) return false;

        final String format = getDefaultValueFor(perm);
        final String pluginName = GlitchSMPPlugin.class.getSimpleName();

        String adminPerm  = PermissionStrings.resolve(format, pluginName, GlitchSMPPluginPermissionLevel.Admin);
        String playerPerm = PermissionStrings.resolve(format, pluginName, GlitchSMPPluginPermissionLevel.Player);

        var user = DreamLuckPerms.getUser(player);
        boolean allowed = DreamLuckPerms.hasPermission(user, adminPerm) || DreamLuckPerms.hasPermission(user, playerPerm);

        if (!allowed && sendError) {
            DreamChat.SendMessageToPlayer(player, perm.GetError(), settings);
        }
        return allowed;
    }
}
