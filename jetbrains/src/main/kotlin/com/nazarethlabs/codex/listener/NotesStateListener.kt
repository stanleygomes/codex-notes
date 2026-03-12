package com.nazarethlabs.codex.listener

import com.nazarethlabs.codex.dto.Note

interface NotesStateListener {
    fun onNotesStateChanged(notes: List<Note>)
}
