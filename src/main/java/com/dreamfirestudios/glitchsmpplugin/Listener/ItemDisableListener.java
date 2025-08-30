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
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * <summary>
 * Item Disable listener: cancels sword interactions and damage while active.
 * </summary>
 */
@PulseAutoRegister
public final class ItemDisableListener implements Listener {

    private static boolean isSword(ItemStack stack) {
        if (stack == null) return false;
        Material m = stack.getType();
        return m == Material.WOODEN_SWORD
                || m == Material.STONE_SWORD
                || m == Material.IRON_SWORD
                || m == Material.GOLDEN_SWORD
                || m == Material.DIAMOND_SWORD
                || m == Material.NETHERITE_SWORD;
    }

    /** <summary>Cancels sword swing and use actions while disabled.</summary> */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!GlitchSMPPlugin.GetItemDisableGuard().isActive(p.getUniqueId())) return;

        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK
                || e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isSword(p.getInventory().getItemInMainHand())) {
                e.setCancelled(true);
                p.sendActionBar(Component.text("Your sword is disabled.", NamedTextColor.RED));
            }
        }
    }

    /** <summary>Cancels sword damage while disabled.</summary> */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;
        if (!GlitchSMPPlugin.GetItemDisableGuard().isActive(p.getUniqueId())) return;

        if (isSword(p.getInventory().getItemInMainHand())) {
            e.setCancelled(true);
            p.sendActionBar(Component.text("Your sword is disabled.", NamedTextColor.RED));
        }
    }
}