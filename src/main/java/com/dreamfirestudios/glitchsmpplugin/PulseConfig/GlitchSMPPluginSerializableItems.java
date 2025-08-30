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

import com.dreamfirestudios.dreamconfig.Abstract.StaticPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamconfig.SaveableObjects.SaveableHashmap;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <summary>Serializable item storage.</summary>
 * <remarks>
 * Provides a string-ID to {@link ItemStack} mapping persisted by {@link StaticPulseConfig}.
 * </remarks>
 * <example>
 * <code>
 * GlitchSMPPluginSerializableItems.ReturnStaticAsync(plugin, GlitchSMPPluginSerializableItems.class, cfg -&gt; {
 *     cfg.AddItemStack("core.icon", someItem);
 *     cfg.SaveDreamConfig(plugin, updated -&gt; {});
 * });
 * </code>
 * </example>
 */
@PulseAutoRegister
@ConfigVersion(1)
public class GlitchSMPPluginSerializableItems extends StaticPulseConfig<GlitchSMPPluginSerializableItems> {

    /** <summary>Plugin binding.</summary> */
    @Override
    public JavaPlugin mainClass() {return GlitchSMPPlugin.GetGlitchSMPPlugin();}

    /** <summary>Backing map for ID -&gt; ItemStack.</summary> */
    public SaveableHashmap<String, ItemStack> itemStackSaveableHashmap = new SaveableHashmap<>(String.class, ItemStack.class);

    /**
     * <summary>Adds or replaces an {@link ItemStack} under the given ID.</summary>
     * <param name="id">String identifier.</param>
     * <param name="itemStack">Item to store.</param>
     */
    public void AddItemStack(String id, ItemStack itemStack){
        itemStackSaveableHashmap.getHashMap().put(id, itemStack);
    }

    /** <summary>Store at root (no subfolder).</summary> */
    @Override
    public boolean useSubFolder() {
        return false;
    }

    /**
     * <summary>Retrieves an item by ID.</summary>
     * <param name="id">String identifier.</param>
     * <returns>ItemStack if present; otherwise <c>null</c>.</returns>
     */
    public ItemStack GetItemStack(String id){
        return itemStackSaveableHashmap.getHashMap().getOrDefault(id, null);
    }
}