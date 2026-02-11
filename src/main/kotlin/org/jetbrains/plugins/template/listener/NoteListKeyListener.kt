package org.jetbrains.plugins.template.listener

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.service.DeleteNoteService
import org.jetbrains.plugins.template.service.FavoriteNoteService
import org.jetbrains.plugins.template.service.OpenNoteService
import org.jetbrains.plugins.template.service.RenameNoteService
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_DELETE
import java.awt.event.KeyEvent.VK_ENTER
import java.awt.event.KeyEvent.VK_F
import java.awt.event.KeyEvent.VK_F2

class NoteListKeyListener(
    private val project: Project,
    private val getSelectedValue: () -> Note?
) : KeyAdapter() {

    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == VK_ENTER) {
            val selectedNote = getSelectedValue() ?: return
            OpenNoteService()
                .open(project, selectedNote)
        }

        if (e.keyCode == VK_DELETE) {
            val selectedNote = getSelectedValue() ?: return
            DeleteNoteService()
                .confirmAndDelete(project, selectedNote)
        }

        if (e.keyCode == VK_F2) {
            val selectedNote = getSelectedValue() ?: return
            RenameNoteService()
                .rename(project, selectedNote)
        }

        if (e.keyCode == VK_F) {
            val selectedNote = getSelectedValue() ?: return
            FavoriteNoteService()
                .toggleFavorite(project, selectedNote)
        }
    }
}
