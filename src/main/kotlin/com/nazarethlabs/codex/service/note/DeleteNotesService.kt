package com.nazarethlabs.codex.service.note

import com.intellij.openapi.diagnostic.thisLogger
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
        val noteNames = formatNoteNames(notes)
        val result =
            DialogHelper.showYesNoDialog(
                project,
                MessageHelper.getMessage("dialog.delete.notes.message", noteNames),
                MessageHelper.getMessage("dialog.delete.notes.title"),
            )

        if (result == Messages.YES) {
            notes.forEach { note ->
                try {
                    FileHelper.deleteFile(note.filePath)
                } catch (e: Exception) {
                    thisLogger().error("Failed to delete file: ${note.filePath}", e)
                }
                NoteStorageRepository
                    .getInstance()
                    .removeNote(note.id)
            }
        }
    }

    private fun formatNoteNames(notes: List<Note>): String {
        val maxDisplayedNotes = 5
        return if (notes.size <= maxDisplayedNotes) {
            notes.joinToString(", ") { "\"${it.title}\"" }
        } else {
            val displayedNames = notes.take(maxDisplayedNotes).joinToString(", ") { "\"${it.title}\"" }
            val remainingCount = notes.size - maxDisplayedNotes
            MessageHelper.getMessage("dialog.delete.notes.more", displayedNames, remainingCount.toString())
        }
    }
}
