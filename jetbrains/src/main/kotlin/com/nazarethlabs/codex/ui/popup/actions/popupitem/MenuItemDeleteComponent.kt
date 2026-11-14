package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.DeleteNotesService
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenuItem

class MenuItemDeleteComponent {
    private val deleteNotesService = DeleteNotesService()
    private val menuItemComponent = MenuItemComponent()

    fun build(
        project: Project,
        notes: List<Note>,
    ): JMenuItem {
        val action = { deleteNotesService.confirmAndDelete(project, notes) }

        return menuItemComponent.build(
            text = "${MyBundle.message("note.context.menu.delete")} (Delete)",
            action = action,
        )
    }
}
