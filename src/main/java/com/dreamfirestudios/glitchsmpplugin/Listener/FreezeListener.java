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

import com.dreamfirestudios.dreamcore.DreamEvent.DreamPlayerMoveEvent;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * <summary>
 * Enforces the Freeze Glitch using the low-frequency {@link DreamPlayerMoveEvent}.
 * </summary>
 * <remarks>
 * Cancels all player movement, interaction, and survival actions while frozen.
 * </remarks>
 */
@PulseAutoRegister
public final class FreezeListener implements Listener {

    /** <summary>Cancels movement while frozen.</summary> */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDreamMove(DreamPlayerMoveEvent e) {
        if (!GlitchSMPPlugin.GetFreezeGuard().isFrozen(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }

    /** <summary>Blocks general interaction while frozen.</summary> */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        if (GlitchSMPPlugin.GetFreezeGuard().isFrozen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    /** <summary>Blocks swapping main/off-hand while frozen.</summary> */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (GlitchSMPPlugin.GetFreezeGuard().isFrozen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    /** <summary>Blocks item drops while frozen.</summary> */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent e) {
        if (GlitchSMPPlugin.GetFreezeGuard().isFrozen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    /** <summary>Blocks consuming items while frozen.</summary> */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent e) {
        if (GlitchSMPPlugin.GetFreezeGuard().isFrozen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    /** <summary>Blocks ender-pearl teleports while frozen.</summary> */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL
                && GlitchSMPPlugin.GetFreezeGuard().isFrozen(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}