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

import java.time.Duration;
import java.util.Objects;

/**
 * <summary>
 * Herobrine Glitch: grants Speed II and enables a chance to call lightning on nearby entities
 * whenever the player is damaged. No skin changes included.
 * </summary>
 */
public final class HerobrineGlitch {

    /// <summary>Effect duration.</summary>
    public static final Duration DURATION = Duration.ofSeconds(30);

    private HerobrineGlitch() {}

    /**
     * <summary>Apply Herobrine effect to the player.</summary>
     * <param name="player">Caster.</param>
     * <returns><c>false</c> if blocked by Diffuser; else <c>true</c>.</returns>
     */
    public static boolean execute(Player player) {
        Objects.requireNonNull(player, "player");

        GlitchSMPPlugin.GetHerobrineGuard().enable(player.getUniqueId(), DURATION);

        // Speed II for the duration (refresh if already present)
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED,
                (int) (DURATION.toSeconds() * 20),
                1, // amplifier 1 => Speed II
                false,
                true,
                true
        ));

        // FX
        player.getWorld().spawnParticle(Particle.SOUL, player.getLocation().add(0, 1, 0),
                40, 0.6, 0.6, 0.6, 0.02);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.6f, 1.6f);
        player.sendMessage(Component.text("Herobrine’s power surges through you!", NamedTextColor.DARK_PURPLE));
        return true;
    }
}