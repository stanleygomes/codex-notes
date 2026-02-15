package com.nazarethlabs.codex.ui.toolbar

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.nazarethlabs.codex.ui.toolbar.button.ToolbarButtonCreateNoteComponent

class ToolbarLeftComponent {
    fun build(project: Project): JBPanel<JBPanel<*>> =
        JBPanel<JBPanel<*>>().apply {
            val createNote =
                ToolbarButtonCreateNoteComponent()
                    .build(project)

            add(createNote)
        }
}
