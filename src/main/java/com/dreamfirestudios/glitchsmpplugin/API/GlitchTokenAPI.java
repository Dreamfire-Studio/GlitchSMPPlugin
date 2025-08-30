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
package com.dreamfirestudios.glitchsmpplugin.API;

import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamItems.DreamItemStacksAPI;
import com.dreamfirestudios.glitchsmpplugin.Core.Scheduler;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginPermissions;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginPermissionsConfigs;
import com.dreamfirestudios.glitchsmpplugin.SmartInvs.GlitchTokenMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * <summary>
 * Public API for managing and interacting with Glitch Tokens.
 * </summary>
 * <remarks>
 * <ul>
 *   <li>Provides utility methods for retrieving, giving, and opening menus for Glitch Tokens.</li>
 *   <li>Integrates with DreamItemStacks for token definitions and SmartInvs for menus.</li>
 *   <li>Respects permission checks defined in {@link GlitchSMPPluginPermissionsConfigs}.</li>
 * </ul>
 * </remarks>
 */
public final class GlitchTokenAPI {

    /**
     * <summary>
     * Retrieves a Glitch Token {@link ItemStack} for the given {@link GlitchKey}.
     * </summary>
     * <param name="glitchKey">The key identifying which glitch token to fetch.</param>
     * <returns>The corresponding {@link ItemStack} representation of the glitch token.</returns>
     */
    public static ItemStack GetGlitchToken(GlitchKey glitchKey) {
        return DreamItemStacksAPI.GetItemStackBYID(
                GlitchSMPPlugin.GetGlitchSMPPlugin(),
                "glitch_token:" + glitchKey.name().toLowerCase()
        );
    }

    /**
     * <summary>
     * Gives a specified number of Glitch Tokens to a player.
     * </summary>
     * <param name="glitchKey">The key identifying which glitch token to give.</param>
     * <param name="player">The target player receiving the tokens.</param>
     * <param name="amount">The number of tokens to give.</param>
     * <remarks>
     * Tokens are added one by one to ensure proper item stack handling.
     * </remarks>
     */
    public static void GiveGlitchToken(GlitchKey glitchKey, Player player, int amount) {
        for (int i = 0; i < amount; i++) {
            var itemStack = GlitchTokenAPI.GetGlitchToken(glitchKey);
            player.getInventory().addItem(itemStack);
        }
    }

    /**
     * <summary>
     * Opens the Glitch Token menu for a player if they have the correct permission.
     * </summary>
     * <param name="player">The player for whom the menu should be opened.</param>
     * <remarks>
     * <ul>
     *   <li>Performs an async permission check via {@link GlitchSMPPluginPermissionsConfigs}.</li>
     *   <li>If the player lacks permission, no menu is opened and a message is sent.</li>
     *   <li>Menu opening is scheduled on the main thread.</li>
     * </ul>
     * </remarks>
     */
    public static void OpenGlitchTokenMenu(Player player) {
        GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(
                GlitchSMPPlugin.GetGlitchSMPPlugin(),
                GlitchSMPPluginPermissionsConfigs.class,
                config -> {
                    if (!config.DoesPlayerHavePermission(
                            GlitchSMPPluginPermissions.GlitchTokens,
                            player,
                            true,
                            DreamMessageSettings.all())) return;

                    scheduler().main(() -> new GlitchTokenMenu(player));
                }
        );
    }

    /**
     * <summary>
     * Provides a new instance of {@link Scheduler} bound to the GlitchSMP plugin.
     * </summary>
     * <returns>A scheduler instance for main/async task execution.</returns>
     */
    private static Scheduler scheduler() {
        return new Scheduler(GlitchSMPPlugin.GetGlitchSMPPlugin());
    }
}