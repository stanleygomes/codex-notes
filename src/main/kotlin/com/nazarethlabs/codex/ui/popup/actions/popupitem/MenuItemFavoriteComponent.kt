package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.icons.AllIcons
import com.nazarethlabs.codex.MyBundle.message
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.FavoriteNoteService
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenuItem

class MenuItemFavoriteComponent {
    private val favoriteNoteService = FavoriteNoteService()
    private val menuItemComponent = MenuItemComponent()

    fun build(note: Note): JMenuItem {
        val favoriteText =
            if (note.isFavorite) {
                "${message("note.context.menu.unfavorite")} (F)"
            } else {
                "${message("note.context.menu.favorite")} (F)"
            }

        val favoriteIcon = AllIcons.Nodes.BookmarkGroup

        return menuItemComponent.build(
            text = favoriteText,
            icon = favoriteIcon,
            action = { favoriteNoteService.toggleFavorite(note) },
        )
    }
}
