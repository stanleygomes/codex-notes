package org.jetbrains.plugins.template.ui.toolbar.button

import com.intellij.icons.AllIcons
import org.jetbrains.plugins.template.helper.MessageHelper
import org.jetbrains.plugins.template.ui.component.ButtonComponent
import org.jetbrains.plugins.template.ui.noteslist.NotesListComponent
import javax.swing.JButton

class ToolbarButtonSearchComponent {

    fun build(notesListComponent: NotesListComponent): JButton {
        val searchButton = ButtonComponent()
            .build(AllIcons.Actions.Find, MessageHelper.getMessage("toolbar.note.search"))

        searchButton.addActionListener {
            notesListComponent.toggleSearch()
        }

        return searchButton
    }
}
