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
import com.dreamfirestudios.glitchsmpplugin.DreamItems.HerobrineGlitchToken;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

import java.util.List;
import java.util.Map;

/// <summary>Herobrine Glitch token shaped recipe.</summary>
@PulseAutoRegister
public final class HerobrineGlitchRecipe extends AbstractGlitchShapedRecipe {

    /// <summary>Glitch key.</summary>
    /// <returns><see cref="GlitchKey.HEROBRINE"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected GlitchKey key() { return GlitchKey.HEROBRINE; }

    /// <summary>Registration name.</summary>
    /// <returns><c>glitch_herobrine</c>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected String name() { return "glitch_herobrine"; }

    /// <summary>Pattern.</summary>
    /// <returns><c>NGN / GSG / NGN</c>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected List<String> shape() { return List.of("NGN", "GSG", "NGN"); }

    /// <summary>Ingredients.</summary>
    /// <returns>N/G/S mapping.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected Map<Character, RecipeChoice> ingredients() {
        return Map.of(
                'N', new RecipeChoice.MaterialChoice(Material.NETHERRACK),
                'G', new RecipeChoice.MaterialChoice(Material.GLOWSTONE_DUST),
                'S', new RecipeChoice.MaterialChoice(Material.NETHER_STAR)
        );
    }

    /// <summary>Result token.</summary>
    /// <returns><see cref="HerobrineGlitchToken"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override protected IDreamItemStack token() { return new HerobrineGlitchToken(); }
}