package com.nazarethlabs.codex.state

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.nazarethlabs.codex.dto.Note

@Service(Service.Level.APP)
class SelectedNoteStateManager {
    private val listeners = mutableListOf<SelectedNoteListener>()
    private var selectedNotes: List<Note> = emptyList()

    fun setSelectedNotes(notes: List<Note>) {
        selectedNotes = notes
        notifyListeners()
    }

    fun getSelectedNotes(): List<Note> = selectedNotes

    fun getSelectedNote(): Note? = selectedNotes.firstOrNull()

    fun hasMultipleSelection(): Boolean = selectedNotes.size > 1

    private fun notifyListeners() {
        listeners.forEach { listener ->
            listener.onSelectedNotesChanged(selectedNotes)
        }
    }

    companion object {
        fun getInstance(): SelectedNoteStateManager = service()
    }
}

interface SelectedNoteListener {
    fun onSelectedNotesChanged(notes: List<Note>)
}
