package com.balsdon.androidallyplugin.adb.parameters

@Suppress("MagicNumber")
enum class ColorCorrectionType(val adbValue: Int) {
    OFF(-1),
    GREYSCALE(0),
    TRITANOMALY(13),
    PROTANOMALY(11),
    DEUTERANOMALY(12),
}