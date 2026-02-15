package com.nazarethlabs.notes.listener

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class NoteEventListener {
    private val listeners = mutableListOf<NoteListener>()

    fun addListener(listener: NoteListener) {
        listeners.add(listener)
    }

    fun notifyNoteCreated() {
        listeners.forEach {
            it.onNoteCreated()
        }
    }

    fun notifyNoteUpdated() {
        listeners.forEach {
            it.onNoteUpdated()
        }
    }

    fun notifyNoteDeleted() {
        listeners.forEach {
            it.onNoteDeleted()
        }
    }

    companion object {
        fun getInstance(project: Project): NoteEventListener = project.service()
    }
}
