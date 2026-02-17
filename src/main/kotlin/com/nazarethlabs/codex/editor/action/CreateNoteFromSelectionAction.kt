package com.nazarethlabs.codex.editor.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.helper.NotificationHelper
import com.nazarethlabs.codex.service.note.CreateNoteService
import com.nazarethlabs.codex.service.settings.NotesSettingsService

class CreateNoteFromSelectionAction : AnAction() {
    private val createNoteService = CreateNoteService()
    private val notesSettingsService = NotesSettingsService()

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val hasSelection = editor?.selectionModel?.hasSelection() ?: false
        e.presentation.isEnabledAndVisible = hasSelection
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText ?: return

        ApplicationManager.getApplication().runWriteAction {
            try {
                val title = generateTitleFromText(selectedText)
                val extension = notesSettingsService.getDefaultFileExtension()
                val virtualFile = createNoteService.createWithContent(project, title, extension, selectedText)

                if (virtualFile != null) {
                    NotificationHelper.showSuccess(
                        project,
                        MyBundle.message("action.create.note.from.selection.success.title"),
                        MyBundle.message("action.create.note.from.selection.success.message", virtualFile.name),
                    )
                } else {
                    NotificationHelper.showError(
                        project,
                        MyBundle.message("action.create.note.from.selection.error.title"),
                        MyBundle.message("action.create.note.from.selection.error.message"),
                    )
                }
            } catch (ex: Exception) {
                NotificationHelper.showError(
                    project,
                    MyBundle.message("action.create.note.from.selection.error.title"),
                    ex.message ?: MyBundle.message("action.create.note.from.selection.error.unknown"),
                )
            }
        }
    }

    private fun generateTitleFromText(text: String): String {
        val firstLine = text.lines().firstOrNull() ?: MyBundle.message("action.create.note.from.selection.default.title")
        val maxLength = 50
        return if (firstLine.length > maxLength) {
            firstLine.substring(0, maxLength).trim()
        } else {
            firstLine.trim()
        }
    }
}
