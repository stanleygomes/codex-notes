package com.nazarethlabs.notes.ui.toolbar

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.nazarethlabs.notes.ui.noteslist.NotesListComponent

class ToolbarComponent {
    fun build(
        project: Project,
        notesListComponent: NotesListComponent,
    ): JBPanel<JBPanel<*>> {
        val left = ToolbarLeftComponent()
            .build(project)
        val right = ToolbarRightComponent()
            .build(project, notesListComponent)

        return ToolbarContainerComponent()
            .build(left, right)
    }
}
