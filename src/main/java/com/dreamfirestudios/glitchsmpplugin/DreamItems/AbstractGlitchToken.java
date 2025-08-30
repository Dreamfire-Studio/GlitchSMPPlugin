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

import com.dreamfirestudios.dreamcore.DreamItems.IDreamItemStack;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * <summary>
 * Base implementation for Glitch Token item definitions.
 * </summary>
 * <remarks>
 * <p>
 * Subclasses override the protected accessors to provide their identity and presentation:
 * {@link #key()}, {@link #name()}, {@link #material()}, {@link #itemLore()}, {@link #cmd()},
 * {@link #glow()}, {@link #unstackable()}.
 * </p>
 * <p>
 * This class implements {@link IDreamItemStack} and writes identifying PDC fields via
 * {@link #writePdc(Plugin, PersistentDataContainer)} so tokens can be recognized elsewhere.
 * </p>
 * </remarks>
 * <example>
 * <code>
 * public final class FreezeGlitchToken extends AbstractGlitchToken {
 *     &#64;Override protected GlitchKey key() { return GlitchKey.FREEZE; }
 *     &#64;Override protected Material material() { return Material.PACKED_ICE; }
 *     &#64;Override protected Component name() { return Component.text("Freeze Glitch"); }
 *     &#64;Override protected List&lt;Component&gt; itemLore() {
 *         return List.of(Component.text("— Root for ~30s; block pearls/items."));
 *     }
 * }
 * </code>
 * </example>
 */
public abstract class AbstractGlitchToken implements IDreamItemStack {

    // ---------- Overridable accessors ----------

    /**
     * <summary>
     * Unique glitch key identifying this token type.
     * </summary>
     * <returns>The {@link GlitchKey} for this token.</returns>
     */
    protected abstract GlitchKey key();

    /**
     * <summary>
     * Display name for the token item.
     * </summary>
     * <returns>Defaults to "<c>{KEY} Glitch</c>" where KEY is {@link #key()}.</returns>
     */
    protected Component name() { return Component.text(key().name() + " Glitch"); }

    /**
     * <summary>
     * Material used for the token item.
     * </summary>
     * <returns>{@link Material#PAPER} by default.</returns>
     */
    protected Material material() { return Material.PAPER; }

    /**
     * <summary>
     * Lore lines presented on the item.
     * </summary>
     * <returns>Empty list by default.</returns>
     */
    protected List<Component> itemLore() { return List.of(); }

    /**
     * <summary>
     * Optional custom model data for resource pack mapping.
     * </summary>
     * <returns>{@link OptionalInt#empty()} by default.</returns>
     */
    protected OptionalInt cmd() { return OptionalInt.empty(); }

    /**
     * <summary>
     * Whether the token should visually glow via a hidden enchantment.
     * </summary>
     * <returns><c>true</c> by default.</returns>
     */
    protected boolean glow() { return true; }

    /**
     * <summary>
     * Whether the token should be unstackable via a random UUID in PDC.
     * </summary>
     * <returns><c>true</c> by default.</returns>
     */
    protected boolean unstackable() { return true; }

    // ---------- IDreamItemStack contract ----------

    /**
     * <summary>
     * Stable registry ID used by DreamItemStacks.
     * </summary>
     * <returns>String of form <c>glitch_token:{key}</c> in lowercase.</returns>
     */
    @Override public String id() { return "glitch_token:" + key().name().toLowerCase(); }

    /**
     * <summary>
     * The display name to apply to the Bukkit ItemStack.
     * </summary>
     * <returns>Value from {@link #name()}.</returns>
     */
    @Override public Component displayName() { return name(); }

    /**
     * <summary>
     * The Bukkit material for the item.
     * </summary>
     * <returns>Value from {@link #material()}.</returns>
     */
    @Override public Material type() { return material(); }

    /**
     * <summary>
     * Default amount for the stack produced by the factory.
     * </summary>
     * <returns>Always <c>1</c>.</returns>
     */
    @Override public int amount() { return 1; }

    /**
     * <summary>
     * Custom model data, if any.
     * </summary>
     * <returns>Value from {@link #cmd()}.</returns>
     */
    @Override public OptionalInt customModelData() { return cmd(); }

    /**
     * <summary>
     * Lore lines for the item.
     * </summary>
     * <returns>Value from {@link #itemLore()}.</returns>
     */
    @Override public List<Component> lore() { return itemLore(); }

    /**
     * <summary>
     * Item flags applied to the token for presentation/cleanup.
     * </summary>
     * <returns>
     * If {@link #glow()} is true, hides enchants, attributes, and unbreakable.
     * Otherwise, hides attributes and unbreakable.
     * </returns>
     */
    @Override public Set<ItemFlag> flags() {
        return glow()
                ? Set.of(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)
                : Set.of(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
    }

    /**
     * <summary>
     * Enchantments applied to simulate glow if enabled.
     * </summary>
     * <returns>{@link Enchantment#LUCK_OF_THE_SEA} level 1 when glowing, otherwise empty.</returns>
     */
    @Override public Map<Enchantment, Integer> enchantments() {
        return glow() ? Map.of(Enchantment.LUCK_OF_THE_SEA, 1) : Map.of();
    }

    /**
     * <summary>
     * Attribute modifiers applied to the item.
     * </summary>
     * <returns>Empty by default.</returns>
     */
    @Override public Map<Attribute, Collection<AttributeModifier>> attributeModifiers() { return Map.of(); }

    /**
     * <summary>
     * Writes identification data to the item's {@link PersistentDataContainer}.
     * </summary>
     * <param name="plugin">Owning plugin used to scope {@link org.bukkit.NamespacedKey} values.</param>
     * <param name="pdc">Target container to write fields to.</param>
     * <remarks>
     * Writes:
     * <list type="bullet">
     *   <item><description><c>glitch_key</c> = {@link GlitchKey#name()}</description></item>
     *   <item><description><c>uid</c> = random UUID (if {@link #unstackable()} is true)</description></item>
     * </list>
     * </remarks>
     * <example>
     * <code>
     * token.writePdc(plugin, itemMeta.getPersistentDataContainer());
     * </code>
     * </example>
     */
    @Override
    public void writePdc(Plugin plugin, PersistentDataContainer pdc) {
        pdc.set(GlitchItemKeys.glitchKey(plugin), PersistentDataType.STRING, key().name());
        if (unstackable()) {
            pdc.set(GlitchItemKeys.uid(plugin), PersistentDataType.STRING, UUID.randomUUID().toString());
        }
    }
}