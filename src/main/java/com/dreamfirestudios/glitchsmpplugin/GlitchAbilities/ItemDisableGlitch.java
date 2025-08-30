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

import com.dreamfirestudios.dreamcore.DreamRaycast.DreamRaycast;
import com.dreamfirestudios.dreamcore.DreamRaycast.DreamRaycast.RaycastHit;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

/**
 * <summary>
 * Item Disable Glitch: target opponent cannot use swords for the duration.
 * </summary>
 * <remarks>
 * Targeting: entity ray first (≤ 20 blocks), else nearest to gaze point within 3 blocks.
 * </remarks>
 */
public final class ItemDisableGlitch {

    /// <summary>Max targeting range.</summary>
    public static final double RANGE = 20.0;
    /// <summary>Nearest search radius around gaze point.</summary>
    public static final double NEAREST_RADIUS = 3.0;
    /// <summary>Disable duration.</summary>
    public static final Duration DURATION = Duration.ofSeconds(30);

    private ItemDisableGlitch() {}

    /**
     * <summary>Execute the sword-disable on a targeted player.</summary>
     * <param name="caster">Caster.</param>
     * <returns><c>false</c> if Diffuser active (no consumption); else <c>true</c>.</returns>
     */
    public static boolean execute(Player caster) {
        Objects.requireNonNull(caster, "caster");

        // Entity ray
        RaycastHit entHit = DreamRaycast.raycastEntities(
                caster, RANGE, 0.35,
                (Entity e) -> e instanceof Player p
                        && !p.getUniqueId().equals(caster.getUniqueId())
                        && p.getGameMode() != GameMode.SPECTATOR,
                DreamRaycast.simpleParticle(Particle.CRIT)
        );
        Player target = (entHit != null && entHit.entity() instanceof Player p) ? p : null;

        // Fallback nearest to gaze point
        if (target == null) {
            Location focus = gazePoint(caster);
            target = nearestPlayerTo(caster, focus, NEAREST_RADIUS);
        }

        if (target == null) {
            caster.sendMessage(Component.text("No player in sight.", NamedTextColor.YELLOW));
            return false;
        }

        GlitchSMPPlugin.GetItemDisableGuard().disable(target.getUniqueId(), DURATION);

        // FX + feedback
        target.getWorld().spawnParticle(Particle.ASH, target.getLocation().add(0, 1.0, 0),
                40, 0.6, 0.6, 0.6, 0.01);
        target.getWorld().playSound(target.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.8f, 0.7f);

        caster.sendMessage(Component.text("Disabled " + target.getName() + "’s swords for " + DURATION.toSeconds() + "s.", NamedTextColor.GRAY));
        target.sendMessage(Component.text("Your swords feel… useless.", NamedTextColor.RED));
        return true;
    }

    // ---- helpers ----
    private static Location gazePoint(Player caster) {
        RaycastHit hit = DreamRaycast.raycast(
                caster, RANGE, 0.0, e -> true, Set.of(), false,
                DreamRaycast.simpleParticle(Particle.END_ROD)
        );
        if (hit != null && hit.hitPosition() != null) return hit.hitPosition();

        var eye = caster.getEyeLocation();
        Vector dir = eye.getDirection().normalize();
        return eye.clone().add(dir.multiply(RANGE));
    }

    private static Player nearestPlayerTo(Player caster, Location point, double radius) {
        World w = point.getWorld();
        if (w == null) return null;
        double r2 = radius * radius;

        return w.getPlayers().stream()
                .filter(p -> !p.getUniqueId().equals(caster.getUniqueId()))
                .filter(p -> p.getGameMode() != GameMode.SPECTATOR)
                .filter(p -> p.getWorld() == w)
                .filter(p -> p.getLocation().distanceSquared(point) <= r2)
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(point)))
                .orElse(null);
    }
}