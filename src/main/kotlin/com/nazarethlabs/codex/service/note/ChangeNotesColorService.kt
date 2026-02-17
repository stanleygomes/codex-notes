package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.repository.NoteStorageRepository

class ChangeNotesColorService {
    private val noteStorage = NoteStorageRepository.getInstance()

    fun changeColorAll(
        notes: List<Note>,
        color: NoteColorEnum,
    ) {
        notes.forEach { note ->
            note.color = color
            note.updatedAt = System.currentTimeMillis()
            noteStorage.updateNote(note.id)
        }
    }
}
