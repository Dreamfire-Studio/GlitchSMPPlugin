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

import com.dreamfirestudios.dreamconfig.Abstract.StaticPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamconfig.Interface.StorageComment;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.Event.GlitchSMPPluginSystemToggleEvent;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

/**
 * <summary>Main config class for the plugin.</summary>
 * <remarks>
 * <ul>
 *   <li>Handles system enable/disable state and debug configuration.</li>
 *   <li>Provides toggle method which also fires {@link GlitchSMPPluginSystemToggleEvent}.</li>
 *   <li>Stored as a static PulseConfig bound to the plugin.</li>
 * </ul>
 * </remarks>
 */
@PulseAutoRegister
@ConfigVersion(1)
public class GlitchSMPPluginConfig extends StaticPulseConfig<GlitchSMPPluginConfig> {

    /** <summary>Return the main plugin class.</summary> */
    @Override
    public JavaPlugin mainClass() { return GlitchSMPPlugin.GetGlitchSMPPlugin(); }

    /** <summary>System enable toggle.</summary> */
    @StorageComment("WARNING: SYSTEM WONT RUN IF FALSE!")
    public boolean systemEnabled = true;

    /** <summary>Enable debug logging for config changes.</summary> */
    @StorageComment("Display debugs in the console logs for changes in this config!")
    public boolean debugConfig = false;

    /** <summary>Do not use a subfolder for storage.</summary> */
    @Override
    public boolean useSubFolder() { return false; }

    /**
     * <summary>Toggle system enabled state and fire toggle event.</summary>
     * <param name="onSuccess">Callback after saving config</param>
     * <param name="newState">New enabled state</param>
     */
    public void ToggleSystemEnabled(Consumer<GlitchSMPPluginConfig> onSuccess, boolean newState) {
        var event = new GlitchSMPPluginSystemToggleEvent(systemEnabled, newState);
        event.CallEvent();
        systemEnabled = newState;
        SaveDreamConfig(GlitchSMPPlugin.GetGlitchSMPPlugin(), onSuccess);
    }
}