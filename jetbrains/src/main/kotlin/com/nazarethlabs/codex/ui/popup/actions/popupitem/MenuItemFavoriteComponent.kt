package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.icons.AllIcons
import com.nazarethlabs.codex.MyBundle.message
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.FavoriteNotesService
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenuItem

class MenuItemFavoriteComponent {
    private val favoriteNotesService = FavoriteNotesService()
    private val menuItemComponent = MenuItemComponent()

    fun build(notes: List<Note>): JMenuItem {
        val isSingleNote = notes.size == 1

        val favoriteText =
            if (isSingleNote) {
                if (notes.first().isFavorite) {
                    "${message("note.context.menu.unfavorite")} (F)"
                } else {
                    "${message("note.context.menu.favorite")} (F)"
                }
            } else {
                "${message("note.context.menu.favorite")} (F)"
            }

        val favoriteIcon = AllIcons.Nodes.BookmarkGroup

        return menuItemComponent.build(
            text = favoriteText,
            icon = favoriteIcon,
            action = { notes.forEach { favoriteNotesService.toggleFavorite(it) } },
        )
    }
}
