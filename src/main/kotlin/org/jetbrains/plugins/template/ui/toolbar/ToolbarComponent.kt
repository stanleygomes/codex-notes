package org.jetbrains.plugins.template.ui.toolbar

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import org.jetbrains.plugins.template.ui.noteslist.NotesListComponent
import org.jetbrains.plugins.template.ui.toolbar.button.ToolbarButtonCreateNoteComponent
import org.jetbrains.plugins.template.ui.toolbar.button.ToolbarButtonSearchComponent
import java.awt.BorderLayout

class ToolbarComponent {

    fun build(project: Project, notesListComponent: NotesListComponent): JBPanel<JBPanel<*>> {
        return JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()

            val createNote = ToolbarButtonCreateNoteComponent()
                .build(project)

            add(createNote, BorderLayout.WEST)

            val searchButton = ToolbarButtonSearchComponent()
                .build(notesListComponent)

            add(searchButton, BorderLayout.EAST)
        }
    }
}
