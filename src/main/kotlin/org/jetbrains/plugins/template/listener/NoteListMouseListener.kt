package org.jetbrains.plugins.template.listener

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.service.NoteService
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class NoteListMouseListener(
    private val project: Project,
    private val getSelectedValue: () -> Note?
) : MouseAdapter() {

    private val noteService = NoteService()

    override fun mouseClicked(e: MouseEvent) {
        if (e.clickCount == 2) {
            val selectedNote = getSelectedValue() ?: return
            noteService.openNote(project, selectedNote)
        }
    }
}
