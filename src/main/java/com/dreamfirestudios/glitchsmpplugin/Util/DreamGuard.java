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
 * Tracks “dream luck” active windows per player (used by Dream Glitch).
 * </summary>
 * <remarks>
 * Accessors are self-cleaning: expired entries are removed on read.
 * </remarks>
 */
public final class DreamGuard {
    private final Map<UUID, Instant> ACTIVE_UNTIL = new ConcurrentHashMap<>();

    /**
     * <summary>Enables dream luck for a player.</summary>
     * <param name="playerId">Player UUID.</param>
     * <param name="duration">How long it should last.</param>
     */
    public void enable(UUID playerId, Duration duration) {
        if (duration == null || duration.isNegative() || duration.isZero()) return;
        ACTIVE_UNTIL.put(playerId, Instant.now().plus(duration));
    }

    /**
     * <summary>Checks if dream luck is active for the player.</summary>
     * <param name="playerId">Player UUID.</param>
     * <returns><c>true</c> if active; otherwise <c>false</c>.</returns>
     */
    public boolean isActive(UUID playerId) {
        Instant until = ACTIVE_UNTIL.get(playerId);
        if (until == null) return false;
        if (Instant.now().isBefore(until)) return true;
        ACTIVE_UNTIL.remove(playerId);
        return false;
    }

    /**
     * <summary>Returns remaining dream-luck time.</summary>
     * <param name="playerId">Player UUID.</param>
     * <returns>Remaining time or <c>Duration.ZERO</c>.</returns>
     */
    public Duration remaining(UUID playerId) {
        Instant until = ACTIVE_UNTIL.get(playerId);
        if (until == null) return Duration.ZERO;
        Duration left = Duration.between(Instant.now(), until);
        if (left.isNegative() || left.isZero()) {
            ACTIVE_UNTIL.remove(playerId);
            return Duration.ZERO;
        }
        return left;
    }
}