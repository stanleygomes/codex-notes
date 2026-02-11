package org.jetbrains.plugins.template.service

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.helper.DialogHelper
import org.jetbrains.plugins.template.helper.FileHelper
import org.jetbrains.plugins.template.helper.MessageHelper
import org.jetbrains.plugins.template.repository.NoteStorageRepository

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
                            .getInstance(project)
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
