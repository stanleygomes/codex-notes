package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.dto.Note

object NoteNameHelper {
    fun generateUntitledName(notes: List<Note>): String {
        val untitledNumbers =
            notes.mapNotNull { note ->
                if (note.title.startsWith("Untitled ")) {
                    note.title.substringAfter("Untitled ").toIntOrNull()
                } else {
                    null
                }
            }

        val nextNumber = (untitledNumbers.maxOrNull() ?: 0) + 1
        return "Untitled $nextNumber"
    }

    fun generateDuplicateName(
        baseTitle: String,
        notes: List<Note>,
    ): String {
        val existingNumbers =
            notes.mapNotNull { note ->
                if (note.title == baseTitle) {
                    1
                } else if (note.title.startsWith("$baseTitle ")) {
                    note.title.substringAfter("$baseTitle ").toIntOrNull()
                } else {
                    null
                }
            }

        val nextNumber = (existingNumbers.maxOrNull() ?: 1) + 1
        return "$baseTitle $nextNumber"
    }
}
