package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.helper.TimeHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository

class ChangeNoteColorService {
    private val noteStorage = NoteStorageRepository.getInstance()

    fun changeColor(
        note: Note,
        color: NoteColorEnum,
    ) {
        note.color = color
        note.updatedAt = TimeHelper.getCurrentTimeMillis()
        noteStorage.updateNote(note.id)
    }
}
