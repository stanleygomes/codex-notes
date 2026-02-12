package com.nazarethlabs.notes.service

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.helper.DialogHelper
import com.nazarethlabs.notes.helper.FileHelper
import com.nazarethlabs.notes.helper.MessageHelper
import com.nazarethlabs.notes.repository.NoteStorageRepository

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
                .getInstance()
                .removeNote(project, note.id)
        }
    }
}
