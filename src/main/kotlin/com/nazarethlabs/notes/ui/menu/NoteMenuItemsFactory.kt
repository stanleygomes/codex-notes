package com.nazarethlabs.notes.ui.menu

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.MyBundle
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.enum.NoteColorEnum
import com.nazarethlabs.notes.service.ChangeNoteColorService
import com.nazarethlabs.notes.service.DeleteNoteService
import com.nazarethlabs.notes.service.FavoriteNoteService
import com.nazarethlabs.notes.service.OpenNoteService
import com.nazarethlabs.notes.service.RenameNoteService
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class NoteMenuItemsFactory {
    private val itemInset = JBUI.Borders.empty(5, 10, 5, 10)
    private val openNoteService = OpenNoteService()
    private val renameNoteService = RenameNoteService()
    private val favoriteNoteService = FavoriteNoteService()
    private val deleteNoteService = DeleteNoteService()
    private val changeNoteColorService = ChangeNoteColorService()

    fun createPopupMenu(
        project: Project,
        note: Note,
    ): JPopupMenu {
        val menu = JPopupMenu()
        menu.border = JBUI.Borders.empty(5)

        val openItem = buildMenuItemOpen(project, note)
        menu.add(openItem)

        menu.addSeparator()

        val renameItem = buildMenuItemRename(project, note)
        menu.add(renameItem)

        val favoriteItem = buildMenuItemFavorite(note)
        menu.add(favoriteItem)

        val colorMenu = buildColorMenu(note)
        menu.add(colorMenu)

        menu.addSeparator()

        val deleteItem = buildMenuItemDelete(project, note)
        menu.add(deleteItem)

        return menu
    }

    fun buildMenuItemOpen(
        project: Project,
        note: Note,
    ): JMenuItem {
        val openItem = JMenuItem("${MyBundle.message("note.context.menu.open")} (Double-click)")

        openItem.border = itemInset
        openItem.addActionListener {
            openNoteService.open(project, note)
        }

        return openItem
    }

    fun buildMenuItemRename(
        project: Project,
        note: Note,
    ): JMenuItem {
        val renameItem = JMenuItem("${MyBundle.message("note.context.menu.rename")} (F2)", AllIcons.Actions.Edit)
        renameItem.border = itemInset

        renameItem.addActionListener {
            renameNoteService.rename(project, note)
        }

        return renameItem
    }

    fun buildMenuItemFavorite(note: Note): JMenuItem {
        val favoriteText =
            if (note.isFavorite) {
                "${MyBundle.message("note.context.menu.unfavorite")} (F)"
            } else {
                "${MyBundle.message("note.context.menu.favorite")} (F)"
            }

        val favoriteIcon = AllIcons.Nodes.BookmarkGroup
        val favoriteItem = JMenuItem(favoriteText, favoriteIcon)
        favoriteItem.border = itemInset

        favoriteItem.addActionListener {
            favoriteNoteService.toggleFavorite(note)
        }

        return favoriteItem
    }

    fun buildColorMenu(note: Note): JMenu {
        val colorMenu = JMenu(MyBundle.message("note.context.menu.change.color"))
        colorMenu.icon = AllIcons.Actions.Colors
        colorMenu.border = itemInset

        NoteColorEnum.entries.forEach { color ->
            val colorItem = JMenuItem(MyBundle.message(color.displayNameKey))
            colorItem.border = itemInset

            colorItem.addActionListener {
                changeNoteColorService.changeColor(note, color)
            }

            colorMenu.add(colorItem)
        }

        return colorMenu
    }

    fun buildMenuItemDelete(
        project: Project,
        note: Note,
    ): JMenuItem {
        val deleteItem = JMenuItem("${MyBundle.message("note.context.menu.delete")} (Delete)")
        deleteItem.border = itemInset

        deleteItem.addActionListener {
            deleteNoteService.confirmAndDelete(project, note)
        }

        return deleteItem
    }
}
