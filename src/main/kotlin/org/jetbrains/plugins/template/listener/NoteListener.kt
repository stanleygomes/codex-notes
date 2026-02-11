package org.jetbrains.plugins.template.listener

interface NoteListener {
    fun onNoteCreated()
    fun onNoteUpdated()
    fun onNoteDeleted()
}
