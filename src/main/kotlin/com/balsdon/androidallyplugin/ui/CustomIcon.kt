package com.balsdon.androidallyplugin.ui

import com.intellij.openapi.util.IconLoader
import com.intellij.util.ui.UIUtil
import javax.swing.Icon

enum class CustomIcon(private val fileReference: String) {
    PHONE("phone"),
    EMULATOR("emulator"),
    A11Y_OPEN_MENU("open_menu"),
    A11Y_EXPAND_MORE("expand_more"),
    A11Y_CONTROLS("controls"),
    A11Y_MORE_MENU("more_option"),
    A11Y_SWIPE_LEFT("swipe_left"),
    A11Y_SWIPE_RIGHT("swipe_right"),
    A11Y_TAP("tap"),
    A11Y_TAP_LONG("tap_long"),
    TOGGLE_TALKBACK("accessibility"),
    TOGGLE_FONT_SCALE("font_scale"),
    TOGGLE_ANIMATIONS("animation"),
    TOGGLE_DARK_MODE("dark_mode"),
    COLOR_MODE("colour"),
    LAUNCH_SCREEN("launch"),
    SIZE_OPTIONS("size"),
    HIGH_PRIORITY("priority_high"),
    INFO("info"),
    TOGGLE_VOICE("voice"),
    TOGGLE_TOAST("toast"),
    DEVICE_BACK("back"),
    DEVICE_HOME("home"),
    DEVICE_POWER("power"),
    SCROLL_TO_TOP("scroll_to_top"),
    SCROLL_TO_BOTTOM("scroll_to_bottom"),
    SCROLL_UP("scroll_up"),
    SCROLL_DOWN("scroll_down"),
    TALKBACK_TUTORIAL("tutorial")
    ;

    fun create(): Icon =
        IconLoader.getIcon(
            "$imageFolder$fileReference${if (UIUtil.isUnderDarcula()) darkModeId else lightModeId}$imageType",
            CustomIcon::class.java
        )


    private val imageFolder = "/icons/"
    private val imageType = ".svg"
    private val lightModeId = "_lm"
    private val darkModeId = "_dm"
}