package com.nazarethlabs.notes.service

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.helper.DialogHelper
import com.nazarethlabs.notes.helper.FileHelper
import com.nazarethlabs.notes.helper.MessageHelper
import com.nazarethlabs.notes.repository.NoteStorageRepository

class RenameNoteService {

    fun rename(project: Project, note: Note) {
        var newTitle: String?
        do {
            newTitle = DialogHelper.showInputDialog(
                project,
                MessageHelper.getMessage("dialog.rename.note.message"),
                MessageHelper.getMessage("dialog.rename.note.title"),
                note.title
            )

            if (newTitle.isNullOrBlank() || newTitle == note.title) {
                return
            }

            if (FileHelper.fileExists(FileHelper.getParentPath(note.filePath), newTitle)) {
                DialogHelper.showWarningDialog(
                    project,
                    MessageHelper.getMessage("dialog.rename.error.exists", newTitle),
                    MessageHelper.getMessage("dialog.rename.error.title")
                )
            } else {
                try {
                    if (FileHelper.renameFile(note.filePath, newTitle)) {
                        note.title = newTitle
                        note.filePath = FileHelper.getNewFilePath(note.filePath, newTitle)
                        NoteStorageRepository
                            .getInstance()
                            .updateNote(project, note.id, newTitle)
                    } else {
                        throw RuntimeException(
                            MessageHelper.getMessage("dialog.rename.error.failed")
                        )
                    }
                } catch (_: Exception) {
                    DialogHelper.showErrorDialog(
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
