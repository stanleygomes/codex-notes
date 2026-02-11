package org.jetbrains.plugins.template.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.listener.NoteListener

@Service(Service.Level.PROJECT)
class NoteEventService {
    private val listeners = mutableListOf<NoteListener>()

    fun addListener(listener: NoteListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: NoteListener) {
        listeners.remove(listener)
    }

    fun notifyNoteCreated() {
        listeners.forEach { it.onNoteCreated() }
    }

    fun notifyNoteUpdated() {
        listeners.forEach { it.onNoteUpdated() }
    }

    fun notifyNoteDeleted() {
        listeners.forEach { it.onNoteDeleted() }
    }

    companion object {
        fun getInstance(project: Project): NoteEventService =
            project.service()
    }
}

