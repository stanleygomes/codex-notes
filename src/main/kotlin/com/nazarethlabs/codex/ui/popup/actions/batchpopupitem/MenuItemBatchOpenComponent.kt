package com.nazarethlabs.codex.ui.popup.actions.batchpopupitem

import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.service.note.OpenNotesService
import javax.swing.JMenuItem

class MenuItemBatchOpenComponent {
    private val openNotesService = OpenNotesService()

    fun build(
        project: Project,
        notes: List<Note>,
    ): JMenuItem {
        val openItem =
            JMenuItem(
                MessageHelper.getMessage(
                    "note.context.menu.open.all",
                    notes.size.toString(),
                ),
            )

        openItem.border = JBUI.Borders.empty(5, 10, 5, 10)
        openItem.addActionListener {
            openNotesService.openAll(project, notes)
        }

        return openItem
    }
}
