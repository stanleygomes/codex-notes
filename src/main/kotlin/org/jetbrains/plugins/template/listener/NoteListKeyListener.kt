package org.jetbrains.plugins.template.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.helper.MessageHelper
import org.jetbrains.plugins.template.service.NoteStorageService
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.io.File

class NoteListKeyListener(
    private val project: Project,
    private val getSelectedValue: () -> Note?
) : KeyAdapter() {

    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_DELETE) {
            val selectedNote = getSelectedValue() ?: return
            confirmAndDeleteNote(selectedNote)
        }
    }

    private fun confirmAndDeleteNote(note: Note) {
        val result = Messages.showYesNoDialog(
            project,
            MessageHelper.getMessage("dialog.delete.note.message", note.title),
            MessageHelper.getMessage("dialog.delete.note.title"),
            Messages.getQuestionIcon()
        )

        if (result == Messages.YES) {
            deleteNote(note)
        }
    }

    private fun deleteNote(note: Note) {
        val file = File(note.filePath)
        if (file.exists()) {
            file.delete()
        }
        NoteStorageService.getInstance(project).removeNote(project, note.id)
    }
}
