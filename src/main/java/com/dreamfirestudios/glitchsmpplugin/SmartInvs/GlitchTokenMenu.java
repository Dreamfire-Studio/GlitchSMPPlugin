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

import com.dreamfirestudios.dreamcore.DreamItems.DreamItemStacks;
import com.dreamfirestudios.dreamcore.DreamItems.DreamItemStacksAPI;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.ClickableItem;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.SmartInventory;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryContents;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryProvider;
import com.dreamfirestudios.glitchsmpplugin.Core.EventBus;
import com.dreamfirestudios.glitchsmpplugin.DreamPersistentData.GlitchLoadoutPdcStore;
import com.dreamfirestudios.glitchsmpplugin.Enum.GlitchKey;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import com.dreamfirestudios.glitchsmpplugin.PulseConfig.GlitchSMPPluginInventoryItemsConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * <summary>
 * SmartInvs-backed Glitch Token Menu (1×9) that persists to Player PDC via {@link GlitchLoadoutPdcStore}.
 * </summary>
 * <remarks>
 * <list type="bullet">
 *   <item>Active token slots live at columns 3,4,5 (store indices 0..2).</item>
 *   <item>Slots accept only Glitch Tokens registered under <c>glitch_token:&lt;key&gt;</c>.</item>
 *   <item>Right-click clears a slot; left-click supports pick-up, place, and swap flows.</item>
 * </list>
 * </remarks>
 */
public final class GlitchTokenMenu implements InventoryProvider {

    /// <summary>Inventory id for SmartInvs session grouping.</summary>
    private static final String INV_ID = "GlitchSMPPlugin_GlitchTokenMenu";
    /// <summary>Row count for the menu.</summary>
    private static final int ROWS = 1, COLS = 9, ROW = 0;
    /// <summary>GUI columns that represent token slots (mapped to store indices 0..2).</summary>
    private static final int[] TOKEN_COLS = {3, 4, 5};

    private final SmartInventory smartInventory;
    private final EventBus bus = new EventBus(GlitchSMPPlugin.GetGlitchSMPPlugin());
    private final GlitchLoadoutPdcStore store = new GlitchLoadoutPdcStore();

    /**
     * <summary>
     * Constructs a new menu and opens it for all provided players.
     * </summary>
     * <param name="players">Players to open the inventory for.</param>
     */
    public GlitchTokenMenu(Player... players) {
        this.smartInventory = SmartInventory.builder()
                .id(INV_ID)
                .provider(this)
                .size(ROWS, COLS)
                .title(ChatColor.RED + "Glitch Token Menu")
                .build();
        for (Player p : players) this.smartInventory.open(p);
    }

    /**
     * <summary>
     * Initial render from PDC, populating background and the three token slots.
     * </summary>
     * <param name="player">Viewer/player.</param>
     * <param name="contents">SmartInvs contents handle.</param>
     * <returns>Async completion for initialization work.</returns>
     */
    @Override
    public CompletableFuture<Void> init(Player player, InventoryContents contents) {
        CompletableFuture<Void> done = new CompletableFuture<>();
        GlitchSMPPluginInventoryItemsConfig.ReturnStaticAsync(
                GlitchSMPPlugin.GetGlitchSMPPlugin(),
                GlitchSMPPluginInventoryItemsConfig.class,
                itemsCfg -> bus.runMain(() -> {
                    // Fill background
                    ItemStack blank = cloneOrNull(itemsCfg.GetValue(com.dreamfirestudios.glitchsmpplugin.Enum.GlitchSMPPluginInventoryItems.BlankTile));
                    contents.fillRow(ROW, ClickableItem.of(blank, e -> e.setCancelled(true)));

                    // Render the three active slots from PDC
                    for (int i = 0; i < TOKEN_COLS.length; i++) {
                        int col = TOKEN_COLS[i];
                        Optional<GlitchKey> saved = store.getSlot(player, i);
                        ItemStack icon = saved
                                .map(GlitchTokenMenu::buildTokenFromKey)
                                .orElse(makeTokenPlaceholder(false));
                        contents.set(ROW, col, ClickableItem.of(icon, tokenHandler(player, contents, i, col)));
                    }
                    done.complete(null);
                })
        );
        return done;
    }

    /**
     * <summary>
     * Creates the click handler for a token slot; supports clear/pick/place/swap flows.
     * </summary>
     * <param name="player">Owner of the menu.</param>
     * <param name="contents">Inventory contents.</param>
     * <param name="slotIndex">Store index (0..2).</param>
     * <param name="col">GUI column (3, 4, or 5).</param>
     * <returns>Consumer that handles the inventory click.</returns>
     */
    private Consumer<InventoryClickEvent> tokenHandler(
            Player player, InventoryContents contents, int slotIndex, int col) {

        return e -> {
            e.setCancelled(true); // GUI is a view-only menu

            final ItemStack cursor  = e.getCursor();
            final ItemStack current = e.getCurrentItem();

            // Read current slot & cursor keys up-front
            final java.util.Optional<GlitchKey> slotKeyOpt = store.getSlot(player, slotIndex);
            final GlitchKey cursorKey = readKeyFromItem(cursor).orElse(null);

            // --- Right-click anywhere on the slot → clear it ---
            if (e.isRightClick()) {
                if (slotKeyOpt.isPresent()) {
                    store.setSlot(player, slotIndex, null);
                    contents.set(ROW, col,
                            ClickableItem.of(makeTokenPlaceholder(false),
                                    tokenHandler(player, contents, slotIndex, col)));
                    // no cursor changes on clear
                }
                return;
            }

            // --- Left-click flows ---

            // 1) Pick up: empty cursor + slot has token
            if (cursorKey == null && (cursor == null || cursor.getType().isAir()) && slotKeyOpt.isPresent()) {
                GlitchKey slotKey = slotKeyOpt.get();
                player.setItemOnCursor(buildTokenFromKey(slotKey));   // use player API (works even when event cancelled)
                store.setSlot(player, slotIndex, null);
                contents.set(ROW, col,
                        ClickableItem.of(makeTokenPlaceholder(false),
                                tokenHandler(player, contents, slotIndex, col)));
                return;
            }

            // 2) Place: cursor has token + slot empty
            if (cursorKey != null && slotKeyOpt.isEmpty()) {
                store.setSlot(player, slotIndex, cursorKey);
                contents.set(ROW, col,
                        ClickableItem.of(buildTokenFromKey(cursorKey),
                                tokenHandler(player, contents, slotIndex, col)));
                player.setItemOnCursor(null); // consume cursor item into the slot
                return;
            }

            // 3) Swap: cursor has token + slot has token
            if (cursorKey != null && slotKeyOpt.isPresent()) {
                GlitchKey slotKey = slotKeyOpt.get();
                store.setSlot(player, slotIndex, cursorKey);
                contents.set(ROW, col,
                        ClickableItem.of(buildTokenFromKey(cursorKey),
                                tokenHandler(player, contents, slotIndex, col)));
                player.setItemOnCursor(buildTokenFromKey(slotKey)); // put previous slot token on cursor
                return;
            }

            // 4) Empty cursor + empty slot → do nothing
            // 5) Non-token cursor → reject (optional message)
            if (!isEmpty(cursor) && cursorKey == null) {
                player.sendMessage(Component.text("Only Glitch Tokens can be placed here.", NamedTextColor.RED));
            }
        };
    }

    // ---------------------- Helpers ----------------------

    /**
     * <summary>
     * Builds a token ItemStack from a <see cref="GlitchKey"/> using the DreamItemStacks registry.
     * </summary>
     * <param name="key">Glitch key.</param>
     * <returns>Registered token stack or a placeholder if missing.</returns>
     */
    private static ItemStack buildTokenFromKey(GlitchKey key) {
        // IDs are registered by your token classes as "glitch_token:<lowercase-name>"
        String id = "glitch_token:" + key.name().toLowerCase();
        ItemStack built = DreamItemStacksAPI.GetItemStackBYID(GlitchSMPPlugin.GetGlitchSMPPlugin(), id);
        return built != null ? built : makeTokenPlaceholder(true);
    }

    /**
     * <summary>
     * Attempts to parse a <see cref="GlitchKey"/> from a stack's DreamItemStacks ID.
     * </summary>
     * <param name="stack">Candidate stack.</param>
     * <returns>Optional glitch key if the stack is a known token.</returns>
     */
    private static Optional<GlitchKey> readKeyFromItem(ItemStack stack) {
        if (isEmpty(stack)) return Optional.empty();
        return DreamItemStacks.readId(GlitchSMPPlugin.GetGlitchSMPPlugin(), stack).flatMap(id -> {
            if (!id.startsWith("glitch_token:")) return Optional.empty();
            String tail = id.substring("glitch_token:".length());
            try { return Optional.of(GlitchKey.valueOf(tail.toUpperCase())); }
            catch (IllegalArgumentException ignored) { return Optional.empty(); }
        });
    }

    /**
     * <summary>Returns whether the provided stack is a Glitch Token.</summary>
     * <param name="stack">Stack to inspect.</param>
     * <returns><c>true</c> if it resolves to a known token id.</returns>
     */
    private static boolean isGlitchToken(ItemStack stack) {
        return readKeyFromItem(stack).isPresent();
    }

    /**
     * <summary>Null/air/zero-amount check.</summary>
     * <param name="s">Stack.</param>
     * <returns><c>true</c> if empty.</returns>
     */
    private static boolean isEmpty(ItemStack s) {
        return s == null || s.getType().isAir() || s.getAmount() <= 0;
    }

    /**
     * <summary>
     * Creates a placeholder tile for empty/loading token slots.
     * </summary>
     * <param name="loading"><c>true</c> to use a “loading saved token…” message.</param>
     * <returns>Purple/magenta pane with contextual lore.</returns>
     */
    private static ItemStack makeTokenPlaceholder(boolean loading) {
        Material mat = loading ? Material.PURPLE_STAINED_GLASS_PANE : Material.MAGENTA_STAINED_GLASS_PANE;
        ItemStack pane = new ItemStack(mat);
        var meta = pane.getItemMeta();
        meta.displayName(Component.text("Glitch Token Slot", NamedTextColor.GOLD));
        meta.lore(java.util.List.of(
                Component.text(loading ? "Loading saved token..." : "Place a Glitch Token here.", NamedTextColor.YELLOW)
        ));
        pane.setItemMeta(meta);
        return pane;
    }

    /**
     * <summary>Defensive clone helper.</summary>
     * <param name="s">Source item.</param>
     * <returns>Clone or <c>null</c> if source is null.</returns>
     */
    private static ItemStack cloneOrNull(ItemStack s) { return s == null ? null : s.clone(); }
}