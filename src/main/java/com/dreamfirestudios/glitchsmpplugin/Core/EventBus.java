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
package com.dreamfirestudios.glitchsmpplugin.Core;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * <summary>
 * Utility wrapper for firing Bukkit events and scheduling tasks on the main thread.
 * </summary>
 * <remarks>
 * <ul>
 *   <li>Provides a simple API to safely call Bukkit events on the main thread.</li>
 *   <li>Delegates scheduling to {@link Scheduler}.</li>
 *   <li>Ensures null safety for tasks and events.</li>
 * </ul>
 * </remarks>
 */
public final class EventBus {
    private final Scheduler scheduler;

    /**
     * <summary>
     * Creates a new {@link EventBus} bound to the given plugin.
     * </summary>
     * <param name="plugin">The plugin that owns this event bus.</param>
     */
    public EventBus(final Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        this.scheduler = new Scheduler(plugin);
    }

    /**
     * <summary>
     * Fires a Bukkit {@link Event}, ensuring it runs on the main thread.
     * </summary>
     * <param name="event">The event to fire.</param>
     */
    public void fire(final Event event) {
        Objects.requireNonNull(event, "event");
        if (Bukkit.isPrimaryThread()) Bukkit.getPluginManager().callEvent(event);
        else scheduler.main(() -> Bukkit.getPluginManager().callEvent(event));
    }

    /**
     * <summary>
     * Runs a task immediately on the main thread.
     * </summary>
     * <param name="task">The task to execute.</param>
     */
    public void runMain(final Runnable task) {
        scheduler.main(Objects.requireNonNull(task, "task"));
    }

    /**
     * <summary>
     * Runs a task on the main thread after a delay in ticks.
     * </summary>
     * <param name="task">The task to execute.</param>
     * <param name="delayTicks">Delay in server ticks (20 ticks = 1 second).</param>
     */
    public void runLater(final Runnable task, final long delayTicks) {
        scheduler.mainLater(Objects.requireNonNull(task, "task"), delayTicks);
    }
}