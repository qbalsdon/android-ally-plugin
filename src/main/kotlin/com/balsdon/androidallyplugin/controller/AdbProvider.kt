package com.balsdon.androidallyplugin.controller

import com.android.ddmlib.AndroidDebugBridge

interface AdbProvider {
    fun addDeviceChangeListener(listener: AndroidDebugBridge.IDeviceChangeListener)

    fun refreshAdb()
}