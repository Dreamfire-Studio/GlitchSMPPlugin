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

import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * <summary>
 * Dupe Glitch: duplicates the player's main-hand item up to a max stack of 2, limited to
 * Totem of Undying and all potion types (normal, splash, lingering).
 * </summary>
 * <remarks>
 * If the Diffuser guard is active, activation is blocked and this method returns <c>false</c>
 * so the upstream caller does not consume the token.
 * </remarks>
 */
public final class DupeGlitch {

    /// <summary>Materials eligible for duplication.</summary>
    private static final Set<Material> ALLOWED = Set.of(
            Material.TOTEM_OF_UNDYING,
            Material.POTION,
            Material.SPLASH_POTION,
            Material.LINGERING_POTION
    );

    private DupeGlitch() { }

    /**
     * <summary>Executes the Dupe Glitch on the player’s main-hand item.</summary>
     * <param name="player">Activating player.</param>
     * <returns>
     * <c>false</c> if blocked by Diffuser; otherwise <c>true</c> (handled).
     * </returns>
     */
    public static boolean execute(Player player) {
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand == null || hand.getType() == Material.AIR) {
            player.sendMessage(Component.text("Hold a totem or potion to duplicate.", NamedTextColor.RED));
            return false;
        }

        if (!ALLOWED.contains(hand.getType())) {
            player.sendMessage(Component.text("This glitch only duplicates Totems and Potions (max stack 2).", NamedTextColor.YELLOW));
            return false;
        }

        if (hand.getAmount() >= 2) {
            player.sendMessage(Component.text("Already at maximum stack (2).", NamedTextColor.GRAY));
            return false;
        }

        hand.setAmount(2);

        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1.0, 0),
                20, 0.3, 0.3, 0.3, 0.01);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.9f, 1.2f);
        player.sendMessage(Component.text("Duplicated to a stack of 2.", NamedTextColor.GREEN));

        return true;
    }
}