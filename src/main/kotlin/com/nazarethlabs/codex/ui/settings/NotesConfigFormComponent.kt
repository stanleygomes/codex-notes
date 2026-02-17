package com.nazarethlabs.codex.ui.settings

import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.helper.OpenFolderHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository
import com.nazarethlabs.codex.service.note.ExportNotesService
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JPanel

class NotesConfigFormComponent {
    private var fileExtensionField: JBTextField? = null
    private var notesDirectoryField: JBTextField? = null
    private val openNotesFolderService = OpenFolderHelper
    private val notesSettingsService = NotesSettingsService()
    private val exportNotesService = ExportNotesService()

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
            .addLabeledComponent("", createExportNotesPanel(), 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
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

    private fun createOpenFolderPanel(): JPanel {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
        panel.border = JBUI.Borders.empty()
        panel.add(createOpenFolderButton())
        return panel
    }

    private fun createExportNotesButton(): JButton {
        val button = JButton(MessageHelper.getMessage("settings.export.notes.button"))
        button.icon = AllIcons.ToolbarDecorator.Export
        button.toolTipText = MessageHelper.getMessage("settings.export.notes.tooltip")

        button.addActionListener {
            val notes = NoteStorageRepository.getInstance().getAllNotes()

            if (notes.isEmpty()) {
                Messages.showInfoMessage(
                    MessageHelper.getMessage("settings.export.notes.empty"),
                    MessageHelper.getMessage("settings.display.name"),
                )
                return@addActionListener
            }

            val fileChooser = JFileChooser()
            fileChooser.dialogTitle = MessageHelper.getMessage("settings.export.notes.button")
            fileChooser.selectedFile = java.io.File("codex-notes-export.zip")

            val result = fileChooser.showSaveDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    val outputPath = fileChooser.selectedFile.absolutePath
                    val zipFile = exportNotesService.export(notes, outputPath)
                    Messages.showInfoMessage(
                        MessageHelper.getMessage("settings.export.notes.success", zipFile.absolutePath),
                        MessageHelper.getMessage("settings.display.name"),
                    )
                } catch (e: Exception) {
                    Messages.showErrorDialog(
                        "${MessageHelper.getMessage("settings.export.notes.error")}: ${e.message}",
                        MessageHelper.getMessage("settings.display.name"),
                    )
                }
            }
        }

        return button
    }

    private fun createExportNotesPanel(): JPanel {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
        panel.border = JBUI.Borders.empty()
        panel.add(createExportNotesButton())
        return panel
    }

    fun getFileExtensionField(): JBTextField? = fileExtensionField

    fun getNotesDirectoryField(): JBTextField? = notesDirectoryField
}
