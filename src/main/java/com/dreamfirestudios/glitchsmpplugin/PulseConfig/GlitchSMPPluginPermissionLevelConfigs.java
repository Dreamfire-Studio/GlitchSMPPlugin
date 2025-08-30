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
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginPermissionLevel;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <summary>Maps permission levels to string fragments.</summary>
 * <example>
 * <code>
 * GlitchSMPPluginPermissionLevelConfigs.ReturnStaticAsync(plugin,
 *   GlitchSMPPluginPermissionLevelConfigs.class, cfg -&gt; {
 *       String lvl = cfg.GetValue(GlitchSMPPluginPermissionLevel.Admin);
 *   });
 * </code>
 * </example>
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class GlitchSMPPluginPermissionLevelConfigs
        extends StaticEnumPulseConfig<GlitchSMPPluginPermissionLevelConfigs, GlitchSMPPluginPermissionLevel, String> {

    /** <summary>Plugin binding.</summary> */
    @Override public JavaPlugin mainClass() { return GlitchSMPPlugin.GetGlitchSMPPlugin(); }

    /** <summary>Key enum type.</summary> */
    @Override protected Class<GlitchSMPPluginPermissionLevel> getKeyClass() { return GlitchSMPPluginPermissionLevel.class; }

    /** <summary>Value type.</summary> */
    @Override protected Class<String> getValueClass() { return String.class; }

    /** <summary>Store at root (no subfolder).</summary> */
    @Override public boolean useSubFolder() { return false; }

    /** <summary>Default mapping to {@link GlitchSMPPluginPermissionLevel#GetPermissionLevel()}.</summary> */
    @Override
    protected String getDefaultValueFor(GlitchSMPPluginPermissionLevel key) {
        return key.GetPermissionLevel();
    }
}