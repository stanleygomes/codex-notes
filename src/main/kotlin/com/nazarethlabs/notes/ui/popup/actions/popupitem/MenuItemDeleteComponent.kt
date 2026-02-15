package com.nazarethlabs.notes.ui.popup.actions.popupitem

import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.MyBundle
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.service.note.DeleteNoteService
import javax.swing.JMenuItem

class MenuItemDeleteComponent {
    private val deleteNoteService = DeleteNoteService()

    fun build(
        project: Project,
        note: Note,
    ): JMenuItem {
        val deleteItem = JMenuItem("${MyBundle.message("note.context.menu.delete")} (Delete)")
        deleteItem.border = JBUI.Borders.empty(5, 10, 5, 10)

        deleteItem.addActionListener {
            deleteNoteService.confirmAndDelete(project, note)
        }

        return deleteItem
    }
}
