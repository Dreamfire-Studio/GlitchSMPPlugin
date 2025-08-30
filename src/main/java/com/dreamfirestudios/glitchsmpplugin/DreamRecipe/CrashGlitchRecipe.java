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

import com.dreamfirestudios.dreamcore.DreamItems.IDreamItemStack;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.DreamItems.CrashGlitchToken;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

import java.util.List;
import java.util.Map;

/// <summary>Crash Glitch token shaped recipe.</summary>
/// <remarks>Blackstone + redstone + deepslate motif.</remarks>
@PulseAutoRegister
public final class CrashGlitchRecipe extends AbstractGlitchShapedRecipe {

    /// <summary>Glitch key produced by this recipe.</summary>
    /// <returns><see cref="GlitchKey.CRASH"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected GlitchKey key() { return GlitchKey.CRASH; }

    /// <summary>Recipe registration name.</summary>
    /// <returns><c>glitch_crash</c>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected String name() { return "glitch_crash"; }

    /// <summary>Shaped pattern.</summary>
    /// <returns><c>BRB / RDR / BRB</c>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected List<String> shape() { return List.of("BRB", "RDR", "BRB"); }

    /// <summary>Ingredient choices for pattern symbols.</summary>
    /// <returns>Mapping for B/R/D.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected Map<Character, RecipeChoice> ingredients() {
        return Map.of(
                'B', new RecipeChoice.MaterialChoice(Material.BLACKSTONE),
                'R', new RecipeChoice.MaterialChoice(Material.REDSTONE),
                'D', new RecipeChoice.MaterialChoice(Material.DEEPSLATE)
        );
    }

    /// <summary>Token produced by the recipe.</summary>
    /// <returns>New <see cref="CrashGlitchToken"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected IDreamItemStack token() { return new CrashGlitchToken(); }
}