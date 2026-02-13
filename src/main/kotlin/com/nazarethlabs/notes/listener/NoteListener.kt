package com.nazarethlabs.notes.listener

interface NoteListener {
    fun onNoteCreated()

    fun onNoteUpdated()

    fun onNoteDeleted()
}
