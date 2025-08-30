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
package com.dreamfirestudios.glitchsmpplugin.Enum;

import com.dreamfirestudios.glitchsmpplugin.GlitchSMPPlugin;

import java.util.Arrays;

/// <summary>
/// Permission node formats and corresponding default error templates.
/// </summary>
/// <remarks>
/// <list type="bullet">
///   <item><description>Permission format shape: <c>"%s.%s.Action"</c> → <c>Plugin.Level.Action</c></description></item>
///   <item><description>Error templates expect plugin name as the first <c>%s</c> placeholder.</description></item>
/// </list>
/// </remarks>
public enum GlitchSMPPluginPermissions {
    /// <summary>Reload configs action.</summary>
    ReloadConfigs("%s.%s.ReloadConfigs", "#7fff36[%s]: You do not have the permission to use this command!"),

    /// <summary>Reset configs action.</summary>
    ResetConfigs("%s.%s.ResetConfigs", "#7fff36[%s]: You do not have the permission to use this command!"),

    /// <summary>Enable/disable system action.</summary>
    EnableSystem("%s.%s.EnableSystem", "#7fff36[%s]: You do not have the permission to use this command!"),

    /// <summary>Serialize item action.</summary>
    SerializeItem("%s.%s.SerializeItem", "#7fff36[%s]: You do not have the permission to use this command!"),

    /// <summary>Admin console-only actions.</summary>
    AdminConsole("%s.%s.AdminConsole", "#7fff36[%s]: You do not have the permission to use this command!"),

    /// <summary>Permission to fetch a glitch item by ID.</summary>
    GetGlitchItem("%s.%s.GetGlitchItem", "#7fff36[%s]: You do not have the permission to use this command!"),

    /// <summary>Permission to give a glitch item to players.</summary>
    GiveGlitchItem("%s.%s.GiveGlitchItem", "#7fff36[%s]: You do not have the permission to use this command!"),

    /// <summary>Permission to open/use the Glitch Tokens menu.</summary>
    GlitchTokens("%s.%s.GlitchTokens", "#7fff36[%s]: You do not have the permission to use this command!");

    private final String permission;
    private final String error;

    /// <summary>
    /// Constructs a permission enum value with its format and error template.
    /// </summary>
    /// <param name="permission">Format string like <c>"%s.%s.Action"</c>.</param>
    /// <param name="error">Error template, expects plugin name as first placeholder.</param>
    GlitchSMPPluginPermissions(final String permission, final String error) {
        this.permission = permission;
        this.error = error;
    }

    /// <summary>
    /// Build a concrete permission string for a specific level.
    /// </summary>
    /// <param name="permissionLevel">Desired logical level segment.</param>
    /// <returns>Concrete permission node (e.g., <c>GlitchSMPPlugin.Admin.ReloadConfigs</c>).</returns>
    public String GetPermission(final GlitchSMPPluginPermissionLevel permissionLevel) {
        return String.format(permission, GlitchSMPPlugin.class.getSimpleName(), permissionLevel.GetPermissionLevel());
    }

    /// <summary>
    /// Build a formatted error message for missing permission.
    /// </summary>
    /// <param name="args">Optional additional args (currently ignored by the template).</param>
    /// <returns>Formatted error line.</returns>
    /// <remarks>
    /// Current templates only consume the plugin name; extra arguments are ignored by <c>String.format</c>.
    /// </remarks>
    public String GetError(final Object... args) {
        return String.format(error, GlitchSMPPlugin.class.getSimpleName(), Arrays.toString(args));
    }

    /// <summary>
    /// Return the raw permission format string.
    /// </summary>
    /// <returns>Format with placeholders (e.g., <c>"%s.%s.Action"</c>).</returns>
    public String getPermissionFormat() {
        return permission;
    }
}