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
 * Immunity Glitch: grants full damage immunity for a fixed duration.
 * </summary>
 * <remarks>Guard handles actual damage cancellation; this method triggers FX and feedback.</remarks>
 */
public final class ImmunityGlitch {

    /// <summary>Default immunity duration.</summary>
    public static final Duration DURATION = Duration.ofSeconds(30);

    private ImmunityGlitch() {}

    /**
     * <summary>Apply immunity to the caster.</summary>
     * <param name="player">Activating player.</param>
     * <returns><c>false</c> if blocked by Diffuser; otherwise <c>true</c>.</returns>
     */
    public static boolean execute(Player player) {
        Objects.requireNonNull(player, "player");

        GlitchSMPPlugin.GetImmunityGuard().enable(player.getUniqueId(), DURATION);

        // FX + feedback
        player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(0, 1, 0),
                40, 0.5, 0.8, 0.5, 0.02);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.9f, 1.4f);
        player.sendMessage(Component.text("You are immune to damage for " + DURATION.toSeconds() + "s.", NamedTextColor.GOLD));
        return true;
    }
}