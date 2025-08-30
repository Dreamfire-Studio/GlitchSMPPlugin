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
 * Freeze Glitch: immobilizes a targeted player for the duration and blocks specific interactions.
 * </summary>
 * <remarks>
 * Targeting: entity ray first (≤ {@link #RANGE}), then nearest-to-gaze within {@link #NEAREST_RADIUS}.
 * </remarks>
 */
public final class FreezeGlitch {

    /// <summary>Maximum targeting range.</summary>
    public static final double RANGE = 20.0;
    /// <summary>Nearest search radius when no entity is directly hit.</summary>
    public static final double NEAREST_RADIUS = 3.0;
    /// <summary>Freeze duration applied to the target.</summary>
    public static final Duration DURATION = Duration.ofSeconds(30);

    private FreezeGlitch() {}

    /**
     * <summary>Applies the freeze effect to a targeted player.</summary>
     * <param name="caster">Player using the glitch.</param>
     * <returns><c>true</c> if a target was frozen; otherwise <c>false</c>.</returns>
     */
    public static boolean execute(Player caster) {
        Objects.requireNonNull(caster, "caster");

        RaycastHit entHit = DreamRaycast.raycastEntities(
                caster, RANGE, 0.35,
                (Entity e) -> e instanceof Player p
                        && !p.getUniqueId().equals(caster.getUniqueId())
                        && p.getGameMode() != GameMode.SPECTATOR,
                DreamRaycast.simpleParticle(Particle.ASH)
        );
        Player target = (entHit != null && entHit.entity() instanceof Player p) ? p : null;

        if (target == null) {
            Location focus = gazePoint(caster);
            target = nearestPlayerTo(caster, focus, NEAREST_RADIUS);
        }

        if (target == null) {
            caster.sendMessage(Component.text("No player in sight.", NamedTextColor.YELLOW));
            return false;
        }

        GlitchSMPPlugin.GetFreezeGuard().freeze(target.getUniqueId(), DURATION);

        target.getWorld().spawnParticle(Particle.SNOWFLAKE, target.getLocation().add(0, 1.0, 0),
                60, 0.6, 0.6, 0.6, 0.02);
        target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_PLACE, 0.9f, 0.6f);

        long secs = Math.max(1, DURATION.toSeconds());
        target.sendMessage(Component.text("You are frozen for " + secs + "s!", NamedTextColor.AQUA));
        caster.sendMessage(Component.text("Froze " + target.getName() + " for " + secs + "s.", NamedTextColor.GRAY));
        return true;
    }

    /**
     * <summary>Raycasts to a gaze point for the caster.</summary>
     * <param name="caster">Caster.</param>
     * <returns>Hit position or forward point at {@link #RANGE}.</returns>
     */
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

    /**
     * <summary>Finds nearest valid player to a point, excluding the caster.</summary>
     * <param name="caster">Caster.</param>
     * <param name="point">Reference point.</param>
     * <param name="radius">Search radius.</param>
     * <returns>Player or <c>null</c>.</returns>
     */
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