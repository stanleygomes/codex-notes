package org.jetbrains.plugins.template.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import org.jetbrains.plugins.template.ui.noteslist.NotesListComponent
import org.jetbrains.plugins.template.ui.toolbar.ToolbarComponent
import java.awt.BorderLayout

class ToolWindowPanel {

    fun create(project: Project): JBPanel<JBPanel<*>> {
        return JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()

            val toolbar = ToolbarComponent().build(project)
            add(toolbar, BorderLayout.NORTH)

            val itemList = NotesListComponent().build()
            add(itemList, BorderLayout.CENTER)
        }
    }
}
