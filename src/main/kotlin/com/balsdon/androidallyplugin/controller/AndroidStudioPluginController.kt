package com.balsdon.androidallyplugin.controller

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener
import com.android.ddmlib.IDevice
import com.android.ddmlib.IDevice.PROP_BUILD_API_LEVEL
import com.android.ddmlib.IDevice.PROP_BUILD_VERSION
import com.balsdon.androidallyplugin.extension.friendlyName
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.balsdon.androidallyplugin.model.AndroidStudioPluginNotificationPayload
import com.balsdon.androidallyplugin.model.NotificationPayload
import com.balsdon.androidallyplugin.utils.log
import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject


class AndroidStudioPluginController(private val project: Project) : Controller {
    private val groupId = "AndroidAlly"
    private val connectedDeviceList: MutableList<AndroidDevice.Device> = mutableListOf()
    private val androidDeviceSource = BehaviorSubject.create<Set<AndroidDevice>>()
    override var selectedDeviceSerialList: MutableSet<String> = mutableSetOf()

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

    /* The guidance is to avoid initialisations in constructors
     * @see [https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension]
     *
     * However, this is core functionality
    */
    init {
        AndroidDebugBridge.addDeviceChangeListener(object : IDeviceChangeListener {
            private fun updateDeviceList(device: IDevice, updateDevice: Boolean = true) {
                connectedDeviceList.removeIf { it.serial == device.serialNumber }
                if (updateDevice) {
                    connectedDeviceList.add(
                        AndroidDevice.Device(
                            rawDevice = device,
                            isEmulator = device.isEmulator,
                            serial = device.serialNumber,
                            apiLevel = device.getProperty(PROP_BUILD_API_LEVEL) ?: "",
                            sdkLevel = device.getProperty(PROP_BUILD_VERSION) ?: "",
                            friendlyName = device.friendlyName
                        )
                    )
                }
                androidDeviceSource.onNext(connectedDeviceList.toSet())
            }

            override fun deviceConnected(device: IDevice?) {
                log("deviceConnected: [${device?.serialNumber}]")
                if (device != null) {
                    updateDeviceList(device)
                }
            }

            override fun deviceDisconnected(device: IDevice?) {
                log("deviceDisconnected: [${device?.serialNumber}]")
                if (device != null) {
                    updateDeviceList(device, false)
                }
            }

            // TODO: Notify a new subject
            override fun deviceChanged(device: IDevice?, p1: Int) {
                log("deviceChanged: [${device?.serialNumber}] [$p1]")
                if (device != null) {
                    updateDeviceList(device)
                }
            }

        })
    }

    override val connectedDevicesNotifier: Observable<Set<AndroidDevice>> = androidDeviceSource
}