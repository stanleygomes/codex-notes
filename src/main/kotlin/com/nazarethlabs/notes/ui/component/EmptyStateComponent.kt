package com.nazarethlabs.notes.ui.component

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
        label.font = label.font.deriveFont(Font.BOLD)
        label.foreground = JBColor.GRAY

        return JPanel(BorderLayout()).apply {
            add(label, BorderLayout.CENTER)
        }
    }
}
