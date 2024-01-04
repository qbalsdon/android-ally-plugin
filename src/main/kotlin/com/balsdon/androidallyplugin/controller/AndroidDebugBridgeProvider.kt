package com.balsdon.androidallyplugin.controller

import com.android.ddmlib.AndroidDebugBridge

class AndroidDebugBridgeProvider: AdbProvider {
    override fun addDeviceChangeListener(listener: AndroidDebugBridge.IDeviceChangeListener) =
        AndroidDebugBridge.addDeviceChangeListener(listener)
}