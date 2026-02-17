package com.nazarethlabs.codex.ui.settings

import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.BorderLayout.NORTH
import javax.swing.JComponent
import javax.swing.JPanel

class NotesConfigComponent {
    private var fileExtensionField: JBTextField? = null
    private var notesDirectoryField: JBTextField? = null

    fun build(): JComponent {
        val formPanelComponent = NotesConfigFormComponent()
        val formPanel = formPanelComponent.build()
        fileExtensionField = formPanelComponent.getFileExtensionField()
        notesDirectoryField = formPanelComponent.getNotesDirectoryField()

        val mainPanel =
            JPanel(BorderLayout()).apply {
                border = JBUI.Borders.empty(10)
                add(formPanel, NORTH)
            }

        return mainPanel
    }

    fun getFileExtensionField(): JBTextField? = fileExtensionField

    fun getNotesDirectoryField(): JBTextField? = notesDirectoryField
}
