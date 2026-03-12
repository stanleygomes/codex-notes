package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.service.note.OpenNotesService
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenuItem

class MenuItemOpenComponent {
    private val openNotesService = OpenNotesService()
    private val menuItemComponent = MenuItemComponent()

    fun build(
        project: Project,
        notes: List<Note>,
    ): JMenuItem {
        val isSingleNote = notes.size == 1

        val text =
            if (isSingleNote) {
                "${MyBundle.message("note.context.menu.open")} (Double-click)"
            } else {
                MessageHelper.getMessage("note.context.menu.open.all", notes.size.toString())
            }

        val action = { openNotesService.openAll(project, notes) }

        return menuItemComponent.build(text = text, action = action)
    }
}
