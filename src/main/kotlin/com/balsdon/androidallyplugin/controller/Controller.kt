package com.balsdon.androidallyplugin.controller

import com.balsdon.androidallyplugin.model.AndroidDevice
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType

/**
 * Listener for connected device set changes (e.g. device connected/disconnected).
 * Same pattern as [com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener].
 */
fun interface ConnectedDevicesListener {
    fun onConnectedDevicesChanged(devices: Set<AndroidDevice>)
}

interface Controller {
    /** Current set of connected devices. */
    val connectedDevices: Set<AndroidDevice>
    var selectedDeviceSerialList: MutableSet<String>

    fun addConnectedDevicesListener(listener: ConnectedDevicesListener)
    fun removeConnectedDevicesListener(listener: ConnectedDevicesListener)

    fun showNotification(
        title: String,
        message: String,
        type: NotificationType,
        actions: Collection<NotificationAction> = emptyList()
    )

    fun showInstallTB4DSuccessNotification(device: AndroidDevice)
    fun showInstallTB4DErrorNotification(device: AndroidDevice)
    fun runOnAllValidSelectedDevices(fn: (AndroidDevice) -> Unit)

    fun refreshAdb()
}