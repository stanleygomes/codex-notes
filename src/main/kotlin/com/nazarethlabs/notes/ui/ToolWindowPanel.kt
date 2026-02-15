package com.nazarethlabs.notes.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.nazarethlabs.notes.ui.noteslist.NotesListComponent
import com.nazarethlabs.notes.ui.toolbar.ToolbarComponent

class ToolWindowPanel {
    fun build(project: Project): JBPanel<JBPanel<*>> {
        val notesListComponent = NotesListComponent()
        val toolbar = ToolbarComponent()
            .build(project, notesListComponent)
        val notesList = notesListComponent
            .build(project)

        return WindowContainerComponent()
            .build(toolbar, notesList)
    }
}
