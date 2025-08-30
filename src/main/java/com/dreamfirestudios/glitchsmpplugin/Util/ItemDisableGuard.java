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
 * Tracks per-player disable windows for the “Item Disable” glitch (e.g., swords).
 * </summary>
 */
public final class ItemDisableGuard {

    private final Map<UUID, Instant> until = new ConcurrentHashMap<>();

    /// <summary>Disable target’s items for duration.</summary>
    public void disable(UUID id, Duration duration) {
        if (id == null || duration == null || duration.isZero() || duration.isNegative()) return;
        until.put(id, Instant.now().plus(duration));
    }

    /// <summary>True if disable is active now.</summary>
    public boolean isActive(UUID id) {
        if (id == null) return false;
        Instant u = until.get(id);
        if (u == null) return false;
        if (Instant.now().isBefore(u)) return true;
        until.remove(id);
        return false;
    }

    /// <summary>Remaining disable time; zero if expired.</summary>
    public java.time.Duration remaining(UUID id) {
        Instant u = until.get(id);
        if (u == null) return java.time.Duration.ZERO;
        java.time.Duration left = java.time.Duration.between(java.time.Instant.now(), u);
        if (!left.isPositive()) {
            until.remove(id);
            return java.time.Duration.ZERO;
        }
        return left;
    }
}