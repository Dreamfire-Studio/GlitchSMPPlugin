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
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageFormatter;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcore.DreamPersistentData.DreamPersistentItemStack;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginInventoryItems;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * <summary>Inventory items configuration.</summary>
 * <remarks>
 * Generates default {@link ItemStack} definitions for {@link GlitchSMPPluginInventoryItems}.
 * </remarks>
 * <example>
 * <code>
 * GlitchSMPPluginInventoryItemsConfig.ReturnStaticAsync(plugin,
 *   GlitchSMPPluginInventoryItemsConfig.class, cfg -&gt; {
 *       ItemStack icon = cfg.GetValue(GlitchSMPPluginInventoryItems.BlankTile);
 *   });
 * </code>
 * </example>
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class GlitchSMPPluginInventoryItemsConfig
        extends StaticEnumPulseConfig<GlitchSMPPluginInventoryItemsConfig, GlitchSMPPluginInventoryItems, ItemStack> {

    /** <summary>Plugin binding.</summary> */
    @Override public JavaPlugin mainClass() { return GlitchSMPPlugin.GetGlitchSMPPlugin(); }

    /** <summary>Key enum type.</summary> */
    @Override protected Class<GlitchSMPPluginInventoryItems> getKeyClass() { return GlitchSMPPluginInventoryItems.class; }

    /** <summary>Value type.</summary> */
    @Override protected Class<ItemStack> getValueClass() { return ItemStack.class; }

    /** <summary>Store at root (no subfolder).</summary> */
    @Override public boolean useSubFolder() { return false; }

    /**
     * <summary>Provides a default {@link ItemStack} for the given key.</summary>
     * <param name="key">Inventory item key.</param>
     * <returns>Newly constructed immutable template {@link ItemStack}.</returns>
     */
    @Override
    protected ItemStack getDefaultValueFor(GlitchSMPPluginInventoryItems key) {
        var settings = DreamMessageSettings.all();
        var is = new ItemStack(key.itemMaterial);
        var meta = is.getItemMeta();
        meta.displayName(DreamMessageFormatter.format(key.displayName, settings));
        var lore = new ArrayList<Component>();
        for (var l : key.itemLore) lore.add(DreamMessageFormatter.format(l, settings));
        meta.lore(lore);
        meta.setCustomModelData(key.modelData);
        is.setItemMeta(meta);
        for (var k : key.keys) {
            DreamPersistentItemStack.Add(GlitchSMPPlugin.GetGlitchSMPPlugin(), is, PersistentDataType.STRING, k, k);
        }
        return is;
    }
}