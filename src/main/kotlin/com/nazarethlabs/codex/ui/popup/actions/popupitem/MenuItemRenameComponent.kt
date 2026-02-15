package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.RenameNoteService
import javax.swing.JMenuItem

class MenuItemRenameComponent {
    private val renameNoteService = RenameNoteService()

    fun build(
        project: Project,
        note: Note,
    ): JMenuItem {
        val renameItem = JMenuItem("${MyBundle.message("note.context.menu.rename")} (F2)", AllIcons.Actions.Edit)
        renameItem.border = JBUI.Borders.empty(5, 10, 5, 10)

        renameItem.addActionListener {
            renameNoteService.rename(project, note)
        }

        return renameItem
    }
}
