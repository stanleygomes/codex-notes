package com.nazarethlabs.notes.helper

import com.nazarethlabs.notes.dto.Note

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
}
