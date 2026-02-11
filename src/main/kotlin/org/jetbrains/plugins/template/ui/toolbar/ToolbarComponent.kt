package org.jetbrains.plugins.template.ui.toolbar

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBUI
import org.jetbrains.plugins.template.ui.noteslist.NotesListComponent
import org.jetbrains.plugins.template.ui.toolbar.button.ToolbarButtonCreateNoteComponent
import org.jetbrains.plugins.template.ui.toolbar.button.ToolbarButtonSearchComponent
import org.jetbrains.plugins.template.ui.toolbar.button.ToolbarButtonSortComponent
import java.awt.BorderLayout
import java.awt.BorderLayout.EAST
import java.awt.BorderLayout.WEST
import javax.swing.Box
import javax.swing.BoxLayout

class ToolbarComponent {

    fun build(project: Project, notesListComponent: NotesListComponent): JBPanel<JBPanel<*>> {
        return JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()
            border = JBUI.Borders.empty(5)

            val createNote = ToolbarButtonCreateNoteComponent()
                .build(project)

            add(createNote, WEST)

            val rightPanel = JBPanel<JBPanel<*>>().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)

                val sortButton = ToolbarButtonSortComponent()
                    .build(notesListComponent)
                add(sortButton)

                add(Box.createHorizontalStrut(2))

                val searchButton = ToolbarButtonSearchComponent()
                    .build(notesListComponent)
                add(searchButton)
            }

            add(rightPanel, EAST)
        }
    }
}
