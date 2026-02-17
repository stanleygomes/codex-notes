package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.OpenNoteFileLocationService
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenuItem

class MenuItemOpenFileLocationComponent {
    private val openNoteFileLocationService = OpenNoteFileLocationService()
    private val menuItemComponent = MenuItemComponent()

    fun build(notes: List<Note>): JMenuItem {
        val isSingleNote = notes.size == 1

        val menuItem =
            menuItemComponent.build(
                text = MyBundle.message("note.context.menu.open.file.location"),
                action = { if (isSingleNote) openNoteFileLocationService.openFileLocation(notes.first()) },
            )

        menuItem.isEnabled = isSingleNote

        return menuItem
    }
}
