package com.nazarethlabs.notes.listener

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.service.note.OpenNoteService
import com.nazarethlabs.notes.ui.menu.NoteListContextMenuHandler
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities

class NoteListMouseListener(
    private val project: Project,
    private val getSelectedValue: () -> Note?,
) : MouseAdapter() {
    private val contextMenuHandler = NoteListContextMenuHandler()

    override fun mouseClicked(e: MouseEvent) {
        if (e.clickCount == 2 && SwingUtilities.isLeftMouseButton(e)) {
            val selectedNote = getSelectedValue() ?: return
            OpenNoteService().open(project, selectedNote)
        }
    }

    override fun mousePressed(e: MouseEvent) {
        contextMenuHandler.handleContextMenu(e, project, getSelectedValue)
    }

    override fun mouseReleased(e: MouseEvent) {
        contextMenuHandler.handleContextMenu(e, project, getSelectedValue)
    }
}
