package com.nazarethlabs.codex.ui.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.ui.settings.component.NotesConfigExportNotesPanelComponent
import com.nazarethlabs.codex.ui.settings.component.NotesConfigOpenFolderPanelComponent
import javax.swing.JPanel

class NotesConfigFormComponent {
    private var fileExtensionField: JBTextField? = null
    private var notesDirectoryField: JBTextField? = null
    private val openFolderPanelComponent = NotesConfigOpenFolderPanelComponent()
    private val exportNotesPanelComponent = NotesConfigExportNotesPanelComponent()

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
            .addComponent(JPanel().apply { preferredSize = java.awt.Dimension(0, 10) })
            .addLabeledComponent(
                MessageHelper.getMessage("settings.notes.directory.label"),
                notesDirectoryField!!,
                1,
                false,
            ).addComponentToRightColumn(notesDirectoryDescriptionLabel, 1)
            .addComponent(JPanel().apply { preferredSize = java.awt.Dimension(0, 10) })
            .addLabeledComponent("", openFolderPanelComponent.createOpenFolderPanel(), 1, false)
            .addComponent(JPanel().apply { preferredSize = java.awt.Dimension(0, 10) })
            .addLabeledComponent("", exportNotesPanelComponent.createExportNotesPanel(), 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getFileExtensionField(): JBTextField? = fileExtensionField

    fun getNotesDirectoryField(): JBTextField? = notesDirectoryField
}
