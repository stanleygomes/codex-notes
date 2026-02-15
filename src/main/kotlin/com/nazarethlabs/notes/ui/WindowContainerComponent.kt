package com.nazarethlabs.notes.ui

import com.intellij.ui.components.JBPanel
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.NORTH
import javax.swing.JComponent

class WindowContainerComponent {
    fun build(toolbar: JComponent, notesList: JComponent): JBPanel<JBPanel<*>> {
        return JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()

            add(toolbar, NORTH)
            add(notesList, CENTER)
        }
    }
}
