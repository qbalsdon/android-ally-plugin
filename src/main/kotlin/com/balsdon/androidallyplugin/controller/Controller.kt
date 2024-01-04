package com.balsdon.androidallyplugin.controller

import com.balsdon.androidallyplugin.model.AndroidDevice
import io.reactivex.rxjava3.core.Observable

interface Controller {
    val connectedDevicesNotifier: Observable<Set<AndroidDevice>>
    var selectedDeviceSerialList: MutableSet<String>
    fun showInstallTB4DSuccessNotification()
    fun showInstallTB4DErrorNotification()
    fun runOnAllValidSelectedDevices(fn: (AndroidDevice) -> Unit)
}