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
import com.dreamfirestudios.glitchsmpplugin.Util.DiffuserGuard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Objects;

/**
 * <summary>
 * Diffuser Glitch: globally diffuses (blocks) other glitches for a duration.
 * </summary>
 * <remarks>
 * Enables the shared {@link DiffuserGuard} which upstream ability checks should honor.
 * </remarks>
 * <example>
 * <code>
 * DiffuserGlitch.execute(player); // blocks other glitches for 30 seconds
 * </code>
 * </example>
 */
public final class DiffuserGlitch {
    /// <summary>Default duration for diffusion.</summary>
    public static final Duration DEFAULT_DURATION = Duration.ofSeconds(30);

    private DiffuserGlitch() {}

    /**
     * <summary>Activates the diffuser effect for the caster.</summary>
     * <param name="caster">Player enabling diffusion.</param>
     * <returns>Always <c>true</c> to indicate handled.</returns>
     */
    public static boolean execute(Player caster) {
        Objects.requireNonNull(caster, "caster");
        caster.getWorld().spawnParticle(Particle.ASH, caster.getLocation().add(0, 1.0, 0),
                40, 0.6, 0.6, 0.6, 0.01);
        caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 0.8f);
        GlitchSMPPlugin.GetDiffuserGuard().enable(DEFAULT_DURATION, caster);
        caster.sendMessage(Component.text("You diffused all glitches for 30s.", NamedTextColor.GRAY));
        return true;
    }
}