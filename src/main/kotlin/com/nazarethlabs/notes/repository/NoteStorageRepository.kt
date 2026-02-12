package com.nazarethlabs.notes.repository

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.listener.NoteEventListener
import com.nazarethlabs.notes.state.NoteState
import java.util.UUID

@Service(Service.Level.PROJECT)
@State(
    name = "NoteStorage",
    storages = [Storage("notes.xml")]
)
class NoteStorageRepository : PersistentStateComponent<NoteState> {
    private var state = NoteState()

    override fun getState(): NoteState = state

    override fun loadState(state: NoteState) {
        this.state = state
    }

    fun addNote(project: Project, title: String, filePath: String): Note {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            filePath = filePath,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        state.notes.add(note)

        NoteEventListener
            .getInstance(project)
            .notifyNoteCreated()

        return note
    }

    fun updateNote(project: Project, id: String, title: String? = null) {
        state.notes.find { it.id == id }?.apply {
            title?.let { this.title = it }
            updatedAt = System.currentTimeMillis()
        }

        NoteEventListener
            .getInstance(project)
            .notifyNoteUpdated()
    }

    fun toggleFavorite(project: Project, id: String) {
        state.notes.find { it.id == id }?.apply {
            isFavorite = !isFavorite
            updatedAt = System.currentTimeMillis()
        }

        NoteEventListener
            .getInstance(project)
            .notifyNoteUpdated()
    }

    fun removeNote(project: Project, id: String) {
        state.notes.removeIf { it.id == id }

        NoteEventListener
            .getInstance(project)
            .notifyNoteDeleted()
    }

    fun getAllNotes(): List<Note> = state.notes.toList()

    fun findNotesByTitle(query: String): List<Note> =
        state.notes.filter { it.title.contains(query, ignoreCase = true) }

    fun sortByTitle(): List<Note> =
        state.notes.sortedBy { it.title }

    fun sortByDate(): List<Note> =
        state.notes.sortedByDescending { it.updatedAt }

    companion object {
        fun getInstance(project: Project): NoteStorageRepository =
            project.service()
    }
}
