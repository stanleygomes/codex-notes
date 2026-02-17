package com.nazarethlabs.codex.ui.settings

import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import com.nazarethlabs.codex.service.settings.OpenNotesFolderService
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JPanel

class NotesConfigFormComponent {
    private var fileExtensionField: JBTextField? = null
    private var notesDirectoryField: JBTextField? = null

    fun build(): JPanel {
        fileExtensionField = JBTextField()
        notesDirectoryField = JBTextField()

        val descriptionLabel = JBLabel(MessageHelper.getMessage("settings.file.extension.description"))
        val notesDirectoryDescriptionLabel = JBLabel(MessageHelper.getMessage("settings.notes.directory.description"))

        return FormBuilder
            .createFormBuilder()
            .addLabeledComponent(
                MessageHelper.getMessage("settings.file.extension.label"),
                fileExtensionField!!,
                1,
                false,
            ).addComponentToRightColumn(descriptionLabel, 1)
            .addLabeledComponent(
                MessageHelper.getMessage("settings.notes.directory.label"),
                notesDirectoryField!!,
                1,
                false,
            ).addComponentToRightColumn(notesDirectoryDescriptionLabel, 1)
            .addLabeledComponent("", createOpenFolderPanel(), 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    private fun createOpenFolderButton(): JButton {
        val button = JButton(MessageHelper.getMessage("settings.open.notes.folder.button"))
        button.icon = AllIcons.Actions.MenuOpen
        button.toolTipText = MessageHelper.getMessage("settings.open.notes.folder.tooltip")

        button.addActionListener {
            val openNotesFolderService = OpenNotesFolderService()
            val notesSettingsService = NotesSettingsService()
            openNotesFolderService.openFolder(notesSettingsService.getNotesDirectory())
        }

        return button
    }

    private fun createOpenFolderPanel(): JPanel {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
        panel.border = JBUI.Borders.empty()
        panel.add(createOpenFolderButton())
        return panel
    }

    fun getFileExtensionField(): JBTextField? = fileExtensionField

    fun getNotesDirectoryField(): JBTextField? = notesDirectoryField
}
