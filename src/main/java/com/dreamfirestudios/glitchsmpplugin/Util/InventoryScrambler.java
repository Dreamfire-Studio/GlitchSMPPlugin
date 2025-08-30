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
package com.dreamfirestudios.glitchsmpplugin.Util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <summary>
 * Utility to scramble a player's hotbar and main inventory without touching armor/offhand.
 * </summary>
 */
public final class InventoryScrambler {

    private InventoryScrambler() {}

    /**
     * <summary>
     * Randomly permutes slots 0..8 (hotbar) and 9..35 (main inventory) independently.
     * </summary>
     * <param name="inv">Target player inventory.</param>
     */
    public static void scrambleHotbarAndMain(PlayerInventory inv) {
        if (inv == null) return;
        shuffleRange(inv, 0, 8);   // hotbar
        shuffleRange(inv, 9, 35);  // main
    }

    /**
     * <summary>
     * Shuffles a contiguous slot range in the given player inventory.
     * </summary>
     * <param name="inv">Inventory to mutate.</param>
     * <param name="start">Start slot index (inclusive).</param>
     * <param name="endInclusive">End slot index (inclusive).</param>
     */
    private static void shuffleRange(PlayerInventory inv, int start, int endInclusive) {
        List<Integer> slots = new ArrayList<>();
        for (int i = start; i <= endInclusive; i++) slots.add(i);

        // snapshot
        List<ItemStack> items = new ArrayList<>(slots.size());
        for (int s : slots) items.add(cloneOrNull(inv.getItem(s)));

        // shuffle
        Collections.shuffle(items, ThreadLocalRandom.current());

        // apply
        for (int idx = 0; idx < slots.size(); idx++) {
            inv.setItem(slots.get(idx), items.get(idx));
        }
    }

    private static ItemStack cloneOrNull(ItemStack in) {
        return (in == null) ? null : in.clone();
    }
}