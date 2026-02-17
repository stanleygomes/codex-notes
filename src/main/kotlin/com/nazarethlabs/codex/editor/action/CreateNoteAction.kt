package com.nazarethlabs.codex.editor.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.helper.NotificationHelper
import com.nazarethlabs.codex.service.note.CreateNoteService

class CreateNoteAction : AnAction() {
    private val createNoteService = CreateNoteService()

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        ApplicationManager.getApplication().runWriteAction {
            try {
                val virtualFile = createNoteService.create(project)
                if (virtualFile != null) {
                    NotificationHelper.showSuccess(
                        project,
                        MyBundle.message("action.create.note.success.title"),
                        MyBundle.message("action.create.note.success.message", virtualFile.name),
                    )
                } else {
                    NotificationHelper.showError(
                        project,
                        MyBundle.message("action.create.note.error.title"),
                        "Failed to create note",
                    )
                }
            } catch (ex: Exception) {
                NotificationHelper.showError(
                    project,
                    MyBundle.message("action.create.note.error.title"),
                    ex.message ?: "Unknown error",
                )
            }
        }
    }
}
