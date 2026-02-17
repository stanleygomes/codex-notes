package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.RenameNoteService
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenuItem

class MenuItemRenameComponent {
    private val renameNoteService = RenameNoteService()
    private val menuItemComponent = MenuItemComponent()

    fun build(
        project: Project,
        note: Note,
    ): JMenuItem =
        menuItemComponent.build(
            text = "${MyBundle.message("note.context.menu.rename")} (F2)",
            icon = AllIcons.Actions.Edit,
            action = { renameNoteService.rename(project, note) },
        )
}
