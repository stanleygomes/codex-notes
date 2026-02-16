package com.nazarethlabs.codex.state

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.nazarethlabs.codex.dto.Note

@Service(Service.Level.APP)
class SelectedNoteStateManager {
    private val listeners = mutableListOf<SelectedNoteListener>()
    private var selectedNote: Note? = null

    fun setSelectedNote(note: Note?) {
        selectedNote = note
        notifyListeners()
    }

    fun getSelectedNote(): Note? = selectedNote

    private fun notifyListeners() {
        listeners.forEach { listener ->
            listener.onSelectedNoteChanged(selectedNote)
        }
    }

    companion object {
        fun getInstance(): SelectedNoteStateManager = service()
    }
}

interface SelectedNoteListener {
    fun onSelectedNoteChanged(note: Note?)
}
