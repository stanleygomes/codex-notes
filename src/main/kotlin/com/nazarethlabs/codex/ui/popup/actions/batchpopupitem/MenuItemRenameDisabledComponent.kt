package com.nazarethlabs.codex.ui.popup.actions.batchpopupitem

import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.helper.MessageHelper
import javax.swing.JMenuItem

class MenuItemRenameDisabledComponent {
    fun build(): JMenuItem {
        val renameItem =
            JMenuItem(
                MessageHelper.getMessage("note.context.menu.rename.disabled"),
                AllIcons.Actions.Edit,
            )

        renameItem.border = JBUI.Borders.empty(5, 10, 5, 10)
        renameItem.isEnabled = false

        return renameItem
    }
}
