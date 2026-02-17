package com.nazarethlabs.codex.ui.toolbar.button

import com.intellij.icons.AllIcons.Actions.More
import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.helper.MessageHelper.getMessage
import com.nazarethlabs.codex.state.SelectedNoteStateManager
import com.nazarethlabs.codex.ui.component.ButtonComponent
import com.nazarethlabs.codex.ui.popup.actions.NoteActionsPopupMenuComponent
import javax.swing.JButton

class ToolbarButtonNoteActionsComponent {
    private val menuFactory = NoteActionsPopupMenuComponent()

    fun build(project: Project): JButton {
        val actionsButton =
            ButtonComponent()
                .build(More, getMessage("toolbar.note.actions"))

        actionsButton.addActionListener { event ->
            val selectedNotes = SelectedNoteStateManager.getInstance().getSelectedNotes()
            if (selectedNotes.isEmpty()) return@addActionListener

            val menu = menuFactory.createPopupMenu(project, selectedNotes)

            val component = event.source as JButton
            menu.show(component, 0, component.height)
        }

        return actionsButton
    }
}
