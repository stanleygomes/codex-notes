package com.nazarethlabs.codex.ui.component

import com.intellij.ui.JBColor.GRAY
import com.nazarethlabs.codex.MyBundle
import java.awt.BorderLayout
import java.awt.Font.BOLD
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class EmptyStateComponent {
    fun build(): JPanel {
        val label = JLabel(MyBundle.message("empty.state.message"))
        label.horizontalAlignment = SwingConstants.CENTER
        label.font = label.font.deriveFont(BOLD)
        label.foreground = GRAY

        return JPanel(BorderLayout()).apply {
            add(label, BorderLayout.CENTER)
        }
    }
}
