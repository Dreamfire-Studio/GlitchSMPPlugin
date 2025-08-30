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
package com.dreamfirestudios.glitchsmpplugin.DreamItems;

import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

/**
 * <summary>
 * Centralized {@link NamespacedKey} factories for token PDC fields and related flags.
 * </summary>
 * <remarks>
 * Always use these helpers to avoid key typos and to keep the namespace scoped to the plugin.
 * </remarks>
 * <example>
 * <code>
 * PersistentDataContainer pdc = meta.getPersistentDataContainer();
 * pdc.set(GlitchItemKeys.glitchKey(plugin), PersistentDataType.STRING, GlitchKey.DUPE.name());
 * </code>
 * </example>
 */
public final class GlitchItemKeys {
    private GlitchItemKeys() {}

    /**
     * <summary>
     * Namespaced key for the Dream item registry identifier.
     * </summary>
     * <param name="plugin">Owning plugin that provides the namespace.</param>
     * <returns>A {@link NamespacedKey} with key <c>dream_item_id</c>.</returns>
     */
    public static NamespacedKey dreamId(Plugin plugin) {
        return new NamespacedKey(plugin, "dream_item_id");
    }

    /**
     * <summary>
     * Namespaced key that stores the {@link GlitchKey} name for a token.
     * </summary>
     * <param name="plugin">Owning plugin that provides the namespace.</param>
     * <returns>A {@link NamespacedKey} with key <c>glitch_key</c>.</returns>
     */
    public static NamespacedKey glitchKey(Plugin plugin) {
        return new NamespacedKey(plugin, "glitch_key");
    }

    /**
     * <summary>
     * Namespaced key used to make items unstackable via a random UUID value.
     * </summary>
     * <param name="plugin">Owning plugin that provides the namespace.</param>
     * <returns>A {@link NamespacedKey} with key <c>uid</c>.</returns>
     */
    public static NamespacedKey uid(Plugin plugin) {
        return new NamespacedKey(plugin, "uid");
    }

    /**
     * <summary>
     * Per-glitch "crafted once" boolean/flag key (value type is implementation-defined).
     * </summary>
     * <param name="plugin">Owning plugin that provides the namespace.</param>
     * <param name="key">Glitch type used to suffix the key.</param>
     * <returns>
     * A {@link NamespacedKey} like <c>crafted_once_dupe</c> (lowercased glitch key).
     * </returns>
     */
    public static NamespacedKey craftedOnce(Plugin plugin, GlitchKey key) {
        return new NamespacedKey(plugin, "crafted_once_" + key.name().toLowerCase());
    }
}