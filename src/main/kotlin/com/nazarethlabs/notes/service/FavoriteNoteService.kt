package com.nazarethlabs.notes.service

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.repository.NoteStorageRepository

class FavoriteNoteService {

    fun toggleFavorite(project: Project, note: Note) {
        NoteStorageRepository
            .getInstance(project)
            .toggleFavorite(project, note.id)
    }
}
