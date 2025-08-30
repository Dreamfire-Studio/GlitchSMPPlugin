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
import com.dreamfirestudios.dreamcommand.Annotations.PCMethodData;
import com.dreamfirestudios.dreamcommand.Annotations.PCTab;
import com.dreamfirestudios.dreamcommand.Enums.TabType;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.API.GlitchTokenAPI;
import com.dreamfirestudios.glitchsmpplugin.Core.Scheduler;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginPermissions;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginConfig;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginPermissionsConfigs;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <summary>
 * Player command entrypoint for managing Glitch Tokens.
 * </summary>
 * <remarks>
 * <list type="bullet">
 *   <item>Root command: <c>/glitchtoken</c> (aliases: <c>/gt</c>).</item>
 *   <item>Subcommands include: open menu, get token, give token (with optional amount).</item>
 *   <item>All permission checks &amp; system enablement are resolved via Pulse configs.</item>
 * </list>
 * </remarks>
 * <example>
 * <code>
 * /gt                // opens the token menu
 * /gt get DREAM      // gives yourself a single DREAM token (if permitted)
 * /gt give DREAM Notch 8  // gives 8 DREAM tokens to Notch (if permitted)
 * </code>
 * </example>
 */
@PulseAutoRegister
public class GlitchTokenPlayerCommand {

    /// <summary>Primary command name.</summary>
    public static final String COMMAND_NAME = "glitchtoken";
    /// <summary>Command aliases.</summary>
    public static final String[] COMMAND_ALIASES = {"gt"};
    /// <summary>Enable extra debug output (unused here, wired for future).</summary>
    public static final boolean COMMAND_DEBUG = false;

    /// <summary>Creates a scheduler tied to the plugin instance.</summary>
    private Scheduler scheduler() { return new Scheduler(GlitchSMPPlugin.GetGlitchSMPPlugin()); }

    /// <summary>Default constructor.</summary>
    public GlitchTokenPlayerCommand() { }

    /**
     * <summary>
     * Dynamic tab-completion source for glitch keys.
     * </summary>
     * <param name="input">Current partial user input.</param>
     * <returns>Matching <see cref="GlitchKey"/> names.</returns>
     */
    @PCMethodData
    public List<String> glitchKeyTabs(String input) {
        var data = new ArrayList<String>();
        for (var glitchKey : GlitchKey.values()) {
            if (!glitchKey.name().toLowerCase().contains(input.toLowerCase())) continue;
            data.add(glitchKey.name());
        }
        return data;
    }

    /**
     * <summary>
     * Opens the Glitch Token SmartInvs menu for the invoking player.
     * </summary>
     * <param name="player">Invoker.</param>
     * <remarks>
     * No explicit permission gate here; gate is inside <c>OpenGlitchTokenMenu</c> via permission configs.
     * </remarks>
     */
    @PCMethod({})
    public void GlitchTokenMenu(Player player){
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            GlitchTokenAPI.OpenGlitchTokenMenu(player);
        });
    }

    /**
     * <summary>
     * Gives the invoking player a single Glitch Token of the specified type.
     * </summary>
     * <param name="commandUser">Invoker (receiver).</param>
     * <param name="glitchKey">Token type to grant.</param>
     */
    @PCMethod({"get"})
    @PCTab(pos = 1, type = TabType.InformationFromFunction, data = "glitchKeyTabs")
    public void GlitchTokenGet(Player commandUser, GlitchKey glitchKey){
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginPermissionsConfigs.class, permsConfig -> {
                if (!permsConfig.DoesPlayerHavePermission(GlitchSMPPluginPermissions.GetGlitchItem, commandUser, true, DreamMessageSettings.all())) return;
                GlitchTokenAPI.GiveGlitchToken(glitchKey, commandUser, 1);
            });
        });
    }

    /**
     * <summary>
     * Gives a single Glitch Token of the specified type to a target player.
     * </summary>
     * <param name="commandUser">Invoker (permission subject).</param>
     * <param name="glitchKey">Token type to grant.</param>
     * <param name="player">Recipient.</param>
     */
    @PCMethod({"give"})
    @PCTab(pos = 1, type = TabType.InformationFromFunction, data = "glitchKeyTabs")
    @PCTab(pos = 2, type = TabType.OnlinePlayerNames)
    public void GlitchTokenGive(Player commandUser, GlitchKey glitchKey, Player player){
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginPermissionsConfigs.class, permsConfig -> {
                if (!permsConfig.DoesPlayerHavePermission(GlitchSMPPluginPermissions.GiveGlitchItem, commandUser, true, DreamMessageSettings.all())) return;
                com.dreamfirestudios.glitchsmpplugin.API.GlitchTokenAPI.GiveGlitchToken(glitchKey, player, 1);
            });
        });
    }

    /**
     * <summary>
     * Gives a specified amount of Glitch Tokens to a target player.
     * </summary>
     * <param name="commandUser">Invoker (permission subject).</param>
     * <param name="glitchKey">Token type to grant.</param>
     * <param name="player">Recipient.</param>
     * <param name="amount">Amount to grant (suggested tabs provided).</param>
     */
    @PCMethod({"give"})
    @PCTab(pos = 1, type = TabType.InformationFromFunction, data = "glitchKeyTabs")
    @PCTab(pos = 2, type = TabType.OnlinePlayerNames)
    @PCTab(pos = 3, type = TabType.PureData, data = "1")
    @PCTab(pos = 3, type = TabType.PureData, data = "8")
    @PCTab(pos = 3, type = TabType.PureData, data = "16")
    @PCTab(pos = 3, type = TabType.PureData, data = "32")
    @PCTab(pos = 3, type = TabType.PureData, data = "64")
    public void GlitchTokenGive(Player commandUser, GlitchKey glitchKey, Player player, int amount){
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginPermissionsConfigs.class, permsConfig -> {
                if (!permsConfig.DoesPlayerHavePermission(GlitchSMPPluginPermissions.GiveGlitchItem, commandUser, true, DreamMessageSettings.all())) return;
                com.dreamfirestudios.glitchsmpplugin.API.GlitchTokenAPI.GiveGlitchToken(glitchKey, player, amount);
            });
        });
    }
}