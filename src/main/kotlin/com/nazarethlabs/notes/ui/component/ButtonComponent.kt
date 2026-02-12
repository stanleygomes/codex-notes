package com.nazarethlabs.notes.ui.component

import javax.swing.Icon
import javax.swing.JButton

class ButtonComponent {

    fun build(icon: Icon, tooltip: String? = null): JButton {
        val button = JButton(icon)

        button.apply {
            if (tooltip != null) toolTipText = tooltip
        }

        return button
    }
}
