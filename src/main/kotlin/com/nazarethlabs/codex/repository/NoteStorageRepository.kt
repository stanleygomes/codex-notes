package com.nazarethlabs.codex.repository

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.ProjectManager
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.listener.NoteEventListener
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import java.io.File
import java.util.UUID

@Service(Service.Level.APP)
class NoteStorageRepository {
    private val databaseManager: NoteDatabaseManager by lazy {
        val notesDir = resolveNotesDirectory()
        NoteDatabaseManager(File(notesDir, "data").absolutePath)
    }

    private fun resolveNotesDirectory(): String =
        try {
            NotesSettingsService().getNotesDirectory()
        } catch (_: Exception) {
            FileHelper.getDefaultNotesDir()
        }

    fun addNote(
        title: String,
        filePath: String,
    ): Note {
        val note =
            Note(
                id = UUID.randomUUID().toString(),
                title = title,
                filePath = filePath,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
            )

        databaseManager.insertNote(note)

        notifyAllProjects { it.notifyNoteCreated() }

        return note
    }

    fun updateNote(
        id: String,
        title: String? = null,
        filePath: String? = null,
    ) {
        databaseManager.updateNote(id, title, filePath)

        notifyAllProjects { it.notifyNoteUpdated() }
    }

    fun toggleFavorite(id: String) {
        databaseManager.toggleFavorite(id)

        notifyAllProjects { it.notifyNoteUpdated() }
    }

    fun changeColor(
        id: String,
        color: NoteColorEnum,
    ) {
        databaseManager.changeColor(id, color)

        notifyAllProjects { it.notifyNoteUpdated() }
    }

    fun removeNote(id: String) {
        databaseManager.deleteNote(id)

        notifyAllProjects { it.notifyNoteDeleted() }
    }

    fun getAllNotes(): List<Note> = databaseManager.getAllNotes()

    fun importNote(note: Note) {
        if (!databaseManager.noteExists(note.id)) {
            databaseManager.insertNote(note)
        }
    }

    private fun notifyAllProjects(action: (NoteEventListener) -> Unit) {
        ProjectManager.getInstance().openProjects.forEach { project ->
            action(NoteEventListener.getInstance(project))
        }
    }

    companion object {
        fun getInstance(): NoteStorageRepository = ApplicationManager.getApplication().getService(NoteStorageRepository::class.java)
    }
}
