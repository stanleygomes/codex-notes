package com.nazarethlabs.codex.service.note

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.DialogHelper
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository

class RenameNoteService {
    fun rename(
        project: Project,
        note: Note,
    ) {
        val fileExtension = getFileExtension(note.filePath)
        val parentPath = FileHelper.getParentPath(note.filePath)
        val newTitle = getNewTitle(project, note.title, fileExtension, parentPath)
        if (newTitle != null) {
            performRename(project, note, newTitle, fileExtension)
        }
    }

    private fun getFileExtension(filePath: String): String = filePath.substringAfterLast('.', "")

    private fun getNewTitle(
        project: Project,
        currentTitle: String,
        fileExtension: String,
        parentPath: String,
    ): String? {
        var newTitle: String?
        do {
            newTitle =
                DialogHelper.showInputDialog(
                    project,
                    MessageHelper.getMessage("dialog.rename.note.message"),
                    MessageHelper.getMessage("dialog.rename.note.title"),
                    currentTitle,
                )

            if (newTitle.isNullOrBlank() || newTitle == currentTitle) {
                return null
            }

            val newFileName = createNewFileName(newTitle, fileExtension)

            if (fileExists(parentPath, newFileName)) {
                DialogHelper.showWarningDialog(
                    project,
                    MessageHelper.getMessage("dialog.rename.error.exists", newTitle),
                    MessageHelper.getMessage("dialog.rename.error.title"),
                )
            } else {
                return newTitle
            }
        } while (true)
    }

    private fun createNewFileName(
        newTitle: String,
        fileExtension: String,
    ): String = if (fileExtension.isNotEmpty()) "$newTitle.$fileExtension" else newTitle

    private fun fileExists(
        parentPath: String,
        fileName: String,
    ): Boolean = FileHelper.fileExists(parentPath, fileName)

    private fun performRename(
        project: Project,
        note: Note,
        newTitle: String,
        fileExtension: String,
    ) {
        val newFileName = createNewFileName(newTitle, fileExtension)
        try {
            if (FileHelper.renameFile(note.filePath, newFileName)) {
                note.title = newTitle
                note.filePath = FileHelper.getNewFilePath(note.filePath, newFileName)
                NoteStorageRepository
                    .getInstance()
                    .updateNote(note.id, newTitle)
            } else {
                throw RuntimeException(
                    MessageHelper.getMessage("dialog.rename.error.failed"),
                )
            }
        } catch (_: Exception) {
            DialogHelper.showErrorDialog(
                project,
                MessageHelper.getMessage("dialog.rename.error.failed"),
                MessageHelper.getMessage("dialog.rename.error.title"),
            )
        }
    }
}
