package com.nazarethlabs.notes.listener

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.service.OpenNoteService
import com.nazarethlabs.notes.ui.menu.NoteContextMenuFactory
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities

class NoteListMouseListener(
    private val project: Project,
    private val getSelectedValue: () -> Note?,
) : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) {
        if (e.clickCount == 2 && SwingUtilities.isLeftMouseButton(e)) {
            val selectedNote = getSelectedValue() ?: return
            OpenNoteService().open(project, selectedNote)
        }
    }

    override fun mousePressed(e: MouseEvent) {
        handleContextMenu(e)
    }

    override fun mouseReleased(e: MouseEvent) {
        handleContextMenu(e)
    }

    private fun handleContextMenu(e: MouseEvent) {
        if (e.isPopupTrigger) {
            val selectedNote = getSelectedValue() ?: return
            val menu =
                NoteContextMenuFactory()
                    .create(project, selectedNote)

            menu.show(e.component, e.x, e.y)
        }
    }
}
