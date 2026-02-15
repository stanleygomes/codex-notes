package com.nazarethlabs.notes.ui.menu

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import java.awt.event.MouseEvent
import javax.swing.JList

class NoteListContextMenuHandler {

    fun handleContextMenu(e: MouseEvent, project: Project, getSelectedValue: () -> Note?) {
        if (e.isPopupTrigger) {
            val list = e.source as? JList<*> ?: return

            val index = list.locationToIndex(e.point)
            if (index >= 0) {
                list.selectedIndex = index
            }

            val selectedNote = getSelectedValue() ?: return
            val menu =
                NoteContextMenuFactory()
                    .create(project, selectedNote)

            menu.show(e.component, e.x, e.y)
        }
    }
}
