package com.nazarethlabs.notes.ui.menu

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import javax.swing.JPopupMenu

class NoteContextMenuFactory {
    private val menuItemsFactory = NoteMenuItemsFactory()

    fun create(
        project: Project,
        note: Note,
    ): JPopupMenu = menuItemsFactory.createPopupMenu(project, note)
}
