package org.jetbrains.plugins.template.ui.noteslist

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import org.jetbrains.plugins.template.MyBundle
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.service.DeleteNoteService
import org.jetbrains.plugins.template.service.FavoriteNoteService
import org.jetbrains.plugins.template.service.OpenNoteService
import org.jetbrains.plugins.template.service.RenameNoteService
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class NoteContextMenuFactory {

    private val itemInset = JBUI.Borders.empty(5, 10, 5, 10)
    private val openNoteService = OpenNoteService()
    private val renameNoteService = RenameNoteService()
    private val favoriteNoteService = FavoriteNoteService()
    private val deleteNoteService = DeleteNoteService()

    fun create(project: Project, note: Note): JPopupMenu {
        val menu = JPopupMenu()
        menu.border = JBUI.Borders.empty(5)

        val openItem = buildMenuItemOpen(project, note)
        menu.add(openItem)

        menu.addSeparator()

        val renameItem = buildMenuItemRename(project, note)
        menu.add(renameItem)

        val favoriteItem = buildMenuItemFavorite(project, note)
        menu.add(favoriteItem)

        menu.addSeparator()

        val deleteItem = buildMenuItemDelete(project, note)
        menu.add(deleteItem)

        return menu
    }

    private fun buildMenuItemOpen(project: Project, note: Note): JMenuItem {
        val openItem = JMenuItem("${MyBundle.message("note.context.menu.open")} (Double-click)", AllIcons.Actions.MenuOpen)

        openItem.border = itemInset
        openItem.addActionListener {
            openNoteService.open(project, note)
        }

        return openItem
    }

    private fun buildMenuItemRename(project: Project, note: Note): JMenuItem {
        val renameItem = JMenuItem("${MyBundle.message("note.context.menu.rename")} (F2)", AllIcons.Actions.Edit)
        renameItem.border = itemInset

        renameItem.addActionListener {
            renameNoteService.rename(project, note)
        }

        return renameItem
    }

    private fun buildMenuItemFavorite(project: Project, note: Note): JMenuItem {
        val favoriteText = if (note.isFavorite) {
            "${MyBundle.message("note.context.menu.unfavorite")} (F)"
        } else {
            "${MyBundle.message("note.context.menu.favorite")} (F)"
        }

        val favoriteIcon = AllIcons.Nodes.BookmarkGroup
        val favoriteItem = JMenuItem(favoriteText, favoriteIcon)
        favoriteItem.border = itemInset

        favoriteItem.addActionListener {
            favoriteNoteService.toggleFavorite(project, note)
        }

        return favoriteItem
    }

    private fun buildMenuItemDelete(project: Project, note: Note): JMenuItem {
        val deleteItem = JMenuItem("${MyBundle.message("note.context.menu.delete")} (Delete)", AllIcons.Actions.Cancel)
        deleteItem.border = itemInset

        deleteItem.addActionListener {
            deleteNoteService.confirmAndDelete(project, note)
        }

        return deleteItem
    }
}
