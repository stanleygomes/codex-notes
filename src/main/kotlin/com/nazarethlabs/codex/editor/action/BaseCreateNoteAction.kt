package com.nazarethlabs.codex.editor.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
import com.nazarethlabs.codex.Constants
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.helper.NotificationHelper

abstract class BaseCreateNoteAction : AnAction() {
    protected abstract fun createNote(e: AnActionEvent): VirtualFile?

    protected abstract fun getSuccessTitleKey(): String

    protected abstract fun getSuccessMessageKey(): String

    protected abstract fun getErrorTitleKey(): String

    protected abstract fun getErrorMessageKey(): String

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        ApplicationManager.getApplication().runWriteAction {
            try {
                val virtualFile = createNote(e)
                if (virtualFile != null) {
                    NotificationHelper.showSuccess(
                        project,
                        MyBundle.message(getSuccessTitleKey()),
                        MyBundle.message(getSuccessMessageKey(), virtualFile.name),
                    )
                    ToolWindowManager.getInstance(project).getToolWindow(Constants.TOOL_WINDOW_ID)?.activate(null)
                } else {
                    NotificationHelper.showError(
                        project,
                        MyBundle.message(getErrorTitleKey()),
                        MyBundle.message(getErrorMessageKey()),
                    )
                }
            } catch (ex: Exception) {
                NotificationHelper.showError(
                    project,
                    MyBundle.message(getErrorTitleKey()),
                    ex.message ?: "Unknown error",
                )
            }
        }
    }
}
