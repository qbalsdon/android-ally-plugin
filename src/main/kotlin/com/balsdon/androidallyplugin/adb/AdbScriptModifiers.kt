package com.balsdon.androidallyplugin.adb

import com.balsdon.androidallyplugin.adb.parameters.AdbBoolean
import com.balsdon.androidallyplugin.adb.parameters.AdbParamUsingString
import com.balsdon.androidallyplugin.adb.parameters.AdbPositiveValue
import com.balsdon.androidallyplugin.adb.parameters.BooleanPrintMode
import com.balsdon.androidallyplugin.adb.parameters.ColorCorrectionAdbParam
import com.balsdon.androidallyplugin.adb.parameters.ColorCorrectionType
import com.balsdon.androidallyplugin.adb.parameters.InternalSettingType
import com.balsdon.androidallyplugin.adb.parameters.TypingTextAdbParam
import com.balsdon.androidallyplugin.adb.parameters.UnitAdbParam

fun talkBackService(activate: Boolean) = AdbScript.AccessibilityService(
    activate,
    true,
    "com.android.talkback4d",
    "com.developer.talkback.TalkBackDevService"
)

fun accessibilityScannerService(activate: Boolean) = AdbScript.AccessibilityService(
    activate,
    false,
    "com.google.android.apps.accessibility.auditor",
    "com.google.android.apps.accessibility.auditor.ScannerService"
)

fun fontScale(scale: Float) = AdbScript.InternalSetting(
    name = "font_scale",
    internalSettingType = InternalSettingType.SYSTEM,
    settingValue = AdbParamUsingString(scale)
)

fun timeToReact(value: Int) = AdbScript.InternalSetting(
    name = "accessibility_non_interactive_ui_timeout_ms",
    internalSettingType = InternalSettingType.SECURE,
    settingValue = AdbParamUsingString(value)
)

fun displayDensity(value: Int = -1) = AdbScript.Command(
    commandScript = "wm density $1",
    params = listOf(AdbPositiveValue(value, "reset"))
)

fun darkMode(isDarkMode: Boolean) = AdbScript.Command(
    commandScript = "cmd uimode night $1",
    params = listOf(AdbBoolean(isDarkMode, BooleanPrintMode.ENGLISH))
)

fun colorInversion(isEnabled: Boolean) = AdbScript.InternalSetting(
    name = "accessibility_display_inversion_enabled",
    internalSettingType = InternalSettingType.SECURE,
    settingValue = AdbBoolean(isEnabled, BooleanPrintMode.BINARY)
)

fun showTouches(isEnabled: Boolean) = AdbScript.InternalSetting(
    name = "show_touches",
    internalSettingType = InternalSettingType.SYSTEM,
    settingValue = AdbBoolean(isEnabled, BooleanPrintMode.BINARY)
)

fun highTextContrast(isEnabled: Boolean) = AdbScript.InternalSetting(
    name = "high_text_contrast_enabled",
    internalSettingType = InternalSettingType.SYSTEM,
    settingValue = AdbBoolean(isEnabled, BooleanPrintMode.BINARY)
)

private fun colorCorrectionEnabled(isEnabled: Boolean) = AdbScript.InternalSetting(
    name = "accessibility_display_daltonizer_enabled",
    internalSettingType = InternalSettingType.SECURE,
    settingValue = AdbBoolean(isEnabled, BooleanPrintMode.BINARY)
)

// Needs to be done in conjunction with enabling
private fun colorCorrectionSetting(correctionType: ColorCorrectionType) = AdbScript.InternalSetting(
    name = "accessibility_display_daltonizer",
    internalSettingType = InternalSettingType.SECURE,
    settingValue = ColorCorrectionAdbParam(correctionType)
)

fun colorCorrection(correctionType: ColorCorrectionType) = AdbScript.CombinationCommand(
    commandList = if (correctionType == ColorCorrectionType.OFF) {
        listOf(colorCorrectionEnabled(false))
    } else {
        listOf(
            colorCorrectionEnabled(true),
            colorCorrectionSetting(correctionType)
        )
    }
)

fun powerOff() = AdbScript.Command<UnitAdbParam>(
    commandScript = "reboot -p"
)

fun reboot() = AdbScript.Command<UnitAdbParam>(
    commandScript = "reboot"
)

fun typeText(text: String) = AdbScript.Command(
    commandScript = "input text $1",
    params = listOf(TypingTextAdbParam(text))
)

fun layoutBounds(enabled: Boolean) = AdbScript.Command(
    commandScript = "setprop debug.layout $1; service call activity 1599295570 > /dev/null 2>&1",
    params = listOf(AdbBoolean(enabled, BooleanPrintMode.BINARY))
)

fun animations(enabled: Boolean) = AdbScript.Command(
    commandScript = "cmd settings put global animator_duration_scale $1; cmd settings put global transition_animation_scale $1; cmd settings put global window_animation_scale $1",
    params = listOf(AdbBoolean(enabled, BooleanPrintMode.FLOAT))
)

fun captions(enabled: Boolean) = AdbScript.InternalSetting(
    name = "accessibility_captioning_enabled",
    internalSettingType = InternalSettingType.SECURE,
    settingValue = AdbBoolean(enabled, BooleanPrintMode.BINARY)
)

fun audioDescription(enabled: Boolean) = AdbScript.CombinationCommand(
    commandList = listOf(
        AdbScript.InternalSetting(
            name = "enabled_accessibility_audio_description_by_default",
            internalSettingType = InternalSettingType.SECURE,
            settingValue = AdbBoolean(enabled, BooleanPrintMode.BINARY)
        ),
        AdbScript.InternalSetting(
            name = "accessibility_audio_descriptions_enabled",
            internalSettingType = InternalSettingType.SECURE,
            settingValue = AdbBoolean(enabled, BooleanPrintMode.BINARY)
        )
    )
)

fun settings(internalSettingType: InternalSettingType) = if (internalSettingType == InternalSettingType.ALL) {
    AdbScript.Command<UnitAdbParam>(
        "settings list system | sed 's/^/system./';settings list secure | sed 's/^/secure./';settings list global | sed 's/^/global./';",
    )
} else {
    AdbScript.Command(
        "settings list $1",
        listOf(AdbParamUsingString(internalSettingType.name, true))
    )
}