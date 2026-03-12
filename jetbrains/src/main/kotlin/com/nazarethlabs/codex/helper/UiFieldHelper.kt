package com.nazarethlabs.codex.helper

import java.awt.Component
import java.awt.Container
import javax.swing.JPanel
import javax.swing.JTextField

object UiFieldHelper {
    fun focusField(panel: JPanel) {
        findField(panel)?.requestFocusInWindow()
    }

    private fun findField(component: Component): JTextField? =
        when (component) {
            is JTextField -> component
            is Container -> component.components.firstNotNullOfOrNull(::findField)
            else -> null
        }
}
