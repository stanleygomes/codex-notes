package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.MyBundle.message
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.service.note.FavoriteNoteService
import javax.swing.JMenuItem

class MenuItemFavoriteComponent {
    private val favoriteNoteService = FavoriteNoteService()

    fun build(note: Note): JMenuItem {
        val favoriteText =
            if (note.isFavorite) {
                "${message("note.context.menu.unfavorite")} (F)"
            } else {
                "${message("note.context.menu.favorite")} (F)"
            }

        val favoriteIcon = AllIcons.Nodes.BookmarkGroup
        val favoriteItem = JMenuItem(favoriteText, favoriteIcon)
        favoriteItem.border = JBUI.Borders.empty(5, 10, 5, 10)

        favoriteItem.addActionListener {
            favoriteNoteService.toggleFavorite(note)
        }

        return favoriteItem
    }
}
