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
package com.dreamfirestudios.glitchsmpplugin.SmartInvs;

import com.dreamfirestudios.dreamcore.DreamSmartInvs.ClickableItem;
import com.dreamfirestudios.glitchsmpplugin.Core.EventBus;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginInventoryItems;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginInventoryItemsConfig;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginSerializableItems;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class GlitchSMPPluginSmartInvsItems {

    private GlitchSMPPluginSmartInvsItems() { }

    public static void SerializedItem(final Player player, final String itemID, final Function<ItemStack, ItemStack> mutator, final Consumer<ClickableItem> place, final BiConsumer<Player, InventoryClickEvent> onClick) {
        Objects.requireNonNull(itemID, "itemID");
        Objects.requireNonNull(mutator, "mutator");
        Objects.requireNonNull(place, "place");
        Objects.requireNonNull(onClick, "onClick");

        final EventBus bus = new EventBus(GlitchSMPPlugin.GetGlitchSMPPlugin());

        GlitchSMPPluginSerializableItems.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginSerializableItems.class, cfg -> {
                    final ItemStack src = cfg.GetItemStack(itemID);
                    if (src == null) return;
                    ItemStack item = mutator.apply(src.clone());
                    if (item == null) item = src.clone();
                    final ClickableItem ci = ClickableItem.of(item, e -> onClick.accept(player, e));
                    bus.runMain(() -> place.accept(ci));
                }
        );
    }

    public static void InventoryItemWithFeedback(final Player player, final GlitchSMPPluginInventoryItems key, final Function<ItemStack, ItemStack> mutator, final Consumer<ClickableItem> place, final BiConsumer<Player, InventoryClickEvent> onClick) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(mutator, "mutator");
        Objects.requireNonNull(place, "place");
        Objects.requireNonNull(onClick, "onClick");
        final EventBus bus = new EventBus(GlitchSMPPlugin.GetGlitchSMPPlugin());
        GlitchSMPPluginInventoryItemsConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginInventoryItemsConfig.class, cfg -> {
                    ItemStack base = cfg.GetValue(key);
                    if (base == null) return;
                    ItemStack item = mutator.apply(base.clone());
                    if (item == null) item = base.clone();
                    final ClickableItem ci = ClickableItem.of(item, e -> onClick.accept(player, e));
                    bus.runMain(() -> place.accept(ci));
                }
        );
    }

    public static void InventoryItem(final Player player, final GlitchSMPPluginInventoryItems key, final Consumer<ClickableItem> place, final BiConsumer<Player, InventoryClickEvent> onClick) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(place, "place");
        Objects.requireNonNull(onClick, "onClick");
        final EventBus bus = new EventBus(GlitchSMPPlugin.GetGlitchSMPPlugin());
        GlitchSMPPluginInventoryItemsConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginInventoryItemsConfig.class, cfg -> {
                    ItemStack base = cfg.GetValue(key);
                    if (base == null) return;
                    final ClickableItem ci = ClickableItem.of(base.clone(), e -> onClick.accept(player, e));
                    bus.runMain(() -> place.accept(ci));
                }
        );
    }
}
