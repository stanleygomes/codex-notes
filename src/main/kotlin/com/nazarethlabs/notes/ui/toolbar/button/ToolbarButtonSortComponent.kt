package com.nazarethlabs.notes.ui.toolbar.button

import com.intellij.icons.AllIcons.ObjectBrowser.Sorted
import com.nazarethlabs.notes.helper.MessageHelper.getMessage
import com.nazarethlabs.notes.ui.component.ButtonComponent
import com.nazarethlabs.notes.ui.popup.sort.NoteSortPopupComponent
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
