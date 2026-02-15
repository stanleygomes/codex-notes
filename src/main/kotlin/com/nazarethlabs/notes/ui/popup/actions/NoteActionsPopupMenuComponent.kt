package com.nazarethlabs.notes.ui.popup.actions

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.ui.component.PopupMenuComponent
import com.nazarethlabs.notes.ui.popup.actions.popupitem.MenuItemColorComponent
import com.nazarethlabs.notes.ui.popup.actions.popupitem.MenuItemDeleteComponent
import com.nazarethlabs.notes.ui.popup.actions.popupitem.MenuItemFavoriteComponent
import com.nazarethlabs.notes.ui.popup.actions.popupitem.MenuItemOpenComponent
import com.nazarethlabs.notes.ui.popup.actions.popupitem.MenuItemRenameComponent
import javax.swing.JPopupMenu

class NoteActionsPopupMenuComponent {
    fun createPopupMenu(
        project: Project,
        note: Note,
    ): JPopupMenu {
        val menu =
            PopupMenuComponent()
                .build()

        val openItem = MenuItemOpenComponent().build(project, note)
        menu.add(openItem)

        menu.addSeparator()

        val renameItem = MenuItemRenameComponent().build(project, note)
        menu.add(renameItem)

        val favoriteItem = MenuItemFavoriteComponent().build(note)
        menu.add(favoriteItem)

        val colorMenu = MenuItemColorComponent().build(note)
        menu.add(colorMenu)

        menu.addSeparator()

        val deleteItem = MenuItemDeleteComponent().build(project, note)
        menu.add(deleteItem)

        return menu
    }
}
