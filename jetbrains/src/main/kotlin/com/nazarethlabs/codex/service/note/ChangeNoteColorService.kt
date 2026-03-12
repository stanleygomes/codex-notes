package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.repository.NoteRepository

class ChangeNoteColorService {
    private val noteStorage = NoteRepository.getInstance()

    fun changeColor(
        note: Note,
        color: NoteColorEnum,
    ) {
        noteStorage.changeColor(note.id, color)
    }
}
