package com.balsdon.androidallyplugin.controller

import com.android.ddmlib.AndroidDebugBridge
import java.util.concurrent.TimeUnit

class AndroidDebugBridgeProvider: AdbProvider {
    @Suppress("MagicNumber")
    private val restartTimeout = 500L
    override fun addDeviceChangeListener(listener: AndroidDebugBridge.IDeviceChangeListener) =
        AndroidDebugBridge.addDeviceChangeListener(listener)

    override fun refreshAdb() {
        AndroidDebugBridge.getBridge().restart(restartTimeout, TimeUnit.MILLISECONDS)
    }
}