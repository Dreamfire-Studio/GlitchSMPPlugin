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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <summary>
 * Effect Amplify Glitch: upgrades all active level I potion effects to level II, preserving durations.
 * </summary>
 * <remarks>
 * Effects with amplifier &gt; 0 or with non-positive durations are ignored.
 * </remarks>
 * <example>
 * <code>
 * boolean ok = EffectAmplifyGlitch.execute(player);
 * </code>
 * </example>
 */
public final class EffectAmplifyGlitch {

    private EffectAmplifyGlitch() {}

    /**
     * <summary>Amplifies eligible active potion effects on the player.</summary>
     * <param name="player">Target player.</param>
     * <returns><c>true</c> if any effects were upgraded; otherwise <c>false</c>.</returns>
     */
    public static boolean execute(Player player) {
        Objects.requireNonNull(player, "player");

        List<PotionEffect> upgrades = new ArrayList<>();
        for (PotionEffect pe : player.getActivePotionEffects()) {
            if (pe == null) continue;
            PotionEffectType type = pe.getType();
            if (type == null) continue;
            if (pe.getAmplifier() == 0 && pe.getDuration() > 0) {
                upgrades.add(new PotionEffect(
                        type,
                        pe.getDuration(),
                        1,
                        pe.isAmbient(),
                        pe.hasParticles(),
                        pe.hasIcon()
                ));
            }
        }

        if (upgrades.isEmpty()) {
            player.sendMessage(Component.text("No level I effects to amplify.", NamedTextColor.GRAY));
            return false;
        }

        for (PotionEffect up : upgrades) {
            player.addPotionEffect(up, true);
        }

        player.getWorld().spawnParticle(Particle.ENCHANT, player.getLocation().add(0, 1.0, 0),
                50, 0.6, 0.6, 0.6, 0.02);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1.0f);
        player.sendMessage(Component.text("Your effects were amplified!", NamedTextColor.GREEN));
        return true;
    }
}