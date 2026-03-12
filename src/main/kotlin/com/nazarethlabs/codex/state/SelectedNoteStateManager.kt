package com.nazarethlabs.codex.state

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.nazarethlabs.codex.dto.Note

@Service(Service.Level.APP)
class SelectedNoteStateManager {
    private val listeners = mutableListOf<(List<Note>) -> Unit>()
    private var selectedNotes: List<Note> = emptyList()

    fun setSelectedNotes(notes: List<Note>) {
        selectedNotes = notes
        notifyListeners()
    }

    fun getSelectedNotes(): List<Note> = selectedNotes

    fun getSelectedNote(): Note? = selectedNotes.firstOrNull()

    fun hasMultipleSelection(): Boolean = selectedNotes.size > 1

    fun addListener(listener: (List<Note>) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (List<Note>) -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { listener ->
            listener(selectedNotes)
        }
    }

    companion object {
        fun getInstance(): SelectedNoteStateManager = service()
    }
}
