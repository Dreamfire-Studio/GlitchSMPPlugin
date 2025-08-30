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
package com.dreamfirestudios.glitchsmpplugin.Util;

import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import com.dreamfirestudios.glitchsmpplugin.Event.GlitchUseEvent;
import com.dreamfirestudios.glitchsmpplugin.GlitchAbilities.*;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * <summary>
 * Central dispatcher for executing glitch abilities from a <see cref="GlitchKey"/>.
 * </summary>
 * <remarks>
 * <list type="bullet">
 *   <item>Fires {@link GlitchUseEvent} (cancellable) prior to execution.</item>
 *   <item>Respects global Diffuser guard (except for the Diffuser key itself).</item>
 * </list>
 * </remarks>
 */
public final class GlitchAbilityExecutor {

    private GlitchAbilityExecutor() {}

    /**
     * <summary>
     * Executes the glitch ability corresponding to the provided key.
     * </summary>
     * <param name="plugin">Owning plugin (for context).</param>
     * <param name="player">Activating player.</param>
     * <param name="key">Glitch key.</param>
     * <returns><c>true</c> if the ability handled; <c>false</c> if blocked/cancelled/not implemented.</returns>
     */
    public static boolean handle(JavaPlugin plugin, Player player, GlitchKey key) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(key, "key");

        // Fire cancellable event
        GlitchUseEvent event = new GlitchUseEvent(player, key);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            player.sendMessage(Component.text("You cannot use that glitch right now.", NamedTextColor.RED));
            return false;
        }

        // Diffuser guard
        if (key != GlitchKey.DIFFUSER && GlitchSMPPlugin.GetDiffuserGuard().isActive()) {
            long secs = Math.max(0, GlitchSMPPlugin.GetDiffuserGuard().remaining().toSeconds());
            player.sendMessage(Component.text("Glitches are diffused for " + secs + "s.", NamedTextColor.YELLOW));
            return false;
        }

        // Dispatch
        return switch (key) {
            case TELEPORT -> TeleportGlitch.execute(player);
            case CRASH -> CrashGlitch.execute(player);
            case DIFFUSER -> DiffuserGlitch.execute(player);
            case DREAM -> DreamGlitch.execute(player);
            case DUPE -> DupeGlitch.execute(player);
            case EFFECT_AMPLIFY -> EffectAmplifyGlitch.execute(player);
            case FREEZE -> FreezeGlitch.execute(player);
            case GLIDE -> GlideGlitch.execute(player);
            case HEROBRINE -> HerobrineGlitch.execute(player);
            case IMMUNITY -> ImmunityGlitch.execute(player);
            case INVENTORY -> InventoryGlitch.execute(player);
            case INVISIBILITY -> InvisibilityGlitch.execute(player);
            case ITEM_DISABLE -> ItemDisableGlitch.execute(player);
            default -> false;
        };
    }
}