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
package com.dreamfirestudios.glitchsmpplugin.Event;

import com.dreamfirestudios.glitchsmpplugin.Core.EventBus;
import com.dreamfirestudios.glitchsmpplugin.Core.Log;
import com.dreamfirestudios.glitchsmpplugin.Core.Scheduler;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * /// <summary>
 * Base class for all {@code GlitchSMPPlugin} events.
 * /// </summary>
 * /// <remarks>
 * <ul>
 *   <li>Implements Bukkit {@link Event} contract and provides a shared {@link HandlerList}.</li>
 *   <li>Guards against firing events when the system is disabled.</li>
 *   <li>Ensures thread-safety: events are always dispatched on the main thread, regardless of caller.</li>
 *   <li>Optional debug logging controlled by {@code debugConfig} in {@link GlitchSMPPluginConfig}.</li>
 * </ul>
 * </remarks>
 * /// <example>
 * <code>
 * new GlitchSMPPluginConfigReloadEvent().CallEvent();
 * </code>
 * /// </example>
 */
public class GlitchSMPPluginEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    /** Logger instance (debug flag overridden dynamically per config). */
    private final Log log = Log.of(GlitchSMPPlugin.GetGlitchSMPPlugin(), false);

    /** Thread-aware event bus dispatcher. */
    private final EventBus bus = new EventBus(GlitchSMPPlugin.GetGlitchSMPPlugin());

    /** Scheduler utility for thread hopping. */
    private final Scheduler scheduler = new Scheduler(GlitchSMPPlugin.GetGlitchSMPPlugin());

    /**
     * /// <summary>
     * Constructs a plugin event, auto-detecting async state.
     * </summary>
     * /// <remarks>
     * Calls {@link Event#Event(boolean)} with <c>!Bukkit.isPrimaryThread()</c>.
     * </remarks>
     */
    public GlitchSMPPluginEvent() {
        super(!Bukkit.isPrimaryThread());
    }

    /**
     * /// <summary>Standard Bukkit handler list accessor.</summary>
     */
    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    /**
     * /// <summary>Standard Bukkit static handler list accessor.</summary>
     */
    public static HandlerList getHandlerList() { return HANDLERS; }

    /**
     * /// <summary>
     * Fire this event if and only if the system is enabled.
     * </summary>
     * /// <remarks>
     * <ul>
     *   <li>Debug log entries are written before and after dispatch (if {@code debugConfig} is true).</li>
     *   <li>Dispatch is always performed on the main thread via {@link Scheduler#main(Runnable)}.</li>
     * </ul>
     * </remarks>
     * /// <example>
     * <code>
     * new GlitchSMPPluginSystemToggleEvent(false, true).CallEvent();
     * </code>
     * /// </example>
     */
    public void CallEvent() {
        GlitchSMPPluginConfig.ReturnStaticAsync(
                GlitchSMPPlugin.GetGlitchSMPPlugin(),
                GlitchSMPPluginConfig.class,
                cfg -> {
                    if (!cfg.systemEnabled) {
                        if (cfg.debugConfig) {
                            log.debug("Events", "Skipped event (systemEnabled=false): " + getClass().getSimpleName());
                        }
                        return;
                    }

                    scheduler.main(() -> {
                        if (cfg.debugConfig) {
                            log.debug("Events", "Firing: " + getClass().getSimpleName());
                        }
                        bus.fire(this);
                    });
                }
        );
    }
}