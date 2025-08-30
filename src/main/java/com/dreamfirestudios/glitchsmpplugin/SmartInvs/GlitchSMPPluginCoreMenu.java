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
package com.dreamfirestudios.glitchsmpplugin.SmartInvs;

import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.ClickableItem;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.SmartInventory;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryContents;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryProvider;
import com.dreamfirestudios.glitchsmpplugin.API.GlitchSMPPluginAPI;
import com.dreamfirestudios.glitchsmpplugin.Core.EventBus;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginInventoryItems;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginPermissions;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginConfig;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginInventoryItemsConfig;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginPermissionsConfigs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public final class GlitchSMPPluginCoreMenu implements InventoryProvider {
    private static final String INV_ID = "GlitchSMPPlugin_CoreMenu";
    private static final int ROWS = 1, COLS = 9, ROW = 0;
    private static final int COL_SYSTEM = 2, COL_RELOAD = 4, COL_RESET = 6;

    private final SmartInventory smartInventory;
    private final EventBus bus = new EventBus(GlitchSMPPlugin.GetGlitchSMPPlugin());
    private static final AtomicInteger GLOBAL_GEN = new AtomicInteger(0);
    private int myGen = 0;

    public GlitchSMPPluginCoreMenu(final Player... players) {
        this.smartInventory = SmartInventory.builder()
                .id(INV_ID)
                .provider(this)
                .size(ROWS, COLS)
                .title(ChatColor.RED + "GlitchSMP Admin")
                .build();
        for (final Player p : players) this.smartInventory.open(p);
    }

    @Override
    public CompletableFuture<Void> init(final Player player, final InventoryContents contents) {
        final CompletableFuture<Void> done = new CompletableFuture<>();
        final int gen = GLOBAL_GEN.incrementAndGet();
        this.myGen = gen;

        GlitchSMPPluginInventoryItemsConfig.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(), GlitchSMPPluginInventoryItemsConfig.class, itemsCfg -> GlitchSMPPluginConfig.ReturnStaticAsync(
                        GlitchSMPPlugin.GetGlitchSMPPlugin(),
                        GlitchSMPPluginConfig.class,
                        coreCfg -> {
                            final ItemStack blank  = cloneOrNull(itemsCfg.GetValue(GlitchSMPPluginInventoryItems.BlankTile));
                            final ItemStack reload = cloneOrNull(itemsCfg.GetValue(GlitchSMPPluginInventoryItems.ReloadConfigs));
                            final ItemStack reset  = cloneOrNull(itemsCfg.GetValue(GlitchSMPPluginInventoryItems.ResetConfigs));
                            final ItemStack system = withSystemLore(cloneOrNull(itemsCfg.GetValue(GlitchSMPPluginInventoryItems.SystemEnabled)), coreCfg.systemEnabled);

                            bus.runMain(() -> {
                                if (gen != myGen) { done.complete(null); return; }
                                contents.fillRow(ROW, ClickableItem.of(blank, e -> onBlank(player, e)));
                                contents.set(ROW, COL_SYSTEM, ClickableItem.of(system, e -> onToggleSystem(player, e)));
                                contents.set(ROW, COL_RELOAD, ClickableItem.of(reload, e -> onReload(player, e)));
                                contents.set(ROW, COL_RESET,  ClickableItem.of(reset,  e -> onReset(player, e)));
                                done.complete(null);
                            });
                        }
                )
        );

        return done;
    }

    private void onBlank(final Player player, final InventoryClickEvent e) { e.setCancelled(false); }

    private void onToggleSystem(final Player player, final InventoryClickEvent e) {
        e.setCancelled(true);
        GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(),
                GlitchSMPPluginPermissionsConfigs.class, cfg -> {
                    if (!cfg.DoesPlayerHavePermission(GlitchSMPPluginPermissions.EnableSystem, player, true, DreamMessageSettings.all())) return;
                    GlitchSMPPluginAPI.GlitchSMPPluginEnableSystem(x -> {});
                    bus.runMain(player::closeInventory);
                    bus.runLater(() -> this.smartInventory.open(player), 1L);
                });
    }

    private void onReload(final Player player, final InventoryClickEvent e) {
        e.setCancelled(true);
        GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(),
                GlitchSMPPluginPermissionsConfigs.class, cfg -> {
                    if (!cfg.DoesPlayerHavePermission(GlitchSMPPluginPermissions.ReloadConfigs, player, true, DreamMessageSettings.all())) return;
                    GlitchSMPPluginAPI.GlitchSMPPluginReloadConfigs(DreamMessageSettings.all());
                    bus.runMain(player::closeInventory);
                    bus.runLater(() -> this.smartInventory.open(player), 1L);
                });
    }

    private void onReset(final Player player, final InventoryClickEvent e) {
        e.setCancelled(true);
        GlitchSMPPluginPermissionsConfigs.ReturnStaticAsync(GlitchSMPPlugin.GetGlitchSMPPlugin(),
                GlitchSMPPluginPermissionsConfigs.class, cfg -> {
                    if (!cfg.DoesPlayerHavePermission(GlitchSMPPluginPermissions.ResetConfigs, player, true, DreamMessageSettings.all())) return;
                    GlitchSMPPluginAPI.GlitchSMPPluginResetConfigs(DreamMessageSettings.all());
                    bus.runMain(player::closeInventory);
                    bus.runLater(() -> this.smartInventory.open(player), 1L);
                });
    }

    private static ItemStack cloneOrNull(final ItemStack s) { return s == null ? null : s.clone(); }

    private static ItemStack withSystemLore(final ItemStack base, final boolean enabled) {
        if (base == null) return null;
        final var meta = base.getItemMeta();
        final var status = enabled
                ? Component.text("ENABLED", NamedTextColor.GREEN)
                : Component.text("DISABLED", NamedTextColor.RED);
        meta.lore(List.of(Component.text("Currently: ", NamedTextColor.WHITE).append(status)));
        base.setItemMeta(meta);
        return base;
    }
}