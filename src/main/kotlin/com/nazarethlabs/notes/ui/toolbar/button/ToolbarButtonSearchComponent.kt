package com.nazarethlabs.notes.ui.toolbar.button

import com.intellij.icons.AllIcons.Actions.Find
import com.nazarethlabs.notes.helper.MessageHelper
import com.nazarethlabs.notes.helper.MessageHelper.getMessage
import com.nazarethlabs.notes.ui.component.ButtonComponent
import com.nazarethlabs.notes.ui.noteslist.NotesListComponent
import javax.swing.JButton

class ToolbarButtonSearchComponent {
    fun build(notesListComponent: NotesListComponent): JButton {
        val searchButton =
            ButtonComponent()
                .build(Find, getMessage("toolbar.note.search"))

        searchButton.addActionListener {
            notesListComponent.toggleSearch()
        }

        return searchButton
    }
}
