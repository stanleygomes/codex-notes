package org.jetbrains.plugins.template.ui.toolbar

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import org.jetbrains.plugins.template.ui.toolbar.button.ToolbarButtonCreateNoteComponent
import java.awt.FlowLayout
import java.awt.FlowLayout.LEFT

class ToolbarComponent {

    fun build(project: Project): JBPanel<JBPanel<*>> {
        return JBPanel<JBPanel<*>>().apply {
            layout = FlowLayout(LEFT)

            val createNote = ToolbarButtonCreateNoteComponent()
                .build(project)

            add(createNote)
        }
    }
}
