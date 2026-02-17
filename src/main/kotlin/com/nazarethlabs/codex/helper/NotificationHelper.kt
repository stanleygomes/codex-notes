package com.nazarethlabs.codex.helper

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType.ERROR
import com.intellij.notification.NotificationType.INFORMATION
import com.intellij.openapi.project.Project

object NotificationHelper {
    private const val NOTIFICATION_GROUP_ID = "Codex Notes"

    fun showSuccess(
        project: Project,
        title: String,
        message: String,
    ) {
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup(NOTIFICATION_GROUP_ID)
            .createNotification(title, message, INFORMATION)
            .notify(project)
    }

    fun showError(
        project: Project,
        title: String,
        message: String,
    ) {
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup(NOTIFICATION_GROUP_ID)
            .createNotification(title, message, ERROR)
            .notify(project)
    }
}
