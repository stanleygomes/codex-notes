package com.nazarethlabs.notes.ui.toolbar

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.nazarethlabs.notes.ui.toolbar.button.ToolbarButtonNoteActionsComponent
import com.nazarethlabs.notes.ui.toolbar.button.ToolbarButtonSearchComponent
import com.nazarethlabs.notes.ui.toolbar.button.ToolbarButtonSettingsComponent
import com.nazarethlabs.notes.ui.toolbar.button.ToolbarButtonSortComponent
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.BoxLayout.X_AXIS

class ToolbarRightComponent {
    fun build(project: Project): JBPanel<JBPanel<*>> =
        JBPanel<JBPanel<*>>().apply {
            layout = BoxLayout(this, X_AXIS)

            val searchButton =
                ToolbarButtonSearchComponent()
                    .build()
            add(searchButton)

            add(Box.createHorizontalStrut(4))

            val sortButton =
                ToolbarButtonSortComponent()
                    .build()
            add(sortButton)

            add(Box.createHorizontalStrut(4))

            val actionsButton =
                ToolbarButtonNoteActionsComponent()
                    .build(project)
            add(actionsButton)

            add(Box.createHorizontalStrut(4))

            val settingsButton =
                ToolbarButtonSettingsComponent()
                    .build(project)
            add(settingsButton)
        }
}
