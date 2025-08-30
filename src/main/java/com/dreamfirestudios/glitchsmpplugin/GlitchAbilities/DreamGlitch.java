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
package com.dreamfirestudios.glitchsmpplugin.GlitchAbilities;

import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Objects;

/**
 * <summary>
 * Dream Glitch: grants temporary "luck" effects handled by a guard, with light FX/feedback.
 * </summary>
 * <remarks>
 * Actual loot/behavior adjustments are implemented by the Dream guard/listeners.
 * </remarks>
 */
public final class DreamGlitch {
    /// <summary>Active duration for the Dream effect.</summary>
    public static final Duration DURATION = Duration.ofSeconds(30);

    private DreamGlitch() {}

    /**
     * <summary>Enables the Dream effect on the player.</summary>
     * <param name="player">Target player.</param>
     * <returns>Always <c>true</c> to indicate handled.</returns>
     */
    public static boolean execute(Player player) {
        Objects.requireNonNull(player, "player");
        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER,
                player.getLocation().add(0, 1.0, 0), 30, 0.5, 0.5, 0.5, 0.05);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1.2f);
        GlitchSMPPlugin.GetDreamGuard().enable(player.getUniqueId(), DURATION);
        long secs = Math.max(1, DURATION.toSeconds());
        player.sendMessage(Component.text("Dream luck active for " + secs + "s!", NamedTextColor.GREEN));
        return true;
    }
}