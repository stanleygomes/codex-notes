package com.nazarethlabs.notes.service

import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.enum.NoteColorEnum
import com.nazarethlabs.notes.repository.NoteStorageRepository

class ChangeNoteColorService {
    private val noteStorage = NoteStorageRepository.getInstance()

    fun changeColor(
        note: Note,
        color: NoteColorEnum,
    ) {
        note.color = color
        note.updatedAt = System.currentTimeMillis()
        noteStorage.updateNote(note.id)
    }
}
