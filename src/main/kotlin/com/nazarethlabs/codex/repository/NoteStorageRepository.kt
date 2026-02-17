package com.nazarethlabs.codex.repository

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.ProjectManager
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.listener.NoteEventListener
import com.nazarethlabs.codex.state.NoteState
import java.util.UUID

@Service(Service.Level.APP)
@State(
    name = "NoteStorage",
    storages = [Storage("notes.xml")],
)
class NoteStorageRepository : PersistentStateComponent<NoteState> {
    private var state = NoteState()

    override fun getState(): NoteState = state

    override fun loadState(state: NoteState) {
        this.state = state
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

        state.notes.add(note)

        notifyAllProjects { it.notifyNoteCreated() }

        return note
    }

    fun updateNote(
        id: String,
        title: String? = null,
    ) {
        state.notes.find { it.id == id }?.apply {
            title?.let { this.title = it }
            updatedAt = System.currentTimeMillis()
        }

        notifyAllProjects { it.notifyNoteUpdated() }
    }

    fun toggleFavorite(id: String) {
        state.notes.find { it.id == id }?.apply {
            isFavorite = !isFavorite
            updatedAt = System.currentTimeMillis()
        }

        notifyAllProjects { it.notifyNoteUpdated() }
    }

    fun removeNote(id: String) {
        state.notes.removeIf { it.id == id }

        notifyAllProjects { it.notifyNoteDeleted() }
    }

    fun getAllNotes(): List<Note> = state.notes.toList()

    fun getNotes(): List<Note> = state.notes.toList()

    private fun notifyAllProjects(action: (NoteEventListener) -> Unit) {
        ProjectManager.getInstance().openProjects.forEach { project ->
            action(NoteEventListener.getInstance(project))
        }
    }

    companion object {
        fun getInstance(): NoteStorageRepository = ApplicationManager.getApplication().getService(NoteStorageRepository::class.java)
    }
}
