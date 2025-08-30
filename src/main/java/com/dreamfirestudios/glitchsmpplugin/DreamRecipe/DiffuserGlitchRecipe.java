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
import com.dreamfirestudios.glitchsmpplugin.DreamItems.DiffuserGlitchToken;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

import java.util.List;
import java.util.Map;

/// <summary>Diffuser Glitch token shaped recipe.</summary>
/// <remarks>Tinted glass lattice with a beacon core.</remarks>
@PulseAutoRegister
public final class DiffuserGlitchRecipe extends AbstractGlitchShapedRecipe {

    /// <summary>Glitch key.</summary>
    /// <returns><see cref="GlitchKey.DIFFUSER"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected GlitchKey key() { return GlitchKey.DIFFUSER; }

    /// <summary>Registration name.</summary>
    /// <returns><c>glitch_diffuser</c>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected String name() { return "glitch_diffuser"; }

    /// <summary>Pattern.</summary>
    /// <returns><c>GTG / TBT / GTG</c>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected List<String> shape() { return List.of("GTG", "TBT", "GTG"); }

    /// <summary>Ingredients.</summary>
    /// <returns>G/T/B mapping.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected Map<Character, RecipeChoice> ingredients() {
        return Map.of(
                'G', new RecipeChoice.MaterialChoice(Material.TINTED_GLASS),
                'T', new RecipeChoice.MaterialChoice(Material.REDSTONE_TORCH),
                'B', new RecipeChoice.MaterialChoice(Material.BEACON)
        );
    }

    /// <summary>Result token.</summary>
    /// <returns><see cref="DiffuserGlitchToken"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected IDreamItemStack token() { return new DiffuserGlitchToken(); }
}