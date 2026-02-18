package com.nazarethlabs.codex.service.note

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.helper.NoteNameHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository

class DuplicateNoteService {
    private val createNoteService = CreateNoteService()

    fun duplicate(
        project: Project,
        note: Note,
    ) {
        try {
            val content = FileHelper.readText(note.filePath)
            val extension = getFileExtension(note.filePath)
            val newTitle = generateDuplicateTitle(note.title)
            createNoteService.createWithContent(project, newTitle, extension, content)
        } catch (e: Exception) {
            thisLogger().error("Failed to duplicate note: ${note.title}", e)
        }
    }

    private fun getFileExtension(filePath: String): String {
        val ext = filePath.substringAfterLast('.', "")
        return if (ext.isNotEmpty()) ".$ext" else ""
    }

    private fun generateDuplicateTitle(baseTitle: String): String {
        val notes = NoteStorageRepository.getInstance().getAllNotes()
        return NoteNameHelper.generateDuplicateName(baseTitle, notes)
    }
}
