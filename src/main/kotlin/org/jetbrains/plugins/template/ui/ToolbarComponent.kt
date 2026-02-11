package org.jetbrains.plugins.template.ui

import com.intellij.icons.AllIcons.Actions.AddFile
import com.intellij.ui.components.JBPanel
import org.jetbrains.plugins.template.helper.MessageHelper.getMessage
import java.awt.FlowLayout
import java.awt.FlowLayout.LEFT

class ToolbarComponent {

    fun build(): JBPanel<JBPanel<*>> {
        return JBPanel<JBPanel<*>>().apply {
            layout = FlowLayout(LEFT)

            val editButton = ToolbarButtonComponent().build(AddFile, getMessage("toolbar.note.create"))
            add(editButton)
        }
    }
}
