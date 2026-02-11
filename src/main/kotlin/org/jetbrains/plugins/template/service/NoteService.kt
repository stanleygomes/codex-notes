package org.jetbrains.plugins.template.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.helper.MessageHelper
import org.jetbrains.plugins.template.helper.NoteNameHelper
import org.jetbrains.plugins.template.repository.NoteStorageRepository
import java.io.File

class NoteService {

    fun create(project: Project): VirtualFile? {
        val notes = NoteStorageRepository
            .getInstance(project)
            .getAllNotes()
        val title = NoteNameHelper
            .generateUntitledName(notes)

        val fileName = "$title.md"
        val tempDir = System
            .getProperty("java.io.tmpdir")
        val file = File(tempDir, fileName)
        file.writeText("")

        val virtualFile = LocalFileSystem
            .getInstance()
            .findFileByIoFile(file)

        if (virtualFile != null) {
            NoteStorageRepository
                .getInstance(project)
                .addNote(project, title, virtualFile.path)

            FileEditorManager
                .getInstance(project)
                .openFile(virtualFile, true)
        }

        return virtualFile
    }

    fun openNote(project: Project, note: Note) {
        val virtualFile = LocalFileSystem
            .getInstance()
            .findFileByPath(note.filePath)

        if (virtualFile != null) {
            FileEditorManager
                .getInstance(project)
                .openFile(virtualFile, true)
        }
    }

    fun confirmAndDeleteNote(project: Project, note: Note) {
        val result = Messages.showYesNoDialog(
            project,
            MessageHelper.getMessage("dialog.delete.note.message", note.title),
            MessageHelper.getMessage("dialog.delete.note.title"),
            Messages.getQuestionIcon()
        )

        if (result == Messages.YES) {
            deleteNote(project, note)
        }
    }

    fun deleteNote(project: Project, note: Note) {
        val file = File(note.filePath)
        if (file.exists()) {
            file.delete()
        }

        NoteStorageRepository
            .getInstance(project)
            .removeNote(project, note.id)
    }

    fun renameNote(project: Project, note: Note) {
        val oldFile = File(note.filePath)
        var newTitle: String?
        do {
            newTitle = Messages.showInputDialog(
                project,
                MessageHelper.getMessage("dialog.rename.note.message"),
                MessageHelper.getMessage("dialog.rename.note.title"),
                Messages.getQuestionIcon(),
                note.title,
                null
            )

            if (newTitle.isNullOrBlank() || newTitle == note.title) {
                return
            }

            val newFile = File(oldFile.parent, newTitle)
            if (newFile.exists()) {
                Messages
                    .showWarningDialog(
                        project,
                        MessageHelper.getMessage("dialog.rename.error.exists", newTitle),
                        MessageHelper.getMessage("dialog.rename.error.title")
                    )
            } else {
                try {
                    val oldFile = File(note.filePath)
                    val newFile = File(oldFile.parent, newTitle)

                    if (oldFile.exists() && oldFile.renameTo(newFile)) {
                        note.title = newTitle
                        note.filePath = newFile.absolutePath
                        NoteStorageRepository
                            .getInstance(project)
                            .updateNote(project, note.id, newTitle)
                    } else {
                        throw RuntimeException(
                            MessageHelper.getMessage("dialog.rename.error.failed")
                        )
                    }
                } catch (_: Exception) {
                    Messages
                        .showErrorDialog(
                            project,
                            MessageHelper.getMessage("dialog.rename.error.failed"),
                            MessageHelper.getMessage("dialog.rename.error.title")
                        )
                }
                break
            }
        } while (true)
    }
}
