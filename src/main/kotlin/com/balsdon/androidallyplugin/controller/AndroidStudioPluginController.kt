package com.balsdon.androidallyplugin.controller

import com.balsdon.androidallyplugin.model.AndroidDevice
import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AndroidStudioPluginController(private val project: Project)
    : Controller {
    private val groupId = "AndroidAlly"

    override fun <NPT: NotificationPayload> showNotification(notificationPayload: NPT) {
        require(notificationPayload is AndroidStudioPluginNotificationPayload)

        NotificationGroupManager
            .getInstance()
            .getNotificationGroup(groupId)
            .createNotification(notificationPayload.title, notificationPayload.message, notificationPayload.notificationType)
            .addActions(notificationPayload.actions)
            .notify(project)
    }

    private fun connectedDevices(i: Long): Observable<List<AndroidDevice>> {
        // TODO: fetch devices
        return if (i.toInt() % 10 != 0) {
            Observable.just(
                listOf(AndroidDevice("1111"), AndroidDevice("2222"), AndroidDevice("3333"))
            )
        } else {
            Observable.just(
                listOf(AndroidDevice("$i - 1111"), AndroidDevice("$i - 2222"), AndroidDevice("$i - 3333"))
            )
        }
    }

    private val adbDeviceObserver: Observable<List<AndroidDevice>> by lazy {
        Observable
            .interval(0, 250, TimeUnit.MILLISECONDS, Schedulers.io())
            .flatMap { i -> connectedDevices(i) }
    }
    override fun subscribeToDeviceChange(): Observable<List<AndroidDevice>> = adbDeviceObserver
}