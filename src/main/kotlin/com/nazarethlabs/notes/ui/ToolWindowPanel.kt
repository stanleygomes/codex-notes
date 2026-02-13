package com.nazarethlabs.notes.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.nazarethlabs.notes.ui.noteslist.NotesListComponent
import com.nazarethlabs.notes.ui.toolbar.ToolbarComponent
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.NORTH

class ToolWindowPanel {
    fun create(project: Project): JBPanel<JBPanel<*>> {
        val notesListComponent = NotesListComponent()

        return JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()

            val toolbar =
                ToolbarComponent()
                    .build(project, notesListComponent)

            add(toolbar, NORTH)

            val notesList = notesListComponent.build(project)

            add(notesList, CENTER)
        }
    }
}
