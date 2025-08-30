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
package com.dreamfirestudios.glitchsmpplugin.DreamRecipe;

import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcore.DreamItems.IDreamItemStack;
import com.dreamfirestudios.glitchsmpplugin.DreamItems.MorphGlitchToken;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

import java.util.List;
import java.util.Map;

/// <summary>Morph Glitch token shaped recipe.</summary>
@PulseAutoRegister
public final class MorphGlitchRecipe extends AbstractGlitchShapedRecipe {

    /// <summary>Glitch key.</summary>
    /// <returns><see cref="GlitchKey.MORPH"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected GlitchKey key() { return GlitchKey.MORPH; }

    /// <summary>Registration name.</summary>
    /// <returns><c>glitch_morph</c>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected String name() { return "glitch_morph"; }

    /// <summary>Pattern.</summary>
    /// <returns><c>ASA / SCS / ASA</c>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected List<String> shape() { return List.of("ASA", "SCS", "ASA"); }

    /// <summary>Ingredients.</summary>
    /// <returns>A/S/C mapping.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected Map<Character, RecipeChoice> ingredients() {
        return Map.of(
                'A', new RecipeChoice.MaterialChoice(Material.AMETHYST_SHARD),
                'S', new RecipeChoice.MaterialChoice(Material.SPYGLASS),
                'C', new RecipeChoice.MaterialChoice(Material.ARMOR_STAND)
        );
    }

    /// <summary>Result token.</summary>
    /// <returns><see cref="MorphGlitchToken"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected IDreamItemStack token() { return new MorphGlitchToken(); }
}