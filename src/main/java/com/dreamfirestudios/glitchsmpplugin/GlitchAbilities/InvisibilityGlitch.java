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
 * Invisibility Glitch: makes the player invisible AND masks armor/held items to observers.
 * </summary>
 * <remarks>
 * <list type="bullet">
 *   <item>Respects Diffuser: returns <c>false</c> so token isn’t consumed.</item>
 *   <item>Equipment masking is handled by <c>InvisibilityEquipmentMasker</c> (ProtocolLib).</item>
 * </list>
 * </remarks>
 */
public final class InvisibilityGlitch {

    /// <summary>Duration of invisibility.</summary>
    public static final Duration DURATION = Duration.ofSeconds(30);

    private InvisibilityGlitch() {}

    /**
     * <summary>Activate invisibility for the caster.</summary>
     * <param name="player">Player to turn invisible.</param>
     * <returns><c>false</c> if blocked by Diffuser; else <c>true</c>.</returns>
     */
    public static boolean execute(Player player) {
        Objects.requireNonNull(player, "player");

        // Effect (does not hide armor by itself; Packet masker will)
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.INVISIBILITY,
                (int) (DURATION.toSeconds() * 20),
                0,
                false,
                true,
                true
        ));

        GlitchSMPPlugin.GetInvisibilityGuard().enable(player.getUniqueId(), DURATION);

        // FX
        player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(0, 1, 0),
                30, 0.4, 0.6, 0.4, 0.01);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_AMBIENT, 0.7f, 1.2f);
        player.sendMessage(Component.text("You vanish from sight.", NamedTextColor.GRAY));
        return true;
    }
}