package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.repository.NoteStorageRepository
import com.nazarethlabs.codex.service.sentry.SentryEventHelper

class FavoriteNotesService {
    fun favoriteAll(notes: List<Note>) {
        val repository = NoteStorageRepository.getInstance()
        notes.forEach { note ->
            if (!note.isFavorite) {
                repository.toggleFavorite(note.id)
            }
        }
    }

    fun unfavoriteAll(notes: List<Note>) {
        val repository = NoteStorageRepository.getInstance()
        notes.forEach { note ->
            if (note.isFavorite) {
                repository.toggleFavorite(note.id)
            }
        }
    }

    fun toggleFavorite(note: Note) {
        NoteStorageRepository
            .getInstance()
            .toggleFavorite(note.id)
        SentryEventHelper.captureEvent("note.favorited")
    }
}
