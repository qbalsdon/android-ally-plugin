package com.balsdon.androidallyplugin.controller

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener
import com.android.ddmlib.IDevice
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.balsdon.androidallyplugin.utils.log
import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


class AndroidStudioPluginController(private val project: Project) : Controller {
    private val androidDeviceSource = PublishSubject.create<List<AndroidDevice>>()
    override var selectedDeviceSerial: String = ""
        set(value) {
            if (field != value) {
                log("Trigger device change from [$field] to [$value]")
                field = value
            }
        }

    private val groupId = "AndroidAlly"

    override fun <NPT : NotificationPayload> showNotification(notificationPayload: NPT) {
        require(notificationPayload is AndroidStudioPluginNotificationPayload)

        NotificationGroupManager
            .getInstance()
            .getNotificationGroup(groupId)
            .createNotification(
                notificationPayload.title,
                notificationPayload.message,
                notificationPayload.notificationType
            )
            .addActions(notificationPayload.actions)
            .notify(project)
    }

    init {
        AndroidDebugBridge.addDeviceChangeListener(object : IDeviceChangeListener {
            private fun updateDeviceList() {
                val devices = AndroidDebugBridge
                        .getBridge()
                        .devices
                        .map { AndroidDevice(it.serialNumber) }
                log("updateDeviceList: ${devices.map { it.serial }}")
                androidDeviceSource.onNext(devices)
            }

            override fun deviceConnected(device: IDevice?) = updateDeviceList()

            override fun deviceDisconnected(device: IDevice?) = updateDeviceList()

            // TODO: Notify a new subject
            override fun deviceChanged(device: IDevice?, p1: Int) = Unit

        })
    }

    override val deviceChangeNotifier: Observable<List<AndroidDevice>> = androidDeviceSource
}