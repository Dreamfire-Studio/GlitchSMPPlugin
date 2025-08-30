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
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

/**
 * <summary>
 * Crash Glitch enforcement listener.
 * </summary>
 * <remarks>
 * <list type="bullet">
 *   <item>Kicks players trying to join if they are flagged as crashed/locked out by the {@code CrashGuard}.</item>
 *   <item>Works during the async pre-login phase, before full player join.</item>
 * </list>
 * </remarks>
 */
@PulseAutoRegister
public final class CrashGlitchListener implements Listener {

    /**
     * <summary>
     * Cancels login if the player UUID is marked as crashed in {@link GlitchSMPPlugin#GetCrashGuard()}.
     * </summary>
     * <param name="e">Pre-login event.</param>
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent e) {
        UUID id = e.getUniqueId();
        if (!GlitchSMPPlugin.GetCrashGuard().isBlocked(id)) return;
        e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("CONNECTION THROTTLED"));
    }
}