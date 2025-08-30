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

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <summary>
 * Global “diffuser” guard that disables all glitches system-wide for a short period.
 * </summary>
 * <remarks>
 * Uses a single atomic expiry timestamp. Broadcasts an announcement when enabled.
 * </remarks>
 */
public final class DiffuserGuard {

    private final AtomicReference<Instant> untilRef = new AtomicReference<>(null);

    /**
     * <summary>
     * Enables diffusion for the given duration and optionally announces globally.
     * </summary>
     * <param name="duration">Active window.</param>
     * <param name="announce">Player to name in broadcast; may be <c>null</c>.</param>
     */
    public void enable(Duration duration, Player announce) {
        Objects.requireNonNull(duration, "duration");
        if (duration.isZero() || duration.isNegative()) return;

        Instant until = Instant.now().plus(duration);
        untilRef.set(until);

        if (announce != null) {
            Bukkit.getServer().broadcast(Component.text(
                    "Glitches diffused for " + duration.toSeconds() + "s by " + announce.getName()
            ));
        }
    }

    /**
     * <summary>
     * Returns remaining diffusion time; clears state if expired.
     * </summary>
     * <returns>Remaining duration or <c>Duration.ZERO</c> if inactive.</returns>
     */
    public Duration remaining() {
        Instant until = untilRef.get();
        if (until == null) return Duration.ZERO;

        Duration left = Duration.between(Instant.now(), until);
        if (left.isNegative() || left.isZero()) {
            // Clear on read to avoid stale state
            untilRef.compareAndSet(until, null);
            return Duration.ZERO;
        }
        return left;
    }

    /**
     * <summary>True if diffusion is currently active.</summary>
     */
    public boolean isActive() {
        return remaining().isPositive();
    }
}