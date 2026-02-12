package com.nazarethlabs.notes.ui.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.helper.FileExtensionHelper
import com.nazarethlabs.notes.helper.MessageHelper
import com.nazarethlabs.notes.repository.NotesSettingsRepository
import javax.swing.JComponent
import javax.swing.JPanel
import java.awt.BorderLayout

class NotesConfigurable : Configurable {

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
            add(formPanel, BorderLayout.NORTH)
        }

        return mainPanel!!
    }

    override fun isModified(): Boolean {
        val project = ProjectManager.getInstance().openProjects.firstOrNull() ?: return false
        val settings = NotesSettingsRepository.getInstance(project)
        val currentExtension = FileExtensionHelper.normalizeExtension(fileExtensionField?.text ?: "")
        return currentExtension != settings.getDefaultFileExtension()
    }

    override fun apply() {
        val project = ProjectManager.getInstance().openProjects.firstOrNull() ?: return
        val settings = NotesSettingsRepository.getInstance(project)
        settings.setDefaultFileExtension(FileExtensionHelper.normalizeExtension(fileExtensionField?.text ?: ""))
    }

    override fun reset() {
        val project = ProjectManager.getInstance().openProjects.firstOrNull() ?: return
        val settings = NotesSettingsRepository.getInstance(project)
        fileExtensionField?.text = settings.getDefaultFileExtension()
    }

    override fun disposeUIResources() {
        mainPanel = null
        fileExtensionField = null
    }
}
