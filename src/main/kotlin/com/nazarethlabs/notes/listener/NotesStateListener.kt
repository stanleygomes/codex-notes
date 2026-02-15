package com.nazarethlabs.notes.listener

import com.nazarethlabs.notes.dto.Note

interface NotesStateListener {
    fun onNotesStateChanged(notes: List<Note>)
}
