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
package com.dreamfirestudios.glitchsmpplugin.Event;

import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/// <summary>
/// Bukkit event fired when a player attempts to use a glitch.
/// </summary>
/// <remarks>
/// <list type="bullet">
///   <item><description>Cancellable — if cancelled, the glitch does not execute and the executor returns <c>false</c>.</description></item>
///   <item><description>Allows plugins/systems to veto glitch use (e.g., regions, custom cooldowns).</description></item>
/// </list>
/// </remarks>
/// <example>
/// <code>
/// @EventHandler
/// public void onUse(GlitchUseEvent e) {
///     if (e.getGlitchKey() == GlitchKey.TELEPORT && !canTeleportHere(e.getPlayer())) {
///         e.setCancelled(true);
///     }
/// }
/// </code>
/// </example>
public final class GlitchUseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final GlitchKey glitchKey;
    private boolean cancelled;

    /// <summary>
    /// Creates a new GlitchUseEvent.
    /// </summary>
    /// <param name="player">The player invoking the glitch.</param>
    /// <param name="glitchKey">The glitch key being used.</param>
    /// <remarks>
    /// The event is marked async if constructed off the primary thread.
    /// </remarks>
    public GlitchUseEvent(Player player, GlitchKey glitchKey) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.glitchKey = glitchKey;
    }

    /// <summary>Player who is using the glitch.</summary>
    /// <returns>The invoking player.</returns>
    public Player getPlayer() { return player; }

    /// <summary>The glitch key being used.</summary>
    /// <returns>The <see cref="GlitchKey"/> of the attempt.</returns>
    public GlitchKey getGlitchKey() { return glitchKey; }

    /// <summary>Whether the event is cancelled.</summary>
    /// <returns><c>true</c> if cancelled.</returns>
    @Override public boolean isCancelled() { return cancelled; }

    /// <summary>Sets the cancellation state.</summary>
    /// <param name="cancel"><c>true</c> to prevent the glitch from executing.</param>
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }

    /// <summary>Required Bukkit handler accessor.</summary>
    /// <returns>Handler list for this event instance.</returns>
    @Override public HandlerList getHandlers() { return HANDLERS; }

    /// <summary>Static Bukkit handler list accessor.</summary>
    /// <returns>Global handler list for this event type.</returns>
    public static HandlerList getHandlerList() { return HANDLERS; }
}