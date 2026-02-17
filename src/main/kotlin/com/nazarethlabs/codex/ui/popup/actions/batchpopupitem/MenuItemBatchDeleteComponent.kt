package com.nazarethlabs.codex.ui.popup.actions.batchpopupitem

import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.service.note.DeleteNotesService
import javax.swing.JMenuItem

class MenuItemBatchDeleteComponent {
    private val deleteNotesService = DeleteNotesService()

    fun build(
        project: Project,
        notes: List<Note>,
    ): JMenuItem {
        val deleteItem =
            JMenuItem(
                MessageHelper.getMessage(
                    "note.context.menu.delete.all",
                    notes.size.toString(),
                ),
            )

        deleteItem.border = JBUI.Borders.empty(5, 10, 5, 10)
        deleteItem.addActionListener {
            deleteNotesService.confirmAndDelete(project, notes)
        }

        return deleteItem
    }
}
