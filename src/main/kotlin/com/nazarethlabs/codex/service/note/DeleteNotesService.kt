package com.nazarethlabs.codex.service.note

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.DialogHelper
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository

class DeleteNotesService {
    fun confirmAndDelete(
        project: Project,
        notes: List<Note>,
    ) {
        val noteNames = notes.joinToString(", ") { "\"${it.title}\"" }
        val result =
            DialogHelper.showYesNoDialog(
                project,
                MessageHelper.getMessage("dialog.delete.notes.message", noteNames),
                MessageHelper.getMessage("dialog.delete.notes.title"),
            )

        if (result == Messages.YES) {
            notes.forEach { note ->
                FileHelper.deleteFile(note.filePath)
                NoteStorageRepository
                    .getInstance()
                    .removeNote(note.id)
            }
        }
    }
}
