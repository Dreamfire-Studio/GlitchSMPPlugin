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

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <summary>
 * Tracks players currently in Glide mode (until they land or are manually cleared).
 * </summary>
 * <remarks>
 * No timers: Glide ends when the player touches ground (handled by listener).
 * </remarks>
 */
public final class GlideGuard {

    private final Set<UUID> active = ConcurrentHashMap.newKeySet();

    /// <summary>Enable glide for a player.</summary>
    public void start(UUID id) { if (id != null) active.add(id); }

    /// <summary>Disable glide for a player.</summary>
    public void stop(UUID id) { if (id != null) active.remove(id); }

    /// <summary>Returns true if the player is currently gliding under this guard.</summary>
    public boolean isActive(UUID id) { return id != null && active.contains(id); }

    /// <summary>Clears all active gliders.</summary>
    public void clear() { active.clear(); }
}