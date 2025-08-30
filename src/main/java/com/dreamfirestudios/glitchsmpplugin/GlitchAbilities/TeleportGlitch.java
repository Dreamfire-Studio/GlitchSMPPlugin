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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Set;

/**
 * <summary>
 * Simple “blink” teleport: moves the player to the position they’re looking at,
 * up to a fixed maximum range. If no block is hit, teleports to the point in mid-air.
 * </summary>
 * <remarks>
 * <ul>
 *   <li><b>No</b> ground checks or safety resolution are performed.</li>
 *   <li>Uses {@link DreamRaycast} to fetch the hit (block or none) and a small particle trail.</li>
 *   <li>Retains the player’s yaw/pitch at the destination.</li>
 * </ul>
 * </remarks>
 * <example>
 * ```java
 * TeleportGlitch.execute(player); // blink to gaze point (<= 20 blocks)
 * ```
 * </example>
 */
public final class TeleportGlitch {
    private static final double TELEPORT_RANGE = 20.0;
    private static final Particle TRAIL_PARTICLE = Particle.PORTAL;

    private TeleportGlitch() {}

    /**
     * <summary>
     * Teleports the player to the block (or air point) they are looking at within {@link #TELEPORT_RANGE}.
     * </summary>
     * <param name="player">Player to teleport.</param>
     * <returns>Always {@code true} to indicate the ability was handled.</returns>
     * <remarks>
     * No safety checks are performed (you may end up inside blocks or in mid-air).
     * </remarks>
     */
    public static boolean execute(Player player) {
        Objects.requireNonNull(player, "player");
        World world = player.getWorld();

        RaycastHit hit = DreamRaycast.raycast(
                player,
                TELEPORT_RANGE,
                0.0,
                e -> true,
                Set.of(), false,
                DreamRaycast.simpleParticle(TRAIL_PARTICLE)
        );

        Location eye = player.getEyeLocation();
        Vector dir   = eye.getDirection().normalize();

        Location dest;
        if (hit != null && hit.hitPosition() != null) {
            dest = cloneWithLook(hit.hitPosition(), player);
        } else {
            dest = cloneWithLook(eye.clone().add(dir.multiply(TELEPORT_RANGE)), player);
        }

        fxPre(world, player.getLocation());
        boolean ok = player.teleport(dest);
        fxPost(world, player.getLocation());

        if (!ok) {
            player.sendMessage(Component.text("Teleport failed.", NamedTextColor.RED));
        } else {
            world.playSound(dest, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        }
        return true;
    }

    private static Location cloneWithLook(Location base, Player player) {
        return new Location(
                base.getWorld(),
                base.getX(),
                base.getY(),
                base.getZ(),
                player.getLocation().getYaw(),
                player.getLocation().getPitch()
        );
    }

    private static void fxPre(World world, Location loc) {
        world.spawnParticle(TRAIL_PARTICLE, loc.clone().add(0, 1.0, 0), 24, 0.4, 0.4, 0.4, 0.01);
        world.playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.5f, 1.6f);
    }

    private static void fxPost(World world, Location loc) {
        world.spawnParticle(TRAIL_PARTICLE, loc.clone().add(0, 1.0, 0), 30, 0.5, 0.5, 0.5, 0.01);
        world.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 1.1f);
    }
}