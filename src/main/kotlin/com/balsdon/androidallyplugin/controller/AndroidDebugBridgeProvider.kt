package com.balsdon.androidallyplugin.controller

import com.android.ddmlib.AndroidDebugBridge
import java.util.concurrent.TimeUnit

class AndroidDebugBridgeProvider: AdbProvider {
    @Suppress("MagicNumber")
    private val restartTimeout = 500L
    override fun addDeviceChangeListener(listener: AndroidDebugBridge.IDeviceChangeListener) =
        AndroidDebugBridge.addDeviceChangeListener(listener)

    @Suppress("SwallowedException")
    override fun refreshAdb() {
        try {
            AndroidDebugBridge.getBridge().restart(restartTimeout, TimeUnit.MILLISECONDS)
        } catch (e: IllegalStateException) {
            println("Prevented crash from restart")
        }
    }
}