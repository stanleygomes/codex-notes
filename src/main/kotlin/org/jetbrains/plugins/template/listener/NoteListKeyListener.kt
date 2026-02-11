package org.jetbrains.plugins.template.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.Messages.YES
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.helper.MessageHelper
import org.jetbrains.plugins.template.service.NoteStorageService
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_DELETE
import java.awt.event.KeyEvent.VK_F2
import java.io.File

class NoteListKeyListener(
    private val project: Project,
    private val getSelectedValue: () -> Note?
) : KeyAdapter() {

    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == VK_DELETE) {
            val selectedNote = getSelectedValue() ?: return
            confirmAndDeleteNote(selectedNote)
        }

        if (e.keyCode == VK_F2) {
            val selectedNote = getSelectedValue() ?: return
            renameNote(selectedNote)
        }
    }

    private fun confirmAndDeleteNote(note: Note) {
        val result = Messages.showYesNoDialog(
            project,
            MessageHelper.getMessage("dialog.delete.note.message", note.title),
            MessageHelper.getMessage("dialog.delete.note.title"),
            Messages.getQuestionIcon()
        )

        if (result == YES) {
            deleteNote(note)
        }
    }

    private fun deleteNote(note: Note) {
        val file = File(note.filePath)
        if (file.exists()) {
            file.delete()
        }
        NoteStorageService
            .getInstance(project)
            .removeNote(project, note.id)
    }

    private fun renameNote(note: Note) {
        val oldFile = File(note.filePath)
        var newTitle: String?
        do {
            newTitle = Messages.showInputDialog(
                project,
                MessageHelper.getMessage("dialog.rename.note.message"),
                MessageHelper.getMessage("dialog.rename.note.title"),
                Messages.getQuestionIcon(),
                note.title,
                null
            )

            if (newTitle.isNullOrBlank() || newTitle == note.title) {
                return
            }

            val newFile = File(oldFile.parent, newTitle)
            if (newFile.exists()) {
                Messages
                    .showWarningDialog(project, MessageHelper.getMessage("dialog.rename.error.exists", newTitle), MessageHelper.getMessage("dialog.rename.error.title"))
            } else {
                break
            }
        } while (true)

        if (oldFile.exists() && oldFile.renameTo(File(oldFile.parent, newTitle))) {
            note.title = newTitle
            note.filePath = File(oldFile.parent, newTitle).absolutePath
            NoteStorageService.getInstance(project).updateNote(project, note.id, newTitle)
        } else {
            Messages.showErrorDialog(project, MessageHelper.getMessage("dialog.rename.error.failed"), MessageHelper.getMessage("dialog.rename.error.title"))
        }
    }
}
