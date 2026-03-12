package com.nazarethlabs.codex.ui

import com.intellij.ui.components.JBPanel
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.NORTH
import javax.swing.BoxLayout
import javax.swing.BoxLayout.Y_AXIS
import javax.swing.JComponent

class WindowContainerComponent {
    fun build(
        toolbar: JComponent,
        searchPanel: JComponent,
        notesList: JComponent,
    ): JBPanel<JBPanel<*>> {
        val topPanel =
            JBPanel<JBPanel<*>>().apply {
                layout = BoxLayout(this, Y_AXIS)
                add(toolbar)
                add(searchPanel)
            }

        return JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()

            add(topPanel, NORTH)
            add(notesList, CENTER)
        }
    }
}
