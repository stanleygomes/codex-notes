package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.OpenNoteFileLocationService
import javax.swing.JMenuItem

class MenuItemOpenFileLocationComponent {
    private val openNoteFileLocationService = OpenNoteFileLocationService()

    fun build(note: Note): JMenuItem {
        val openFileLocationItem = JMenuItem(MyBundle.message("note.context.menu.open.file.location"))

        openFileLocationItem.addActionListener {
            openNoteFileLocationService.openFileLocation(note)
        }

        return openFileLocationItem
    }
}
