package com.nazarethlabs.codex.ui.toolbar.button

import com.intellij.icons.AllIcons.ObjectBrowser.Sorted
import com.nazarethlabs.codex.helper.MessageHelper.getMessage
import com.nazarethlabs.codex.ui.component.ButtonComponent
import com.nazarethlabs.codex.ui.popup.sort.NoteSortPopupComponent
import javax.swing.JButton

class ToolbarButtonSortComponent {
    fun build(): JButton {
        val sortButton =
            ButtonComponent()
                .build(Sorted, getMessage("toolbar.note.sort"))

        sortButton.addActionListener { event ->
            val menu =
                NoteSortPopupComponent()
                    .createPopupMenu()
            val component = event.source as JButton

            menu.show(component, 0, component.height)
        }

        return sortButton
    }
}
