package com.nazarethlabs.codex.ui.toolbar

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel

class ToolbarComponent {
    fun build(project: Project): JBPanel<JBPanel<*>> {
        val left =
            ToolbarLeftComponent()
                .build(project)
        val right =
            ToolbarRightComponent()
                .build(project)

        return ToolbarContainerComponent()
            .build(left, right)
    }
}
