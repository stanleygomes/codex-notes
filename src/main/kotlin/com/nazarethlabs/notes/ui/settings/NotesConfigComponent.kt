package com.nazarethlabs.notes.ui.settings

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.helper.FileExtensionHelper
import com.nazarethlabs.notes.helper.MessageHelper
import com.nazarethlabs.notes.service.NotesSettingsService
import java.awt.BorderLayout
import java.awt.BorderLayout.NORTH
import javax.swing.JComponent
import javax.swing.JPanel

class NotesConfigComponent : Configurable {

    private var mainPanel: JPanel? = null
    private var fileExtensionField: JBTextField? = null

    override fun getDisplayName(): String {
        return MessageHelper.getMessage("settings.display.name")
    }

    override fun createComponent(): JComponent {
        fileExtensionField = JBTextField()

        val descriptionLabel = JBLabel(MessageHelper.getMessage("settings.file.extension.description"))

        val formPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                MessageHelper.getMessage("settings.file.extension.label"),
                fileExtensionField!!,
                1,
                false
            )
            .addComponentToRightColumn(descriptionLabel, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        mainPanel = JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(10)
            add(formPanel, NORTH)
        }

        return mainPanel!!
    }

    override fun isModified(): Boolean {
        val settings = NotesSettingsService()
        val currentExtension = FileExtensionHelper.normalizeExtension(fileExtensionField?.text ?: "")
        return currentExtension != settings.getDefaultFileExtension()
    }

    override fun apply() {
        val settings = NotesSettingsService()
        settings.setDefaultFileExtension(FileExtensionHelper.normalizeExtension(fileExtensionField?.text ?: ""))
    }

    override fun reset() {
        val settings = NotesSettingsService()
        fileExtensionField?.text = settings.getDefaultFileExtension()
    }

    override fun disposeUIResources() {
        mainPanel = null
        fileExtensionField = null
    }
}
