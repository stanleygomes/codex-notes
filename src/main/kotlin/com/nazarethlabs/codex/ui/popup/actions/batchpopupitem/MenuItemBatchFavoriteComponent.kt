package com.nazarethlabs.codex.ui.popup.actions.batchpopupitem

import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.service.note.FavoriteNotesService
import javax.swing.JMenu
import javax.swing.JMenuItem

class MenuItemBatchFavoriteComponent {
    private val favoriteNotesService = FavoriteNotesService()

    fun build(notes: List<Note>): JMenu {
        val favoriteMenu =
            JMenu(
                MessageHelper.getMessage(
                    "note.context.menu.favorite.batch",
                    notes.size.toString(),
                ),
            )

        favoriteMenu.icon = AllIcons.Nodes.BookmarkGroup
        favoriteMenu.border = JBUI.Borders.empty(5, 10, 5, 10)

        val favoriteAllItem = JMenuItem(MessageHelper.getMessage("note.context.menu.favorite.all"))
        favoriteAllItem.border = JBUI.Borders.empty(5, 10, 5, 10)
        favoriteAllItem.addActionListener {
            favoriteNotesService.favoriteAll(notes)
        }
        favoriteMenu.add(favoriteAllItem)

        val unfavoriteAllItem = JMenuItem(MessageHelper.getMessage("note.context.menu.unfavorite.all"))
        unfavoriteAllItem.border = JBUI.Borders.empty(5, 10, 5, 10)
        unfavoriteAllItem.addActionListener {
            favoriteNotesService.unfavoriteAll(notes)
        }
        favoriteMenu.add(unfavoriteAllItem)

        return favoriteMenu
    }
}
