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
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

/**
 * <summary>
 * Dream Glitch loot booster listener.
 * </summary>
 * <remarks>
 * <ul>
 *   <li>Boosts mob drop stack sizes and XP for players under Dream effect.</li>
 *   <li>Occasionally re-rolls an extra drop.</li>
 * </ul>
 * </remarks>
 */
@PulseAutoRegister
public final class DreamLootListener implements Listener {
    private static final double BASE_MULTIPLIER = 1.25;
    private static final double LOOTING_BONUS_MULT = 0.10;
    private static final int    DREAM_BONUS_LEVEL = 1;
    private static final double EXTRA_REROLL_CHANCE = 0.25;
    private static final double XP_MULTIPLIER = 1.15;
    private static final Random RNG = new Random();

    /**
     * <summary>
     * Enhances drops and XP when the killer is under Dream glitch.
     * </summary>
     * <param name="e">Death event for a mob.</param>
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(EntityDeathEvent e) {
        LivingEntity mob = e.getEntity();
        Player killer = mob.getKiller();
        if (killer == null) return;
        if (!GlitchSMPPlugin.GetDreamGuard().isActive(killer.getUniqueId())) return;

        double multiplier = BASE_MULTIPLIER + LOOTING_BONUS_MULT * DREAM_BONUS_LEVEL;

        List<ItemStack> drops = e.getDrops();
        for (ItemStack stack : drops) {
            if (stack == null || stack.getType() == Material.AIR) continue;
            int old = stack.getAmount();
            int max = stack.getMaxStackSize();
            int boosted = (int) Math.round(old * multiplier);
            if (boosted < 1) boosted = 1;
            if (boosted > max) boosted = max;
            stack.setAmount(boosted);
        }

        if (!drops.isEmpty() && RNG.nextDouble() < EXTRA_REROLL_CHANCE) {
            ItemStack pick = drops.get(RNG.nextInt(drops.size()));
            if (pick != null && pick.getType() != Material.AIR) {
                drops.add(pick.clone());
            }
        }

        int oldXp = e.getDroppedExp();
        int newXp = (int) Math.round(oldXp * XP_MULTIPLIER);
        e.setDroppedExp(newXp);
    }
}