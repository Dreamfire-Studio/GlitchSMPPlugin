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

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <summary>
 * Guard that temporarily blocks specific players from joining (used by the Crash Glitch).
 * </summary>
 * <remarks>
 * Stores a per-UUID expiry time. {@link #isBlocked(UUID)} both checks and lazily clears expired entries.
 * </remarks>
 */
public final class CrashGuard {
    private final Map<UUID, Instant> CrashGlitchBlocks = new ConcurrentHashMap<>();

    /**
     * <summary>
     * Blocks a player for a duration.
     * </summary>
     * <param name="id">Player UUID.</param>
     * <param name="duration">How long the block should last.</param>
     */
    public void block(UUID id, Duration duration) {
        if (duration.isZero() || duration.isNegative()) return;
        CrashGlitchBlocks.put(id, Instant.now().plus(duration));
    }

    /**
     * <summary>
     * Returns whether the player is currently blocked. Automatically clears expired entries.
     * </summary>
     * <param name="id">Player UUID.</param>
     * <returns><c>true</c> if the block is still active; otherwise <c>false</c>.</returns>
     */
    public boolean isBlocked(UUID id) {
        var until = CrashGlitchBlocks.get(id);
        if (until == null) return false;
        if (Instant.now().isBefore(until)) return true;
        CrashGlitchBlocks.remove(id);
        return false;
    }
}