package com.nazarethlabs.codex.ui.toolbar.button

import com.intellij.icons.AllIcons.General.Settings
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.helper.MessageHelper.getMessage
import com.nazarethlabs.codex.ui.component.ButtonComponent
import javax.swing.JButton

class ToolbarButtonSettingsComponent {
    fun build(project: Project): JButton {
        val settingsButton =
            ButtonComponent()
                .build(Settings, getMessage("toolbar.settings"))

        settingsButton.addActionListener {
            ShowSettingsUtil.getInstance().showSettingsDialog(
                project,
                getMessage("settings.display.name"),
            )
        }

        return settingsButton
    }
}
