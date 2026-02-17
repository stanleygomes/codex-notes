package com.nazarethlabs.codex.listener

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.OpenNoteService
import com.nazarethlabs.codex.service.note.OpenNotesService
import com.nazarethlabs.codex.ui.popup.actions.NoteActionsPopupMenuComponent
import com.nazarethlabs.codex.ui.popup.actions.NotesBatchActionsPopupMenuComponent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JList
import javax.swing.SwingUtilities

class NoteListMouseListener(
    private val project: Project,
    private val getSelectedValues: () -> List<Note>,
) : MouseAdapter() {
    private val singleMenuFactory = NoteActionsPopupMenuComponent()
    private val batchMenuFactory = NotesBatchActionsPopupMenuComponent()

    override fun mouseClicked(e: MouseEvent) {
        if (e.clickCount == 2 && SwingUtilities.isLeftMouseButton(e)) {
            val selectedNotes = getSelectedValues()
            if (selectedNotes.isEmpty()) return

            if (selectedNotes.size == 1) {
                OpenNoteService().open(project, selectedNotes.first())
            } else {
                OpenNotesService().openAll(project, selectedNotes)
            }
        }
    }

    override fun mousePressed(e: MouseEvent) {
        handleContextMenu(e, project, getSelectedValues)
    }

    override fun mouseReleased(e: MouseEvent) {
        handleContextMenu(e, project, getSelectedValues)
    }

    private fun handleContextMenu(
        e: MouseEvent,
        project: Project,
        getSelectedValues: () -> List<Note>,
    ) {
        if (e.isPopupTrigger) {
            val list = e.source as? JList<*> ?: return

            val index = list.locationToIndex(e.point)
            if (index >= 0 && !list.isSelectedIndex(index)) {
                list.selectedIndex = index
            }

            val selectedNotes = getSelectedValues()
            if (selectedNotes.isEmpty()) return

            val menu =
                if (selectedNotes.size == 1) {
                    singleMenuFactory.createPopupMenu(project, selectedNotes.first())
                } else {
                    batchMenuFactory.createPopupMenu(project, selectedNotes)
                }

            menu.show(e.component, e.x, e.y)
        }
    }
}
