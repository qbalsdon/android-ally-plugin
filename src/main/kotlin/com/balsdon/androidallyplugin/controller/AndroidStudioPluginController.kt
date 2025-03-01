package com.balsdon.androidallyplugin.controller

import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener
import com.android.ddmlib.IDevice
import com.balsdon.androidallyplugin.TB4DInstallHelpWebPage
import com.balsdon.androidallyplugin.TB4DWebPage
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.balsdon.androidallyplugin.model.AndroidStudioPluginNotificationPayload
import com.balsdon.androidallyplugin.model.NotificationPayload
import com.balsdon.androidallyplugin.utils.log
import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AndroidStudioPluginController(
    private val project: Project,
    private val adbProvider: AdbProvider = AndroidDebugBridgeProvider()
) : Controller {
    private val groupId = "AndroidAlly"
    private val connectedDeviceList: MutableList<AndroidDevice> = mutableListOf()
    private val androidDeviceSource = BehaviorSubject.create<Set<AndroidDevice>>()
    override var selectedDeviceSerialList: MutableSet<String> = mutableSetOf()

    override val connectedDevicesNotifier: Observable<Set<AndroidDevice>> = androidDeviceSource

    /* The guidance is to avoid initialisations in constructors
         * @see [https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension]
         *
         * However, this is core functionality
        */
    init {
        adbProvider.addDeviceChangeListener(object : IDeviceChangeListener {
            private fun updateDeviceList(device: IDevice, updateDevice: Boolean = true) {
                connectedDeviceList.removeIf { it.serial == device.serialNumber }
                if (updateDevice) {
                    connectedDeviceList.add(AndroidDevice(device))
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

            // Do not update the device list - it creates a backlog of requests on the device
            override fun deviceChanged(device: IDevice?, changeMask: Int) {
                if (device != null) {
                    val sb = StringBuilder()
                    if (changeMask and IDevice.CHANGE_BUILD_INFO != 0) {
                        sb.append("CHANGE_BUILD_INFO")
                    }
                    if (changeMask and IDevice.CHANGE_STATE != 0) {
                        if (sb.isNotBlank()) sb.append(" ")
                        sb.append("CHANGE_STATE")
                    }
                    if (changeMask and IDevice.CHANGE_CLIENT_LIST != 0) {
                        if (sb.isNotBlank()) sb.append(" ")
                        sb.append("CHANGE_CLIENT_LIST")
                    }

                    if (changeMask and IDevice.CHANGE_PROFILEABLE_CLIENT_LIST != 0) {
                        if (sb.isNotBlank()) sb.append(" ")
                        sb.append("CHANGE_PROFILEABLE_CLIENT_LIST")
                    }
                    log("deviceChanged: [${device.serialNumber}] [$changeMask] [$sb]")
                }
            }
        })
    }

    override fun showNotification(
        title: String,
        message: String,
        type: NotificationType,
        actions: Collection<NotificationAction>
    ) {
        showNotification(
            AndroidStudioPluginNotificationPayload(
                title,
                message,
                type,
                actions
            )
        )
    }

    override fun showInstallTB4DSuccessNotification(device: AndroidDevice) {
        showNotification(AndroidStudioPluginNotificationPayload(
            localize("tb4d.install.success.title"),
            localize("tb4d.install.success.message", device.friendlyName),
            actions = listOf(
                object : NotificationAction(localize("tb4d.install.success.action")) {
                    override fun actionPerformed(event: AnActionEvent, notification: Notification) {
                        BrowserUtil.browse(TB4DWebPage)
                    }
                }
            )
        ))
    }

    override fun showInstallTB4DErrorNotification(device: AndroidDevice) {
        showNotification(AndroidStudioPluginNotificationPayload(
            localize("tb4d.install.error.title"),
            localize("tb4d.install.error.message"),
            actions = listOf(
                object : NotificationAction(localize("tb4d.install.error.action")) {
                    override fun actionPerformed(event: AnActionEvent, notification: Notification) {
                        BrowserUtil.browse(TB4DInstallHelpWebPage)
                    }
                }
            )
        ))
    }

    private fun showNoSelectedDevicesNotification() {
        showNotification(AndroidStudioPluginNotificationPayload(
            localize("plugin.notification.no_devices.title"),
            localize("plugin.notification.no_devices.message"),
            notificationType = NotificationType.ERROR,
            actions = emptyList()
        ))
    }

    override fun runOnAllValidSelectedDevices(fn: (AndroidDevice) -> Unit) {
        if (connectedDeviceList.size == 1) {
            fn(connectedDeviceList[0])
        } else {
            connectedDeviceList
                .filter { it.serial in selectedDeviceSerialList }
                .map { fn(it) }
                .ifEmpty { showNoSelectedDevicesNotification() }
        }
    }

    override fun refreshAdb() = adbProvider.refreshAdb()

    private fun <NPT : NotificationPayload> showNotification(notificationPayload: NPT) {
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
}