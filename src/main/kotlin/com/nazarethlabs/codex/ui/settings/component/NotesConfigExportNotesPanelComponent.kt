package com.nazarethlabs.codex.ui.settings.component

import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.Messages
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.helper.SentryHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository
import com.nazarethlabs.codex.service.note.ExportNotesService
import java.io.File
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.JPanel

class NotesConfigExportNotesPanelComponent {
    private val exportNotesService = ExportNotesService()

    fun createExportNotesPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.border = JBUI.Borders.empty()

        val titleLabel = JLabel(MessageHelper.getMessage("settings.export.notes.title"))
        titleLabel.font = titleLabel.font.deriveFont(14.0f).deriveFont(java.awt.Font.BOLD)
        panel.add(titleLabel)

        val descriptionLabel = JLabel(MessageHelper.getMessage("settings.export.notes.description"))
        descriptionLabel.font = descriptionLabel.font.deriveFont(12.0f)
        panel.add(descriptionLabel)

        panel.add(JPanel().apply { preferredSize = java.awt.Dimension(0, 10) })

        panel.add(createExportNotesButton())

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
            fileChooser.selectedFile = File("codex-notes-export.zip")

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
                    SentryHelper.captureException(e)
                    Messages.showErrorDialog(
                        "${MessageHelper.getMessage("settings.export.notes.error")}: ${e.message}",
                        MessageHelper.getMessage("settings.display.name"),
                    )
                }
            }
        }

        return button
    }
}
