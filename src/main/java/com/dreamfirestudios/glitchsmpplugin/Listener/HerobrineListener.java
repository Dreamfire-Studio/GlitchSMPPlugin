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
package com.dreamfirestudios.glitchsmpplugin.Listener;

import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

/**
 * <summary>
 * Herobrine listener: when an affected player is damaged, sometimes calls lightning on nearby entities.
 * </summary>
 */
@PulseAutoRegister
public final class HerobrineListener implements Listener {

    private static final double LIGHTNING_RADIUS = 6.0;
    private static final double PROC_CHANCE = 0.35;

    /** <summary>Handles lightning proc when damaged.</summary> */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamaged(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player victim)) return;
        if (!GlitchSMPPlugin.GetHerobrineGuard().isActive(victim.getUniqueId())) return;

        if (Math.random() > PROC_CHANCE) return;

        Location center = victim.getLocation();
        List<Entity> nearby = victim.getWorld().getNearbyEntities(center, LIGHTNING_RADIUS, LIGHTNING_RADIUS, LIGHTNING_RADIUS)
                .stream()
                .filter(ent -> ent instanceof LivingEntity)
                .filter(ent -> !ent.getUniqueId().equals(victim.getUniqueId()))
                .toList();

        for (Entity ent : nearby) {
            victim.getWorld().strikeLightning(ent.getLocation());
        }
    }
}