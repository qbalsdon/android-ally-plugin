package com.balsdon.androidallyplugin.ui

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon


enum class CustomIcon(private val fileReference: String) {
    PHONE("phone"),
    WATCH("watch"),
    EMULATOR("emulator"),
    EMULATOR_WATCH("emulator_watch"),
    A11Y_OPEN_MENU("open_menu"),
    REFRESH("sync"),
    A11Y_ACTIONS("actions"),
    A11Y_SWIPE_LEFT("swipe_left"),
    A11Y_SWIPE_RIGHT("swipe_right"),
    A11Y_TAP("tap"),
    A11Y_TAP_LONG("tap_long"),
    INFO("info"),
    DEVICE_BACK("back"),
    DEVICE_HOME("home"),
    NOT_APPLICABLE("not_applicable"),
    PERCEIVABLE("perceivable"),
    OPERABLE("operable"),
    UNDERSTANDABLE("understandable"),
    ROBUST("robust"),
    LINK("link"),
    ;

    private fun createFileReference() = "$imageFolder$fileReference$imageType"
    fun create(): Icon =
        IconLoader.getIcon(
            createFileReference(),
            CustomIcon::class.java
        )

    private val imageFolder = "/icons/"
    private val imageType = ".svg"
}