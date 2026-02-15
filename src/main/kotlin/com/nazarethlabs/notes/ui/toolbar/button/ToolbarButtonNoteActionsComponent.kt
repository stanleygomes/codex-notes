package com.nazarethlabs.notes.ui.toolbar.button

import com.intellij.icons.AllIcons.Actions.More
import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.helper.MessageHelper.getMessage
import com.nazarethlabs.notes.ui.component.ButtonComponent
import com.nazarethlabs.notes.ui.popup.notesaction.NoteActionsPopupMenuComponent
import javax.swing.JButton

class ToolbarButtonNoteActionsComponent {
    private val menuItemsFactory = NoteActionsPopupMenuComponent()

    fun build(
        project: Project,
        getSelectedNote: () -> Note?,
    ): JButton {
        val actionsButton =
            ButtonComponent()
                .build(More, getMessage("toolbar.note.actions"))

        actionsButton.addActionListener { event ->
            val selectedNote = getSelectedNote() ?: return@addActionListener
            val menu = menuItemsFactory.createPopupMenu(project, selectedNote)
            val component = event.source as JButton

            menu.show(component, 0, component.height)
        }

        return actionsButton
    }
}
