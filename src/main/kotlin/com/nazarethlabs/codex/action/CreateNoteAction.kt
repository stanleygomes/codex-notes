package com.nazarethlabs.codex.action

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.helper.NoteNameHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository
import com.nazarethlabs.codex.service.settings.NotesSettingsService

class CreateNoteAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        ApplicationManager.getApplication().runWriteAction {
            try {
                val note = createNote()
                openNoteInEditor(project, note.filePath)
                showSuccessNotification(project, note.title)
            } catch (ex: Exception) {
                showErrorNotification(project, ex.message ?: "Unknown error")
            }
        }
    }

    private fun createNote(): com.nazarethlabs.codex.dto.Note {
        val title = generateTitle()
        val extension = getExtension()
        val fileName = "$title$extension"
        val filePath = createPhysicalFile(fileName)

        return NoteStorageRepository.getInstance().addNote(title, filePath)
    }

    private fun generateTitle(): String {
        val notes = NoteStorageRepository.getInstance().getAllNotes()
        return NoteNameHelper.generateUntitledName(notes)
    }

    private fun getExtension(): String = NotesSettingsService().getDefaultFileExtension()

    private fun createPhysicalFile(fileName: String): String {
        val notesDirectory = NotesSettingsService().getNotesDirectory()
        val file = FileHelper.createFile(notesDirectory, fileName)
        return file.absolutePath
    }

    private fun openNoteInEditor(
        project: Project,
        filePath: String,
    ) {
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(filePath) ?: return
        FileEditorManager.getInstance(project).openFile(virtualFile, true)
    }

    private fun showSuccessNotification(
        project: Project,
        title: String,
    ) {
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("Codex Notes")
            .createNotification(
                MyBundle.message("action.create.note.success.title"),
                MyBundle.message("action.create.note.success.message", title),
                NotificationType.INFORMATION,
            ).notify(project)
    }

    private fun showErrorNotification(
        project: Project,
        message: String,
    ) {
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("Codex Notes")
            .createNotification(
                MyBundle.message("action.create.note.error.title"),
                message,
                NotificationType.ERROR,
            ).notify(project)
    }
}
