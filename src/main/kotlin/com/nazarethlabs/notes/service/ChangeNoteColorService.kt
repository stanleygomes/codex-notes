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
        val allNotes = noteStorage.getAllNotes()
        val targetNote = allNotes.find { it.id == note.id }

        if (targetNote != null) {
            targetNote.color = color
            targetNote.updatedAt = System.currentTimeMillis()
            noteStorage.updateNote(targetNote.id)
        }
    }
}
