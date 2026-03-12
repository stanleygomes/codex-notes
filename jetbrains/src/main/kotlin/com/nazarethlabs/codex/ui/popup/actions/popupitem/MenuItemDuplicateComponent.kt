package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.DuplicateNoteService
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenuItem

class MenuItemDuplicateComponent {
    private val duplicateNoteService = DuplicateNoteService()
    private val menuItemComponent = MenuItemComponent()

    fun build(
        project: Project,
        notes: List<Note>,
    ): JMenuItem {
        val isSingleNote = notes.size == 1

        val menuItem =
            menuItemComponent.build(
                text = MyBundle.message("note.context.menu.duplicate"),
                icon = AllIcons.Actions.Copy,
                action = { if (isSingleNote) duplicateNoteService.duplicate(project, notes.first()) },
            )

        menuItem.isEnabled = isSingleNote

        return menuItem
    }
}
