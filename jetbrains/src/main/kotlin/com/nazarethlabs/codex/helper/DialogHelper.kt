package com.nazarethlabs.codex.helper

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

object DialogHelper {
    fun showYesNoDialog(
        project: Project,
        message: String,
        title: String,
    ): Int = Messages.showYesNoDialog(project, message, title, Messages.getQuestionIcon())

    fun showInputDialog(
        project: Project,
        message: String,
        title: String,
        initialValue: String?,
    ): String? = Messages.showInputDialog(project, message, title, Messages.getQuestionIcon(), initialValue, null)

    fun showWarningDialog(
        project: Project,
        message: String,
        title: String,
    ) {
        Messages.showWarningDialog(project, message, title)
    }

    fun showErrorDialog(
        project: Project,
        message: String,
        title: String,
    ) {
        Messages.showErrorDialog(project, message, title)
    }
}
