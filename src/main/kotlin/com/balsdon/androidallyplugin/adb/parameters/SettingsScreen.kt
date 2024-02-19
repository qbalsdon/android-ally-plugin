package com.balsdon.androidallyplugin.adb.parameters

enum class SettingsScreen(val reference: String, val internalAndroidReference: String) {
    SETTINGS("panel.debug.label.screen.settings", "android.settings.SETTINGS"),
    DEVELOPER("panel.debug.label.screen.developer", "com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS"),
    ACCESSIBILITY("panel.debug.label.screen.accessibility", "android.settings.ACCESSIBILITY_SETTINGS"),
    LOCALE("panel.debug.label.screen.locale", "android.settings.LOCALE_SETTINGS"),
    COLORS("panel.debug.label.screen.colors", "com.android.settings.ACCESSIBILITY_COLOR_SPACE_SETTINGS"),
    DISPLAY("panel.debug.label.screen.display", "android.settings.DISPLAY_SETTINGS"),
    DARK("panel.debug.label.screen.dark", "android.settings.DARK_THEME_SETTINGS"),
    BLUETOOTH("panel.debug.label.screen.bluetooth", "android.settings.BLUETOOTH_SETTINGS"),
    WIFI("panel.debug.label.screen.wifi", "android.settings.WIFI_SETTINGS"),
    NETWORK("panel.debug.label.screen.network", "android.settings.AIRPLANE_MODE_SETTINGS");
}