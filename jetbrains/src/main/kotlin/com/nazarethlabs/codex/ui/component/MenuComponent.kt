package com.nazarethlabs.codex.ui.component

import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.JMenu

class MenuComponent {
    fun build(
        text: String,
        icon: Icon? = null,
    ): JMenu {
        val menu = JMenu(text)
        if (icon != null) {
            menu.icon = icon
        }
        menu.border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
        return menu
    }
}
