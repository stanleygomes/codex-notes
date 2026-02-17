package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.DeleteNoteService
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenuItem

class MenuItemDeleteComponent {
    private val deleteNoteService = DeleteNoteService()
    private val menuItemComponent = MenuItemComponent()

    fun build(
        project: Project,
        note: Note,
    ): JMenuItem =
        menuItemComponent.build(
            text = "${MyBundle.message("note.context.menu.delete")} (Delete)",
            action = { deleteNoteService.confirmAndDelete(project, note) },
        )
}
