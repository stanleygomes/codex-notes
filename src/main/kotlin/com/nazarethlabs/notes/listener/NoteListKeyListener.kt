package com.nazarethlabs.notes.listener

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.service.note.DeleteNoteService
import com.nazarethlabs.notes.service.note.FavoriteNoteService
import com.nazarethlabs.notes.service.note.OpenNoteService
import com.nazarethlabs.notes.service.note.RenameNoteService
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_DELETE
import java.awt.event.KeyEvent.VK_ENTER
import java.awt.event.KeyEvent.VK_F
import java.awt.event.KeyEvent.VK_F2

class NoteListKeyListener(
    private val project: Project,
    private val getSelectedValue: () -> Note?,
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
                .toggleFavorite(selectedNote)
        }
    }
}
