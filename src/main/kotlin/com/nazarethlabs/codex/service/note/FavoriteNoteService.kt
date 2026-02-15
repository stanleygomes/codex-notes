package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.repository.NoteStorageRepository

class FavoriteNoteService {
    fun toggleFavorite(note: Note) {
        NoteStorageRepository
            .getInstance()
            .toggleFavorite(note.id)
    }
}
