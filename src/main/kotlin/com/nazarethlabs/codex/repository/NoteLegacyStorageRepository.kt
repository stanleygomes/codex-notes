package com.nazarethlabs.codex.repository

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.state.NoteState

@Service(Service.Level.APP)
@State(
    name = "NoteStorage",
    storages = [Storage("notes.xml")],
)
class NoteLegacyStorageRepository : PersistentStateComponent<NoteState> {
    private var state = NoteState()

    override fun getState(): NoteState = state

    override fun loadState(state: NoteState) {
        this.state = state
    }

    fun getAllNotes(): List<Note> = state.notes.toList()

    fun clearNotes() {
        state.notes.clear()
    }

    companion object {
        fun getInstance(): NoteLegacyStorageRepository =
            ApplicationManager.getApplication().getService(NoteLegacyStorageRepository::class.java)
    }
}
