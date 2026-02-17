package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.OpenNoteService
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenuItem

class MenuItemOpenComponent {
    private val openNoteService = OpenNoteService()
    private val menuItemComponent = MenuItemComponent()

    fun build(
        project: Project,
        note: Note,
    ): JMenuItem =
        menuItemComponent.build(
            text = "${MyBundle.message("note.context.menu.open")} (Double-click)",
            action = { openNoteService.open(project, note) },
        )
}
