package com.nazarethlabs.codex.ui.toolbar.button

import com.intellij.icons.AllIcons.Actions.Find
import com.nazarethlabs.codex.helper.MessageHelper.getMessage
import com.nazarethlabs.codex.state.SearchStateManager
import com.nazarethlabs.codex.ui.component.ButtonComponent
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
