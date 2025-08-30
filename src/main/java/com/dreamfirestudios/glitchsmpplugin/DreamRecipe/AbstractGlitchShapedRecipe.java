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

import com.dreamfirestudios.dreamcore.DreamItems.DreamItemStacks;
import com.dreamfirestudios.dreamcore.DreamItems.IDreamItemStack;
import com.dreamfirestudios.dreamcore.DreamRecipe.IDreamRecipe;
import com.dreamfirestudios.dreamcore.DreamRecipe.RecipeType;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// <summary>
/// Base shaped-recipe for Glitch tokens.
/// </summary>
/// <remarks>
/// Subclasses define: <see cref="key()"/>, <see cref="name()"/>, <see cref="shape()"/>,
/// <see cref="ingredients()"/>, and <see cref="token()"/>.
/// </remarks>
/// <example>
/// <code>
/// public final class ExampleRecipe extends AbstractGlitchShapedRecipe {
///     @Override protected GlitchKey key() { return GlitchKey.DREAM; }
///     @Override protected String name() { return "glitch_example"; }
///     @Override protected List&lt;String&gt; shape() { return List.of("AAA", "ABA", "AAA"); }
///     @Override protected Map&lt;Character, RecipeChoice&gt; ingredients() {
///         return Map.of('A', new RecipeChoice.MaterialChoice(Material.AMETHYST_SHARD),
///                       'B', new RecipeChoice.MaterialChoice(Material.BEACON));
///     }
///     @Override protected IDreamItemStack token() { return new DreamGlitchToken(); }
/// }
/// </code>
/// </example>
public abstract class AbstractGlitchShapedRecipe implements IDreamRecipe {

    /// <summary>Token type that this recipe crafts.</summary>
    /// <returns>The <see cref="GlitchKey"/> for the token.</returns>
    protected abstract GlitchKey key();

    /// <summary>Unique recipe name (used for registration).</summary>
    /// <returns>Lowercase snake-case like <c>glitch_dupe</c>.</returns>
    protected abstract String name();

    /// <summary>Three-line shaped pattern.</summary>
    /// <returns>Exactly three rows, 1–3 characters each.</returns>
    protected abstract List<String> shape();

    /// <summary>Mapping from pattern chars to acceptable ingredients.</summary>
    /// <returns>Symbol → <see cref="RecipeChoice"/> map.</returns>
    protected abstract Map<Character, RecipeChoice> ingredients();

    /// <summary>Token definition produced by this recipe.</summary>
    /// <returns>Concrete <see cref="IDreamItemStack"/>.</returns>
    protected abstract IDreamItemStack token();

    /// <summary>Type of this recipe.</summary>
    /// <returns><see cref="RecipeType.ShapedRecipe"/>.</returns>
    @Override
    public RecipeType recipeType() { return RecipeType.ShapedRecipe; }

    /// <summary>Registration name for this recipe.</summary>
    /// <returns>Value from <see cref="name()"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override
    public String recipeName() { return name(); }

    /// <summary>Shaped pattern rows.</summary>
    /// <returns>Value from <see cref="shape()"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override
    public List<String> recipeShape() { return shape(); }

    /// <summary>Ingredient mappings for the pattern.</summary>
    /// <returns>Copy of <see cref="ingredients()"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override
    public HashMap<Character, RecipeChoice> recipeMaterials() { return new HashMap<>(ingredients()); }

    /// <summary>Builds the resulting item for this recipe.</summary>
    /// <returns>The built <see cref="ItemStack"/>.</returns>
    /// <remarks><inheritdoc/></remarks>
    @Override
    public ItemStack recipeResult() {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(getClass());
        return DreamItemStacks.build(plugin, token());
    }
}