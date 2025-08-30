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
package com.dreamfirestudios.glitchsmpplugin.DreamPersistentData;

import com.dreamfirestudios.dreamcore.DreamPersistentData.DreamPersistentEntity;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <summary>Persists a player's Glitch loadout using DreamPersistentEntity on the Player's PDC.</summary>
 * <remarks>
 * Keys are stored under DreamCore's plugin namespace (we pass <c>null</c> to DreamPersistentEntity so it uses
 * <c>DreamCore.DreamCore</c> consistently). This matches your helper's {@code Get(...)} implementation.
 * </remarks>
 * <example>
 * <code>
 * GlitchLoadoutPdcStore store = new GlitchLoadoutPdcStore();
 * store.setSlot(player, 0, GlitchKey.TELEPORT);
 * store.setPrimary(player, 0);
 * Optional&lt;GlitchKey&gt; first = store.getSlot(player, 0);
 * </code>
 * </example>
 */
public final class GlitchLoadoutPdcStore {

    /// <summary>Total UI slots (1×9).</summary>
    public static final int SLOT_COUNT = 9;

    /// <summary>Number of ACTIVE slots shown in purple (indices 0..2 by convention).</summary>
    public static final int ACTIVE_SLOTS = 3;

    private static final String SLOT_KEY_PREFIX = "glitch/slot_";   // e.g., glitch/slot_0 .. glitch/slot_8
    private static final String PRIMARY_KEY     = "glitch/primary"; // byte 0..2

    /**
     * <summary>Validate slot index.</summary>
     */
    private static void checkIndex(int index) {
        if (index < 0 || index >= SLOT_COUNT) {
            throw new IndexOutOfBoundsException("index must be in [0," + (SLOT_COUNT - 1) + "]");
        }
    }

    /**
     * <summary>Build the PDC key for a specific slot.</summary>
     * <param name="index">0..8</param>
     * <returns>Logical key string accepted by DreamPersistentEntity.</returns>
     */
    private static String slotKey(int index) {
        return SLOT_KEY_PREFIX + index;
    }

    /**
     * <summary>Read the glitch assigned to a menu slot.</summary>
     * <param name="player">Player to read from.</param>
     * <param name="index">Slot index (0..8).</param>
     * <returns>Present if a glitch is stored; empty otherwise.</returns>
     */
    public Optional<GlitchKey> getSlot(Player player, int index) {
        Objects.requireNonNull(player, "player");
        checkIndex(index);
        String raw = DreamPersistentEntity.Get(player, slotKey(index), PersistentDataType.STRING);
        if (raw == null) return Optional.empty();
        try {
            return Optional.of(GlitchKey.valueOf(raw));
        } catch (IllegalArgumentException ex) {
            // Corrupt/outdated value — treat as empty.
            return Optional.empty();
        }
    }

    /**
     * <summary>Assign or clear a glitch in a menu slot.</summary>
     * <param name="player">Target player.</param>
     * <param name="index">Slot index (0..8).</param>
     * <param name="glitchOrNull">Glitch to store, or <c>null</c> to clear.</param>
     */
    public void setSlot(Player player, int index, GlitchKey glitchOrNull) {
        Objects.requireNonNull(player, "player");
        checkIndex(index);
        if (glitchOrNull == null) {
            DreamPersistentEntity.Remove(null, player, slotKey(index)); // null → use DreamCore.DreamCore namespace
        } else {
            DreamPersistentEntity.Add(null, player, PersistentDataType.STRING, slotKey(index), glitchOrNull.name());
        }
    }

    /**
     * <summary>Return all nine slots (empty if not set).</summary>
     * <param name="player">Target player.</param>
     * <returns>List of 9 optionals (indices 0..8).</returns>
     */
    public List<Optional<GlitchKey>> getAll(Player player) {
        Objects.requireNonNull(player, "player");
        List<Optional<GlitchKey>> out = new ArrayList<>(SLOT_COUNT);
        for (int i = 0; i < SLOT_COUNT; i++) {
            out.add(getSlot(player, i));
        }
        return out;
    }

    /**
     * <summary>Get the primary active index (0..2). Defaults to 0 if missing/out of range.</summary>
     * <param name="player">Target player.</param>
     * <returns>Primary index in [0,2].</returns>
     */
    public int getPrimary(Player player) {
        Objects.requireNonNull(player, "player");
        Byte b = DreamPersistentEntity.Get(player, PRIMARY_KEY, PersistentDataType.BYTE);
        int idx = (b == null) ? 0 : b.intValue();
        if (idx < 0 || idx >= ACTIVE_SLOTS) return 0;
        return idx;
    }

    /**
     * <summary>Set the primary active index (0..2). Value is clamped.</summary>
     * <param name="player">Target player.</param>
     * <param name="index">Desired index.</param>
     */
    public void setPrimary(Player player, int index) {
        Objects.requireNonNull(player, "player");
        int clamped = Math.max(0, Math.min(ACTIVE_SLOTS - 1, index));
        DreamPersistentEntity.Add(null, player, PersistentDataType.BYTE, PRIMARY_KEY, (byte) clamped);
    }

    /**
     * <summary>Advance the primary pointer to the next active slot.</summary>
     * <param name="player">Target player.</param>
     * <returns>New primary index.</returns>
     */
    public int cyclePrimary(Player player) {
        int next = (getPrimary(player) + 1) % ACTIVE_SLOTS;
        setPrimary(player, next);
        return next;
    }

    /**
     * <summary>Clear all stored slots and primary pointer.</summary>
     * <param name="player">Target player.</param>
     */
    public void clear(Player player) {
        Objects.requireNonNull(player, "player");
        for (int i = 0; i < SLOT_COUNT; i++) {
            DreamPersistentEntity.Remove(null, player, slotKey(i));
        }
        DreamPersistentEntity.Remove(null, player, PRIMARY_KEY);
    }
}