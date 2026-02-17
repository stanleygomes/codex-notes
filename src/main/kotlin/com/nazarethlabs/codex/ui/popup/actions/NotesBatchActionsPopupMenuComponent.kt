package com.nazarethlabs.codex.ui.popup.actions

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.ui.component.PopupMenuComponent
import com.nazarethlabs.codex.ui.popup.actions.batchpopupitem.MenuItemBatchColorComponent
import com.nazarethlabs.codex.ui.popup.actions.batchpopupitem.MenuItemBatchDeleteComponent
import com.nazarethlabs.codex.ui.popup.actions.batchpopupitem.MenuItemBatchFavoriteComponent
import com.nazarethlabs.codex.ui.popup.actions.batchpopupitem.MenuItemBatchOpenComponent
import com.nazarethlabs.codex.ui.popup.actions.batchpopupitem.MenuItemRenameDisabledComponent
import javax.swing.JPopupMenu

class NotesBatchActionsPopupMenuComponent {
    fun createPopupMenu(
        project: Project,
        notes: List<Note>,
    ): JPopupMenu {
        val menu =
            PopupMenuComponent()
                .build()

        val openItem = MenuItemBatchOpenComponent().build(project, notes)
        menu.add(openItem)

        menu.addSeparator()

        val renameItem = MenuItemRenameDisabledComponent().build()
        menu.add(renameItem)

        val favoriteMenu = MenuItemBatchFavoriteComponent().build(notes)
        menu.add(favoriteMenu)

        val colorMenu = MenuItemBatchColorComponent().build(notes)
        menu.add(colorMenu)

        menu.addSeparator()

        val deleteItem = MenuItemBatchDeleteComponent().build(project, notes)
        menu.add(deleteItem)

        return menu
    }
}
