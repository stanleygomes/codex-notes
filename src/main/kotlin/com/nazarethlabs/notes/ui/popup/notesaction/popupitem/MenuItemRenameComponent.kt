package com.nazarethlabs.notes.ui.popup.notesaction.popupitem

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.MyBundle
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.service.note.RenameNoteService
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
