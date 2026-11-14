package com.nazarethlabs.codex.ui.component

import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.JMenuItem

class MenuItemComponent {
    fun build(
        text: String,
        icon: Icon? = null,
        action: () -> Unit,
    ): JMenuItem {
        val item = JMenuItem(text)
        if (icon != null) {
            item.icon = icon
        }
        item.border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
        item.addActionListener { action() }
        return item
    }
}
