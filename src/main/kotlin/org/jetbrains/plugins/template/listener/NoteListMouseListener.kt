package org.jetbrains.plugins.template.listener

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.service.NoteFileService
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class NoteListMouseListener(
    private val project: Project,
    private val getSelectedValue: () -> Note?
) : MouseAdapter() {

    private val noteFileService = NoteFileService()

    override fun mouseClicked(e: MouseEvent) {
        if (e.clickCount == 2) {
            val selectedNote = getSelectedValue() ?: return
            openNote(selectedNote)
        }
    }

    private fun openNote(note: Note) {
        noteFileService.openNote(project, note)
    }
}
