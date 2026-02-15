package com.nazarethlabs.notes.ui.toolbar.button

import com.intellij.icons.AllIcons.Actions.Find
import com.nazarethlabs.notes.helper.MessageHelper.getMessage
import com.nazarethlabs.notes.state.SearchStateManager
import com.nazarethlabs.notes.ui.component.ButtonComponent
import javax.swing.JButton

class ToolbarButtonSearchComponent {
    fun build(): JButton {
        val searchButton =
            ButtonComponent()
                .build(Find, getMessage("toolbar.note.search"))

        searchButton.addActionListener {
            SearchStateManager.getInstance().toggleSearchVisibility()
        }

        return searchButton
    }
}
