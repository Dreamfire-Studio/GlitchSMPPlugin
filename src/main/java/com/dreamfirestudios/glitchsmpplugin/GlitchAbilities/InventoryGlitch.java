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
import com.dreamfirestudios.glitchsmpplugin.Util.InventoryScrambler;
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

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

/**
 * <summary>
 * Inventory Glitch: instantly scrambles the target player's hotbar and main inventory.
 * </summary>
 * <remarks>
 * Target selection:
 * <ol>
 *   <li>First player entity hit by a short entity ray (≤ {@link #RANGE}).</li>
 *   <li>Else nearest player to the gaze hit point within {@link #NEAREST_RADIUS} blocks.</li>
 * </ol>
 * Respects Diffuser (returns <c>false</c> to avoid consumption).
 * </remarks>
 */
public final class InventoryGlitch {

    /// <summary>Max targeting distance along gaze.</summary>
    public static final double RANGE = 20.0;
    /// <summary>Fallback nearest search radius around gaze hit.</summary>
    public static final double NEAREST_RADIUS = 3.0;

    private InventoryGlitch() {}

    /**
     * <summary>Executes the inventory scramble on a targeted player.</summary>
     * <param name="caster">Player using the glitch.</param>
     * <returns><c>false</c> if blocked by Diffuser; otherwise <c>true</c>.</returns>
     */
    public static boolean execute(Player caster) {
        Objects.requireNonNull(caster, "caster");

        // Try entity ray first
        RaycastHit entHit = DreamRaycast.raycastEntities(
                caster, RANGE, 0.35,
                (Entity e) -> e instanceof Player p
                        && !p.getUniqueId().equals(caster.getUniqueId())
                        && p.getGameMode() != GameMode.SPECTATOR,
                DreamRaycast.simpleParticle(Particle.CRIT)
        );
        Player target = (entHit != null && entHit.entity() instanceof Player p) ? p : null;

        // Else nearest to gaze point
        if (target == null) {
            Location focus = gazePoint(caster);
            target = nearestPlayerTo(caster, focus, NEAREST_RADIUS);
        }

        if (target == null) {
            caster.sendMessage(Component.text("No player in sight.", NamedTextColor.YELLOW));
            return false; // handled; nothing to do
        }

        InventoryScrambler.scrambleHotbarAndMain(target.getInventory());
        target.updateInventory();

        // FX + messages
        target.getWorld().spawnParticle(Particle.WARPED_SPORE, target.getLocation().add(0, 1.0, 0),
                40, 0.6, 0.6, 0.6, 0.01);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_STARE, 0.8f, 0.7f);

        caster.sendMessage(Component.text("Scrambled " + target.getName() + "’s inventory.", NamedTextColor.GRAY));
        target.sendMessage(Component.text("Your inventory was scrambled!", NamedTextColor.RED));
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