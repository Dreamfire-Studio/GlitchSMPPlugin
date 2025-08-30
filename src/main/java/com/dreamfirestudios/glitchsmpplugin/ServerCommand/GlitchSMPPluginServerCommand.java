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
package com.dreamfirestudios.glitchsmpplugin.ServerCommand;

import com.dreamfirestudios.dreamcommand.Annotations.PCMethod;
import com.dreamfirestudios.dreamcommand.Annotations.PCOP;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.API.GlitchSMPPluginAPI;
import com.dreamfirestudios.glitchsmpplugin.Core.Scheduler;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginMessages;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginConfig;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginMessagesConfig;
import org.bukkit.command.CommandSender;

/**
 * <summary>Console/server command endpoints.</summary>
 * <remarks>
 * Annotated methods are discovered by the DreamCommand system.
 * </remarks>
 */
@PulseAutoRegister
public final class GlitchSMPPluginServerCommand {

    /** <summary>Primary command label.</summary> */
    public static final String COMMAND_NAME = "glitchsmpplugin_server";
    /** <summary>Command aliases.</summary> */
    public static final String[] COMMAND_ALIASES = {};
    /** <summary>Whether to enable debug logging at the command layer.</summary> */
    public static final boolean COMMAND_DEBUG = false;

    private Scheduler scheduler() { return new Scheduler(GlitchSMPPlugin.GetGlitchSMPPlugin()); }

    /** <summary>No-op constructor.</summary> */
    public GlitchSMPPluginServerCommand() { }

    /**
     * <summary>Reports status to console.</summary>
     * <remarks>
     * Only runs if the system is enabled.
     * </remarks>
     */
    @PCMethod({"status"})
    @PCOP
    public void GlitchSMPPluginMethod(CommandSender sender) {
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            GlitchSMPPluginMessagesConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginMessagesConfig.class, messageConfig ->
                    scheduler().main(() -> messageConfig.SendMessageToConsole(GlitchSMPPluginMessages.SystemIsntEnabled, DreamMessageSettings.all()))
            );
        });
    }

    /**
     * <summary>Enables or disables the system.</summary>
     * <param name="state"><c>true</c> to enable; <c>false</c> to disable.</param>
     */
    @PCMethod({"enable"})
    @PCOP
    public void GlitchSMPPluginEnableMethod(CommandSender sender, boolean state) {
        GlitchSMPPluginAPI.GlitchSMPPluginEnableSystem(x -> {
            GlitchSMPPluginMessagesConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginMessagesConfig.class, messagesConfig ->
                    scheduler().main(() -> messagesConfig.SendMessageToConsole(state ? GlitchSMPPluginMessages.ConsoleEnabledSystem : GlitchSMPPluginMessages.ConsoleDisableSystem, DreamMessageSettings.all()))
            );
        }, state);
    }

    /**
     * <summary>Resets all plugin configs.</summary>
     * <remarks>
     * Only runs if the system is enabled.
     * </remarks>
     */
    @PCMethod({"configs", "reset"})
    @PCOP
    public void GlitchSMPPluginConfigsResetMethod(CommandSender sender) {
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            GlitchSMPPluginAPI.GlitchSMPPluginResetConfigs(DreamMessageSettings.all());
            GlitchSMPPluginMessagesConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginMessagesConfig.class, messageConfig ->
                    scheduler().main(() -> messageConfig.SendMessageToConsole(GlitchSMPPluginMessages.PlayerResetConfig, DreamMessageSettings.all()))
            );
        });
    }

    /**
     * <summary>Reloads all plugin configs.</summary>
     * <remarks>
     * Only runs if the system is enabled.
     * </remarks>
     */
    @PCMethod({"configs", "reload"})
    @PCOP
    public void GlitchSMPPluginConfigsReloadMethod(CommandSender sender) {
        GlitchSMPPluginConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            GlitchSMPPluginAPI.GlitchSMPPluginReloadConfigs(DreamMessageSettings.all());
            GlitchSMPPluginMessagesConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginMessagesConfig.class, messageConfig ->
                    scheduler().main(() -> messageConfig.SendMessageToConsole(GlitchSMPPluginMessages.PlayerReloadedConfig, DreamMessageSettings.all()))
            );
        });
    }
}