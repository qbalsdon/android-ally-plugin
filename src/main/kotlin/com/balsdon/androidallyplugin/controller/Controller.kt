package com.balsdon.androidallyplugin.controller

import com.balsdon.androidallyplugin.model.AndroidDevice
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import io.reactivex.rxjava3.core.Observable

interface Controller {
    val connectedDevicesNotifier: Observable<Set<AndroidDevice>>
    var selectedDeviceSerialList: MutableSet<String>

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