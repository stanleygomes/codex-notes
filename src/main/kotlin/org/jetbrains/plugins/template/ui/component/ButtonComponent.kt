package org.jetbrains.plugins.template.ui.component

import javax.swing.Icon
import javax.swing.JButton

class ButtonComponent {

    fun build(icon: Icon, tooltip: String? = null): JButton {
        return JButton(icon).apply {
            if (tooltip != null) toolTipText = tooltip
        }
    }
}
