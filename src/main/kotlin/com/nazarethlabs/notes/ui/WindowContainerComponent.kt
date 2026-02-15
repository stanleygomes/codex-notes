package com.nazarethlabs.notes.ui

import com.intellij.ui.components.JBPanel
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.NORTH
import javax.swing.BoxLayout
import javax.swing.BoxLayout.Y_AXIS
import javax.swing.JComponent
import javax.swing.JPanel

class WindowContainerComponent {
    fun build(
        toolbar: JComponent,
        searchPanel: JComponent,
        notesList: JComponent,
    ): JBPanel<JBPanel<*>> {
        val topPanel =
            JPanel().apply {
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
