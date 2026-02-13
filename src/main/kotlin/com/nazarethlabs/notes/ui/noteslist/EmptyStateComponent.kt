package com.nazarethlabs.notes.ui.noteslist

import com.intellij.ui.JBColor
import com.nazarethlabs.notes.MyBundle
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class EmptyStateComponent {
    fun build(): JPanel {
        val label = JLabel(MyBundle.message("empty.state.message"))
        label.horizontalAlignment = SwingConstants.CENTER
        label.font = Font(label.font.family, Font.BOLD, 16)
        label.foreground = JBColor.GRAY

        return JPanel(BorderLayout()).apply {
            add(label, BorderLayout.CENTER)
        }
    }
}
