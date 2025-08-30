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
 * Tracks “Herobrine” empower windows per player.
 * </summary>
 * <remarks>
 * Read access lazily clears expired entries.
 * </remarks>
 */
public final class HerobrineGuard {

    private final Map<UUID, Instant> until = new ConcurrentHashMap<>();

    /**
     * <summary>Enables Herobrine effect for the player.</summary>
     * <param name="id">Player UUID.</param>
     * <param name="duration">Duration to remain active.</param>
     */
    public void enable(UUID id, Duration duration) {
        if (id == null || duration == null || duration.isZero() || duration.isNegative()) return;
        until.put(id, Instant.now().plus(duration));
    }

    /**
     * <summary>Returns whether the effect is active.</summary>
     * <param name="id">Player UUID.</param>
     * <returns><c>true</c> if active; otherwise <c>false</c>.</returns>
     */
    public boolean isActive(UUID id) {
        if (id == null) return false;
        Instant u = until.get(id);
        if (u == null) return false;
        if (Instant.now().isBefore(u)) return true;
        until.remove(id);
        return false;
    }

    /**
     * <summary>Remaining time for the effect.</summary>
     * <param name="id">Player UUID.</param>
     * <returns>Remaining duration or <c>Duration.ZERO</c>.</returns>
     */
    public Duration remaining(UUID id) {
        Instant u = until.get(id);
        if (u == null) return Duration.ZERO;
        Duration left = Duration.between(Instant.now(), u);
        if (!left.isPositive()) {
            until.remove(id);
            return Duration.ZERO;
        }
        return left;
    }
}