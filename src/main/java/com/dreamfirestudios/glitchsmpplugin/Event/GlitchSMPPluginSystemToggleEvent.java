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

import lombok.Getter;

/**
 * /// <summary>
 * Event fired whenever the plugin system enable state is toggled.
 * /// </summary>
 * /// <remarks>
 * Provides both old and new state values for listener inspection.
 * /// </remarks>
 * /// <example>
 * <code>
 * @EventHandler
 * public void onToggle(GlitchSMPPluginSystemToggleEvent e) {
 *     plugin.getLogger().info("System toggled: " + e.isOldState() + " → " + e.isNewState());
 * }
 * </code>
 * /// </example>
 */
@Getter
public final class GlitchSMPPluginSystemToggleEvent extends GlitchSMPPluginEvent {
    /** Previous enabled state. */
    private final boolean oldState;

    /** New enabled state. */
    private final boolean newState;

    /**
     * /// <summary>Create a new toggle event snapshot.</summary>
     * /// <param name="oldState">Previous enabled state.</param>
     * /// <param name="newState">New enabled state.</param>
     */
    public GlitchSMPPluginSystemToggleEvent(final boolean oldState, final boolean newState) {
        super();
        this.oldState = oldState;
        this.newState = newState;
    }
}