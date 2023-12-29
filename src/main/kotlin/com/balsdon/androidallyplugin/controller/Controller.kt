package com.balsdon.androidallyplugin.controller

import com.balsdon.androidallyplugin.model.AndroidDevice
import com.balsdon.androidallyplugin.model.NotificationPayload
import io.reactivex.rxjava3.core.Observable

interface Controller {
    fun <NPT: NotificationPayload> showNotification(notificationPayload: NPT)

    val connectedDevicesNotifier: Observable<Set<AndroidDevice>>

    var selectedDeviceSerialList: MutableSet<String>

    fun showInstallTB4DSuccessNotification()
    fun showInstallTB4DErrorNotification()
}