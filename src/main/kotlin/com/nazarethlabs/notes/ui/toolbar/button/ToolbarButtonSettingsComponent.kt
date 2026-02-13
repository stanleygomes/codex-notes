package com.nazarethlabs.notes.ui.toolbar.button

import com.intellij.icons.AllIcons
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.helper.MessageHelper
import com.nazarethlabs.notes.ui.component.ButtonComponent
import javax.swing.JButton

class ToolbarButtonSettingsComponent {
    fun build(project: Project): JButton {
        val settingsButton =
            ButtonComponent()
                .build(AllIcons.General.Settings, MessageHelper.getMessage("toolbar.settings"))

        settingsButton.addActionListener {
            ShowSettingsUtil.getInstance().showSettingsDialog(
                project,
                MessageHelper.getMessage("settings.display.name"),
            )
        }

        return settingsButton
    }
}
