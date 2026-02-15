package com.nazarethlabs.codex.service.note

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.DialogHelper
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository

class DeleteNoteService {
    fun confirmAndDelete(
        project: Project,
        note: Note,
    ) {
        val result =
            DialogHelper.showYesNoDialog(
                project,
                MessageHelper.getMessage("dialog.delete.note.message", note.title),
                MessageHelper.getMessage("dialog.delete.note.title"),
            )

        if (result == Messages.YES) {
            FileHelper.deleteFile(note.filePath)

            NoteStorageRepository
                .getInstance()
                .removeNote(note.id)
        }
    }
}
