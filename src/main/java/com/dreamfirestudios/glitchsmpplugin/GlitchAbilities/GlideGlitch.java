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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;

/**
 * <summary>
 * Glide Glitch: launches the player and grants long Slow Falling; a guard ends glide on landing.
 * </summary>
 * <remarks>
 * Applies a forward+upward velocity boost and adds a long Slow Falling effect that is cleaned up externally.
 * </remarks>
 */
public final class GlideGlitch {

    /// <summary>Upward push.</summary>
    private static final double LAUNCH_Y = 1.1;
    /// <summary>Forward push magnitude.</summary>
    private static final double LAUNCH_FORWARD = 1.2;
    /// <summary>Very long Slow Falling, removed on landing.</summary>
    private static final int SLOW_FALL_TICKS = 20 * 60 * 10; // 10 minutes

    private GlideGlitch() {}

    /**
     * <summary>Activate Glide for the player.</summary>
     * <param name="player">Caster.</param>
     * <returns><c>false</c> if blocked by Diffuser; else <c>true</c>.</returns>
     */
    public static boolean execute(Player player) {
        Objects.requireNonNull(player, "player");

        Vector dir = player.getLocation().getDirection().normalize();
        Vector boost = dir.multiply(LAUNCH_FORWARD).setY(LAUNCH_Y);
        player.setVelocity(player.getVelocity().add(boost));

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOW_FALLING,
                SLOW_FALL_TICKS,
                0,
                false,
                true,
                true
        ));

        GlitchSMPPlugin.GetGlideGuard().start(player.getUniqueId());

        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0, 1, 0),
                30, 0.6, 0.6, 0.6, 0.02);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1f, 0.8f);
        player.sendMessage(Component.text("You feel weightless… glide away!", NamedTextColor.AQUA));
        return true;
    }
}