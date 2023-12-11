package com.balsdon.androidallyplugin.controller

import com.balsdon.androidallyplugin.model.AndroidDevice
import io.reactivex.rxjava3.core.Observable

interface Controller {
    fun <NPT: NotificationPayload> showNotification(notificationPayload: NPT)

    val deviceChangeNotifier: Observable<List<AndroidDevice>>

    var selectedDeviceSerial: String
}