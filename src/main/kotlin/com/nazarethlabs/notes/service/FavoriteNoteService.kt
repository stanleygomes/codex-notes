package com.nazarethlabs.notes.service

import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.repository.NoteStorageRepository

class FavoriteNoteService {
    fun toggleFavorite(note: Note) {
        NoteStorageRepository
            .getInstance()
            .toggleFavorite(note.id)
    }
}
