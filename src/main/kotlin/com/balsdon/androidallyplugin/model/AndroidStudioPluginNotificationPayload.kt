package com.balsdon.androidallyplugin.model

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction

data class AndroidStudioPluginNotificationPayload(
    val title: String,
    val message: String,
    val notificationType: NotificationType = NotificationType.INFORMATION,
    val actions: Collection<AnAction> = emptyList()
    ): NotificationPayload