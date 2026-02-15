package com.nazarethlabs.notes.ui.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.nazarethlabs.notes.helper.MessageHelper
import javax.swing.JPanel

class NotesConfigFormComponent {
    private var fileExtensionField: JBTextField? = null

    fun build(): JPanel {
        fileExtensionField = JBTextField()

        val descriptionLabel = JBLabel(MessageHelper.getMessage("settings.file.extension.description"))

        return FormBuilder
            .createFormBuilder()
            .addLabeledComponent(
                MessageHelper.getMessage("settings.file.extension.label"),
                fileExtensionField!!,
                1,
                false,
            ).addComponentToRightColumn(descriptionLabel, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getFileExtensionField(): JBTextField? = fileExtensionField
}
