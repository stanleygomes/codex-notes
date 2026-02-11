package org.jetbrains.plugins.template.ui.toolbar.button

import com.intellij.icons.AllIcons
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.helper.MessageHelper
import org.jetbrains.plugins.template.ui.component.ButtonComponent
import javax.swing.JButton

class ToolbarButtonSettingsComponent {

    fun build(project: Project): JButton {
        val settingsButton = ButtonComponent()
            .build(AllIcons.General.Settings, MessageHelper.getMessage("toolbar.settings"))

        settingsButton.addActionListener {
            ShowSettingsUtil.getInstance().showSettingsDialog(
                project,
                MessageHelper.getMessage("settings.display.name")
            )
        }

        return settingsButton
    }
}

