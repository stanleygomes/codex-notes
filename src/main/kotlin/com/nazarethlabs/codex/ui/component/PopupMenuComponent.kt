package com.nazarethlabs.codex.ui.component

import com.intellij.util.ui.JBUI
import javax.swing.JPopupMenu

class PopupMenuComponent {
    fun build(): JPopupMenu =
        JPopupMenu().apply {
            border = JBUI.Borders.empty(5)
        }
}
