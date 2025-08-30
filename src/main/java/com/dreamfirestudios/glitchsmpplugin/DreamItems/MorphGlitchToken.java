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

import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

/**
 * <summary>
 * Morph Glitch token definition.
 * </summary>
 * <remarks>
 * Temporarily copies the target player's skin and armor trims for disguise.
 * </remarks>
 */
@PulseAutoRegister
public final class MorphGlitchToken extends AbstractGlitchToken {
    /** <inheritdoc /> */ @Override protected GlitchKey key() { return GlitchKey.MORPH; }
    /** <inheritdoc /> */ @Override protected Material material() { return Material.ARMOR_STAND; }
    /** <inheritdoc /> */ @Override protected Component name() { return Component.text("Morph Glitch"); }
    /** <inheritdoc /> */ @Override protected List<Component> itemLore() {
        return List.of(Component.text("— Copy target’s skin + armor trims."));
    }
}