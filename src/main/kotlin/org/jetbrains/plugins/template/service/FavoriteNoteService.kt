package org.jetbrains.plugins.template.service

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.repository.NoteStorageRepository

class FavoriteNoteService {

    fun toggleFavorite(project: Project, note: Note) {
        NoteStorageRepository
            .getInstance(project)
            .toggleFavorite(project, note.id)
    }
}
