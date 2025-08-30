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
package com.dreamfirestudios.glitchsmpplugin.DreamKeyPressed;

import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcore.DreamKeyPressed.*;
import com.dreamfirestudios.glitchsmpplugin.DreamPersistentData.GlitchLoadoutPdcStore;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.Util.GlitchAbilityExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/// <summary>
/// Binds "Sneak + Right-Click" as an activator. When triggered:
/// <list type="number">
/// <item><description>Reads the player's first active glitch (slots 0..2) from Player PDC</description></item>
/// <item><description>Removes that glitch from PDC (token is consumed)</description></item>
/// <item><description>Dispatches to the per-glitch ability executor</description></item>
/// </list>
/// </summary>
/// <remarks>
/// Uses <see cref="GlitchLoadoutPdcStore"/> for persistence. The executor isolates gameplay logic.
/// </remarks>
/// <example>
/// <code>
/// // Handler is auto-registered via @PulseAutoRegister.
/// // Press SNEAK + RIGHT_CLICK in-game to activate the first available glitch.
/// </code>
/// </example>
@PulseAutoRegister
public final class SneakRightClickBinding implements IDreamKeyPatternSpec, IDreamKeyPressed {

    private static final GlitchLoadoutPdcStore STORE = new GlitchLoadoutPdcStore();

    /// <summary>Whether this binding works while a player is in an inventory GUI.</summary>
    /// <returns><c>false</c> (disabled in inventories).</returns>
    @Override public boolean worksInInventory() { return false; }

    /// <summary>How the key sequence is interpreted.</summary>
    /// <returns><see cref="DreamPressedType.AllAtOnce"/> (a chord).</returns>
    @Override public DreamPressedType pressedType() { return DreamPressedType.AllAtOnce; }

    /// <summary>Whether the binding should only trigger once per session.</summary>
    /// <returns><c>false</c> (can trigger repeatedly, subject to cooldown).</returns>
    @Override public boolean firstTimeOnly() { return false; }

    /// <summary>Cooldown between activations.</summary>
    /// <returns>500 ms.</returns>
    @Override public Duration cooldown() { return Duration.ofMillis(500); }

    /// <summary>Defines the chord/sequence for activation.</summary>
    /// <returns>Chord: SNEAK + RIGHT_CLICK within 1s spread.</returns>
    @Override
    public List<IDreamKeyStepSpec> steps() {
        return List.of(
                DreamChordDefault.of(EnumSet.of(DreamPressedKeys.SNEAK, DreamPressedKeys.RIGHT_CLICK),
                        DreamTimingDefault.chordSpread(Duration.ofSeconds(1)))
        );
    }

    /**
     * <summary>
     * Completion callback with the activating player's ID.
     * </summary>
     * <param name="playerId">The UUID of the player who performed the binding.</param>
     * <remarks>
     * Iterates active slots 0..2 and triggers the first available glitch. On success, clears that slot.
     * If the glitch is unimplemented, notifies the player.
     * </remarks>
     * <example>
     * <code>
     * // Called by the key-manager when the chord completes for a given playerId
     * binding.ActionComplete(player.getUniqueId());
     * </code>
     * </example>
     */
    @Override
    public void ActionComplete(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) return;
        for (int i = 0; i < GlitchLoadoutPdcStore.ACTIVE_SLOTS; i++) {
            var keyOpt = STORE.getSlot(player, i);
            if (keyOpt.isEmpty()) continue;
            GlitchKey key = keyOpt.get();
            boolean ok = GlitchAbilityExecutor.handle(GlitchSMPPlugin.GetGlitchSMPPlugin(), player, key);
            if (ok) {
                STORE.setSlot(player, i, null);
            } else {
                player.sendMessage(Component.text("That glitch isn’t implemented yet.", NamedTextColor.YELLOW));
            }
            return;
        }
    }

    /// <summary>Invoked when the binding fails to complete (e.g., invalid state).</summary>
    /// <param name="playerId">The player's UUID.</param>
    @Override public void FailedAction(UUID playerId) {
        // No-op: intentionally silent failure.
    }

    /**
     * <summary>Invoked when the binding is on cooldown.</summary>
     * <param name="playerId">The player's UUID.</param>
     * <param name="remaining">Remaining cooldown duration.</param>
     * <remarks>Sends an action bar message indicating remaining cooldown.</remarks>
     */
    @Override
    public void OnCooldown(UUID playerId, Duration remaining) {
        Player p = Bukkit.getPlayer(playerId);
        if (p != null) p.sendActionBar(Component.text("Glitch activator cooldown: " + remaining.toSeconds() + "s", NamedTextColor.GRAY));
    }
}