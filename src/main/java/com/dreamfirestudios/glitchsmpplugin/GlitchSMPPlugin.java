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
package com.dreamfirestudios.glitchsmpplugin;

import com.dreamfirestudios.dreamcommand.DreamCommand;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.DreamClassAPI;
import com.dreamfirestudios.dreamcore.DreamVariable.DreamEnumVariableTest;
import com.dreamfirestudios.glitchsmpplugin.API.GlitchSMPPluginAPI;
import com.dreamfirestudios.glitchsmpplugin.Enum.*;
import com.dreamfirestudios.glitchsmpplugin.Util.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <summary>Main plugin entry.</summary>
 * <remarks>
 * Registers enum variable tests, loads configs, registers classes and commands.
 * </remarks>
 * <example>
 * <code>
 * // Managed by Bukkit lifecycle; no direct construction required.
 * </code>
 * </example>
 */
public class GlitchSMPPlugin extends JavaPlugin {
    private static GlitchSMPPlugin GlitchSMPPlugin;
    private static CrashGuard CrashGuard;
    private static DiffuserGuard DiffuserGuard;
    private static DreamGuard DreamGuard;
    private static FreezeGuard FreezeGuard;
    private static GlideGuard GlideGuard;
    private static HerobrineGuard HerobrineGuard;
    private static ImmunityGuard ImmunityGuard;
    private static InvisibilityGuard InvisibilityGuard;
    private static ItemDisableGuard ItemDisableGuard;

    /// <summary>Global plugin accessor.</summary>
    public static GlitchSMPPlugin GetGlitchSMPPlugin(){return GlitchSMPPlugin;}
    /// <summary>Crash guard accessor.</summary>
    public static CrashGuard GetCrashGuard(){return CrashGuard;}
    /// <summary>Diffuser guard accessor.</summary>
    public static DiffuserGuard GetDiffuserGuard(){return DiffuserGuard;}
    /// <summary>Dream guard accessor.</summary>
    public static DreamGuard GetDreamGuard(){return DreamGuard;}
    /// <summary>Freeze guard accessor.</summary>
    public static FreezeGuard GetFreezeGuard(){return FreezeGuard;}
    /// <summary>Glide guard accessor.</summary>
    public static GlideGuard GetGlideGuard(){return GlideGuard;}
    /// <summary>Herobrine guard accessor.</summary>
    public static HerobrineGuard GetHerobrineGuard(){return HerobrineGuard;}
    /// <summary>Immunity guard accessor.</summary>
    public static ImmunityGuard GetImmunityGuard(){return ImmunityGuard;}
    /// <summary>Invisibility guard accessor.</summary>
    public static InvisibilityGuard GetInvisibilityGuard(){return InvisibilityGuard;}
    /// <summary>Item Disable guard accessor.</summary>
    public static ItemDisableGuard GetItemDisableGuard(){return ItemDisableGuard;}

    /**
     * <summary>
     * Bukkit enable hook. Initializes guards, registers variable tests/classes/commands, and loads configs.
     * </summary>
     */
    @Override
    public void onEnable() {
        GlitchSMPPlugin = this;

        // Guards
        CrashGuard = new CrashGuard();
        DiffuserGuard = new DiffuserGuard();
        DreamGuard = new DreamGuard();
        FreezeGuard = new FreezeGuard();
        GlideGuard = new GlideGuard();
        HerobrineGuard = new HerobrineGuard();
        ImmunityGuard = new ImmunityGuard();
        InvisibilityGuard = new InvisibilityGuard();
        ItemDisableGuard = new ItemDisableGuard();

        // Pulse enum variable tests (exposes enums to your config/variable system)
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(GlitchSMPPluginFeatureFlagKey.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(GlitchSMPPluginInventoryItems.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(GlitchSMPPluginMessages.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(GlitchSMPPluginPermissionLevel.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(GlitchSMPPluginPermissions.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(GlitchKey.class));

        // Load configs once at startup
        GlitchSMPPluginAPI.GlitchSMPPluginReloadConfigs(DreamMessageSettings.all());

        // Auto-register classes/listeners/recipes/etc + register commands
        DreamClassAPI.RegisterClasses(this);
        DreamCommand.RegisterRaw(this);
    }
}