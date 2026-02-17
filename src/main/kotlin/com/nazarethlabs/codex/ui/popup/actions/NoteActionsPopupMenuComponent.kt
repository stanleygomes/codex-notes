package com.nazarethlabs.codex.ui.popup.actions

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.ui.component.PopupMenuComponent
import com.nazarethlabs.codex.ui.popup.actions.popupitem.MenuItemColorComponent
import com.nazarethlabs.codex.ui.popup.actions.popupitem.MenuItemDeleteComponent
import com.nazarethlabs.codex.ui.popup.actions.popupitem.MenuItemDuplicateComponent
import com.nazarethlabs.codex.ui.popup.actions.popupitem.MenuItemFavoriteComponent
import com.nazarethlabs.codex.ui.popup.actions.popupitem.MenuItemOpenComponent
import com.nazarethlabs.codex.ui.popup.actions.popupitem.MenuItemOpenFileLocationComponent
import com.nazarethlabs.codex.ui.popup.actions.popupitem.MenuItemRenameComponent
import javax.swing.JPopupMenu

class NoteActionsPopupMenuComponent {
    fun createPopupMenu(
        project: Project,
        notes: List<Note>,
    ): JPopupMenu {
        val menu =
            PopupMenuComponent()
                .build()

        val openItem = MenuItemOpenComponent().build(project, notes)
        menu.add(openItem)

        menu.addSeparator()

        val renameItem = MenuItemRenameComponent().build(project, notes)
        menu.add(renameItem)

        val duplicateItem = MenuItemDuplicateComponent().build(project, notes)
        menu.add(duplicateItem)

        val favoriteItem = MenuItemFavoriteComponent().build(notes)
        menu.add(favoriteItem)

        val colorMenu = MenuItemColorComponent().build(notes)
        menu.add(colorMenu)

        menu.addSeparator()

        val openFileLocationItem = MenuItemOpenFileLocationComponent().build(notes)
        menu.add(openFileLocationItem)

        menu.addSeparator()

        val deleteItem = MenuItemDeleteComponent().build(project, notes)
        menu.add(deleteItem)

        return menu
    }
}
