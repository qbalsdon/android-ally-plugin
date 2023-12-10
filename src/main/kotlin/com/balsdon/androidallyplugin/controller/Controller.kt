package com.balsdon.androidallyplugin.controller

import com.balsdon.androidallyplugin.model.AndroidDevice
import io.reactivex.rxjava3.core.Observable

interface Controller {
    fun <NPT: NotificationPayload> showNotification(notificationPayload: NPT)

    fun subscribeToDeviceChange(): Observable<List<AndroidDevice>>
}