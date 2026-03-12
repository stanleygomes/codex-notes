package com.nazarethlabs.codex.listener

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.DeleteNotesService
import com.nazarethlabs.codex.service.note.FavoriteNotesService
import com.nazarethlabs.codex.service.note.OpenNotesService
import com.nazarethlabs.codex.service.note.RenameNoteService
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_DELETE
import java.awt.event.KeyEvent.VK_ENTER
import java.awt.event.KeyEvent.VK_F
import java.awt.event.KeyEvent.VK_F2

class NoteListKeyListener(
    private val project: Project,
    private val getSelectedValues: () -> List<Note>,
) : KeyAdapter() {
    override fun keyPressed(e: KeyEvent) {
        val selectedNotes = getSelectedValues()
        if (selectedNotes.isEmpty()) return

        when (e.keyCode) {
            VK_ENTER -> handleOpen(selectedNotes)
            VK_DELETE -> handleDelete(selectedNotes)
            VK_F2 -> handleRename(selectedNotes)
            VK_F -> handleFavorite(selectedNotes)
        }
    }

    private fun handleOpen(notes: List<Note>) {
        OpenNotesService().openAll(project, notes)
    }

    private fun handleDelete(notes: List<Note>) {
        DeleteNotesService().confirmAndDelete(project, notes)
    }

    private fun handleRename(notes: List<Note>) {
        if (notes.size == 1) {
            RenameNoteService().rename(project, notes.first())
        }
    }

    private fun handleFavorite(notes: List<Note>) {
        if (notes.size == 1) {
            FavoriteNotesService().toggleFavorite(notes.first())
        } else {
            val allFavorited = notes.all { it.isFavorite }
            if (allFavorited) {
                FavoriteNotesService().unfavoriteAll(notes)
            } else {
                FavoriteNotesService().favoriteAll(notes)
            }
        }
    }
}
