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
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginFeatureFlagKey;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <summary>Feature flags configuration.</summary>
 * <remarks>
 * Provides boolean toggles mapped to {@link GlitchSMPPluginFeatureFlagKey}.
 * </remarks>
 * <example>
 * <code>
 * GlitchSMPPluginFeatureFlagsConfig.ReturnStaticAsync(plugin,
 *     GlitchSMPPluginFeatureFlagsConfig.class, flags -&gt; {
 *         boolean enabled = flags.GetValue(GlitchSMPPluginFeatureFlagKey.CORE_MENU);
 *     });
 * </code>
 * </example>
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class GlitchSMPPluginFeatureFlagsConfig extends StaticEnumPulseConfig<GlitchSMPPluginFeatureFlagsConfig, GlitchSMPPluginFeatureFlagKey, Boolean> {

    /** <summary>Plugin binding.</summary> */
    @Override public JavaPlugin mainClass() { return GlitchSMPPlugin.GetGlitchSMPPlugin(); }

    /** <summary>Key enum type.</summary> */
    @Override protected Class<GlitchSMPPluginFeatureFlagKey> getKeyClass() { return GlitchSMPPluginFeatureFlagKey.class; }

    /** <summary>Value type.</summary> */
    @Override protected Class<Boolean> getValueClass() { return Boolean.class; }

    /** <summary>Store at root (no subfolder).</summary> */
    @Override public boolean useSubFolder() { return false; }

    /**
     * <summary>Default values for flags.</summary>
     * <param name="key">The feature key.</param>
     * <returns>Default boolean.</returns>
     */
    @Override
    protected Boolean getDefaultValueFor(GlitchSMPPluginFeatureFlagKey key) {
        return switch (key) {
            case CORE_MENU      -> Boolean.TRUE;
            case SERIALIZE_ITEMS-> Boolean.TRUE;
            case CONFIG_COMMANDS-> Boolean.TRUE;
        };
    }
}