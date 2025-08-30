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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <summary>
 * Installs a ProtocolLib listener that masks outgoing equipment updates for players
 * who are currently invisible via InvisibilityGuard. Auto-registered by {@code @PulseAutoRegister}.
 * </summary>
 */
@PulseAutoRegister
public final class InvisibilityEquipmentMaskerBootstrap implements Listener {

    private final ProtocolManager pm = ProtocolLibrary.getProtocolManager();
    private final PacketAdapter adapter;

    public InvisibilityEquipmentMaskerBootstrap() {
        this.adapter = new PacketAdapter(
                GlitchSMPPlugin.GetGlitchSMPPlugin(),
                ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_EQUIPMENT
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                try {
                    final PacketContainer packet = event.getPacket();

                    // Which entity is this equipment update about?
                    final Entity tracked = packet.getEntityModifier(event).readSafely(0);
                    if (!(tracked instanceof Player target)) {
                        return; // mobs also send this packet; we only care about players
                    }

                    final UUID targetId = target.getUniqueId();
                    if (!GlitchSMPPlugin.GetInvisibilityGuard().isActive(targetId)) {
                        return; // target not under invisibility -> leave packet intact
                    }

                    // Optional: if you want the *owner* to still see their own gear, skip when viewer == target.
                    // if (event.getPlayer().getUniqueId().equals(targetId)) return;

                    // Read the typed equipment list and replace items with AIR for visible slots
                    final List<Pair<EnumWrappers.ItemSlot, ItemStack>> slots =
                            packet.getSlotStackPairLists().readSafely(0);
                    if (slots == null || slots.isEmpty()) return;

                    final ItemStack air = new ItemStack(Material.AIR);
                    final List<Pair<EnumWrappers.ItemSlot, ItemStack>> masked = new ArrayList<>(slots.size());

                    for (Pair<EnumWrappers.ItemSlot, ItemStack> pair : slots) {
                        final EnumWrappers.ItemSlot slot = pair.getFirst();

                        final boolean hide = switch (slot) {
                            case MAINHAND, OFFHAND, HEAD, CHEST, LEGS, FEET -> true;
                            default -> false;
                        };

                        masked.add(hide ? new Pair<>(slot, air) : pair);
                    }

                    // Write back the masked list
                    packet.getSlotStackPairLists().write(0, masked);
                } catch (Throwable t) {
                    // Keep the server stable; log once if you like, but avoid spamming the console.
                    // Bukkit.getLogger().log(Level.FINEST, "Masker error", t);
                }
            }
        };

        pm.addPacketListener(adapter);
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        if (e.getPlugin() == GlitchSMPPlugin.GetGlitchSMPPlugin()) {
            pm.removePacketListener(adapter);
        }
    }
}