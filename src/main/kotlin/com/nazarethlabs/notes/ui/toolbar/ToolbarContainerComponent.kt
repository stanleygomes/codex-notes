package com.nazarethlabs.notes.ui.toolbar

import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.BorderLayout.EAST
import java.awt.BorderLayout.WEST
import javax.swing.JComponent

class ToolbarContainerComponent {
    fun build(left: JComponent, right: JComponent): JBPanel<JBPanel<*>> =
        JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()
            border = JBUI.Borders.empty(8, 10)

            add(left, WEST)
            add(right, EAST)
        }
}
