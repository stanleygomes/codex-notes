package com.nazarethlabs.codex.service.note

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note

class OpenNotesService {
    private val openNoteService = OpenNoteService()

    fun openAll(
        project: Project,
        notes: List<Note>,
    ) {
        notes.forEach { note ->
            openNoteService.open(project, note)
        }
    }
}
