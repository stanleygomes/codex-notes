package com.nazarethlabs.notes.ui.popup.actions.popupitem

import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.MyBundle
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.service.note.OpenNoteService
import javax.swing.JMenuItem

class MenuItemOpenComponent {
    private val openNoteService = OpenNoteService()

    fun build(
        project: Project,
        note: Note,
    ): JMenuItem {
        val openItem = JMenuItem("${MyBundle.message("note.context.menu.open")} (Double-click)")

        openItem.border = JBUI.Borders.empty(5, 10, 5, 10)
        openItem.addActionListener {
            openNoteService.open(project, note)
        }

        return openItem
    }
}
