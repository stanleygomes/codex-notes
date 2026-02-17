package com.nazarethlabs.codex.ui.settings.component

import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.Messages
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository
import com.nazarethlabs.codex.service.note.ExportNotesService
import java.awt.FlowLayout
import java.io.File
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JPanel

class NotesConfigExportNotesPanelComponent {
    private val exportNotesService = ExportNotesService()

    fun createExportNotesPanel(): JPanel {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
        panel.border = JBUI.Borders.empty()
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
