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
package com.dreamfirestudios.glitchsmpplugin.Enum;

/// <summary>
/// Identifiers for all supported Glitch token/ability types.
/// </summary>
/// <remarks>
/// Used across items, recipes, PDC storage, events, and the executor switchboard.
/// </remarks>
public enum GlitchKey {
    /// <summary>Forces an opponent disconnect (throttle-style kick).</summary>
    CRASH,

    /// <summary>Disables redstone mechanics temporarily.</summary>
    REDSTONE,

    /// <summary>Disguise + improved mob drop luck.</summary>
    DREAM,

    /// <summary>Duplicates specific items to a small cap (e.g., totems/potions).</summary>
    DUPE,

    /// <summary>Scrambles opponent inventory/hotbar.</summary>
    INVENTORY,

    /// <summary>Prevents weapon usage for a short period.</summary>
    ITEM_DISABLE,

    /// <summary>Herobrine-like disguise, Speed II, lightning chance.</summary>
    HEROBRINE,

    /// <summary>Roots target; blocks pearls/items for a short period.</summary>
    FREEZE,

    /// <summary>Amplifies current potion effects (e.g., I → II).</summary>
    EFFECT_AMPLIFY,

    /// <summary>Full damage immunity for a short window.</summary>
    IMMUNITY,

    /// <summary>Short-range blink to looked block.</summary>
    TELEPORT,

    /// <summary>Launch + sustained glide (cooldown-gated).</summary>
    GLIDE,

    /// <summary>Full invisibility including armor/held items.</summary>
    INVISIBILITY,

    /// <summary>Diffuses/suppresses other glitches temporarily.</summary>
    DIFFUSER,

    /// <summary>Copies target’s skin and armor trims.</summary>
    MORPH
}