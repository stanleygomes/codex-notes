package com.nazarethlabs.codex.ui.settings.component

import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.Messages
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository
import com.nazarethlabs.codex.service.note.ImportNotesService
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.JPanel

class NotesConfigImportNotesPanelComponent {
    private val importNotesService = ImportNotesService()
    private val notesSettingsService = NotesSettingsService()

    fun createImportNotesPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.border = JBUI.Borders.empty()

        val titleLabel = JLabel(MessageHelper.getMessage("settings.import.notes.title"))
        titleLabel.font = titleLabel.font.deriveFont(14.0f).deriveFont(java.awt.Font.BOLD)
        panel.add(titleLabel)

        val descriptionLabel = JLabel(MessageHelper.getMessage("settings.import.notes.description"))
        descriptionLabel.font = descriptionLabel.font.deriveFont(12.0f)
        panel.add(descriptionLabel)

        panel.add(JPanel().apply { preferredSize = java.awt.Dimension(0, 10) })

        panel.add(createImportNotesButton())

        return panel
    }

    private fun createImportNotesButton(): JButton {
        val button = JButton(MessageHelper.getMessage("settings.import.notes.button"))
        button.icon = AllIcons.ToolbarDecorator.Import
        button.toolTipText = MessageHelper.getMessage("settings.import.notes.tooltip")

        button.addActionListener {
            val fileChooser = JFileChooser()
            fileChooser.dialogTitle = MessageHelper.getMessage("settings.import.notes.button")
            fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY

            val result = fileChooser.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    val sourcePath = fileChooser.selectedFile.absolutePath
                    val notesDirectory = notesSettingsService.getNotesDirectory()
                    val importedFiles = importNotesService.import(sourcePath, notesDirectory)

                    if (importedFiles.isEmpty()) {
                        Messages.showInfoMessage(
                            MessageHelper.getMessage("settings.import.notes.empty"),
                            MessageHelper.getMessage("settings.display.name"),
                        )
                    } else {
                        val noteStorageRepository = NoteStorageRepository.getInstance()
                        for (file in importedFiles) {
                            noteStorageRepository.addNote(file.nameWithoutExtension, file.absolutePath)
                        }

                        Messages.showInfoMessage(
                            MessageHelper.getMessage("settings.import.notes.success", importedFiles.size),
                            MessageHelper.getMessage("settings.display.name"),
                        )
                    }
                } catch (e: Exception) {
                    Messages.showErrorDialog(
                        "${MessageHelper.getMessage("settings.import.notes.error")}: ${e.message}",
                        MessageHelper.getMessage("settings.display.name"),
                    )
                }
            }
        }

        return button
    }
}
