package org.jetbrains.plugins.template.service

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.helper.DialogHelper
import org.jetbrains.plugins.template.helper.FileHelper
import org.jetbrains.plugins.template.helper.MessageHelper
import org.jetbrains.plugins.template.repository.NoteStorageRepository

class DeleteNoteService {

    fun confirmAndDelete(project: Project, note: Note) {
        val result = DialogHelper.showYesNoDialog(
            project,
            MessageHelper.getMessage("dialog.delete.note.message", note.title),
            MessageHelper.getMessage("dialog.delete.note.title")
        )

        if (result == Messages.YES) {
            FileHelper.deleteFile(note.filePath)

            NoteStorageRepository
                .getInstance(project)
                .removeNote(project, note.id)
        }
    }
}
