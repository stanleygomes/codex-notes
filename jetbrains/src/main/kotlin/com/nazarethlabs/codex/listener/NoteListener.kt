package com.nazarethlabs.codex.listener

interface NoteListener {
    fun onNoteCreated()

    fun onNoteUpdated()

    fun onNoteDeleted()
}
