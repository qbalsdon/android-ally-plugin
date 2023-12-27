package com.balsdon.androidallyplugin.model

import com.android.ddmlib.IDevice

sealed class AndroidDevice {
    data class Empty(val noop: Unit = Unit): AndroidDevice() // cannot use object classes: https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#object-vs-class
    data class Device(
        val rawDevice: IDevice,
        val isEmulator: Boolean,
        val serial: String,
        val friendlyName: String,
        val apiLevel: String,
        val sdkLevel: String
    ): AndroidDevice()
}