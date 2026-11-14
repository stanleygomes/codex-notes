package com.nazarethlabs.codex.ui.settings.component

import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.helper.OpenFolderHelper
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JPanel

class NotesConfigOpenFolderPanelComponent {
    private val openNotesFolderService = OpenFolderHelper
    private val notesSettingsService = NotesSettingsService()

    fun createOpenFolderPanel(): JPanel {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
        panel.border = JBUI.Borders.empty()
        panel.add(createOpenFolderButton())
        return panel
    }

    private fun createOpenFolderButton(): JButton {
        val button = JButton(MessageHelper.getMessage("settings.open.notes.folder.button"))
        button.icon = AllIcons.Actions.MenuOpen
        button.toolTipText = MessageHelper.getMessage("settings.open.notes.folder.tooltip")

        button.addActionListener {
            openNotesFolderService.openFolder(notesSettingsService.getNotesDirectory())
        }

        return button
    }
}
