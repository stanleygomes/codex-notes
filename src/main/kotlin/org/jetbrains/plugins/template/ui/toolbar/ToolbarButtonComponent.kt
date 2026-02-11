package org.jetbrains.plugins.template.ui.toolbar

import javax.swing.Icon
import javax.swing.JButton

class ToolbarButtonComponent {

    fun build(icon: Icon, tooltip: String? = null): JButton {
        return JButton(icon).apply {
            if (tooltip != null) toolTipText = tooltip
        }
    }
}
