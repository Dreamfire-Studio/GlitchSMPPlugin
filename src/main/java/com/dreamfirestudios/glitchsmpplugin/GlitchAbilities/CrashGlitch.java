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
 * Crash Glitch: target player is forcibly disconnected and blocked from rejoining for a short lockout.
 * </summary>
 * <remarks>
 * <list type="number">
 *   <item><description>Primary target selection via entity raycast (≤ {@link #RANGE}).</description></item>
 *   <item><description>Fallback: nearest player to the caster's gaze point within {@link #NEAREST_RADIUS}.</description></item>
 *   <item><description>Invokes {@code CrashGuard.block(target, LOCKOUT)} before kicking.</description></item>
 * </list>
 * </remarks>
 * <example>
 * <code>
 * boolean ok = CrashGlitch.execute(player);
 * </code>
 * </example>
 */
public final class CrashGlitch {
    /// <summary>Maximum targeting range for gaze/entity ray.</summary>
    public static final double RANGE = 20.0;
    /// <summary>Duration the target is blocked from rejoining.</summary>
    public static final Duration LOCKOUT = Duration.ofSeconds(15);
    /// <summary>Radius used when falling back to nearest-to-gaze targeting.</summary>
    public static final double NEAREST_RADIUS = 3.0;

    private CrashGlitch() {}

    /**
     * <summary>Attempts to crash (disconnect) a targeted player.</summary>
     * <param name="caster">The player activating the glitch.</param>
     * <returns><c>true</c> if a target was found and kicked; otherwise <c>false</c>.</returns>
     */
    public static boolean execute(Player caster) {
        Objects.requireNonNull(caster, "caster");
        RaycastHit entityHit = DreamRaycast.raycastEntities(
                caster,
                RANGE,
                0.35,
                (Entity e) -> e instanceof Player p
                        && !p.getUniqueId().equals(caster.getUniqueId())
                        && p.getGameMode() != GameMode.SPECTATOR,
                DreamRaycast.simpleParticle(Particle.CRIT)
        );

        Player target = (entityHit != null && entityHit.entity() instanceof Player p) ? p : null;

        if (target == null) {
            Location focus = gazePoint(caster);
            target = nearestPlayerTo(caster, focus, NEAREST_RADIUS);
        }

        if (target == null) {
            caster.sendMessage(Component.text("No player in sight.", NamedTextColor.YELLOW));
            return false;
        }

        GlitchSMPPlugin.GetCrashGuard().block(target.getUniqueId(), LOCKOUT);
        target.kick(Component.text("CONNECTION THROTTLED"));
        caster.sendMessage(Component.text("Crashed " + target.getName() + " for 15s.", NamedTextColor.GRAY));
        return true;
    }

    /**
     * <summary>Determines the gaze point for the caster using a block raycast.</summary>
     * <param name="caster">The caster.</param>
     * <returns>Hit position or a point {@link #RANGE} blocks ahead.</returns>
     */
    private static Location gazePoint(Player caster) {
        RaycastHit hit = DreamRaycast.raycast(
                caster,
                RANGE,
                0.0,
                e -> true,
                Set.of(),
                false,
                DreamRaycast.simpleParticle(Particle.END_ROD)
        );

        if (hit != null && hit.hitPosition() != null) {
            return hit.hitPosition();
        }

        Location eye = caster.getEyeLocation();
        Vector dir = eye.getDirection().normalize();
        return eye.clone().add(dir.multiply(RANGE));
    }

    /**
     * <summary>Finds the nearest valid player to a point, excluding the caster.</summary>
     * <param name="caster">The caster.</param>
     * <param name="point">Center point.</param>
     * <param name="radius">Search radius.</param>
     * <returns>Nearest player or <c>null</c> if none.</returns>
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