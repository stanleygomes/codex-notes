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

    fun create(project: Project, note: Note): JPopupMenu {
        val openNoteService = OpenNoteService()
        val renameNoteService = RenameNoteService()
        val favoriteNoteService = FavoriteNoteService()
        val deleteNoteService = DeleteNoteService()

        val menu = JPopupMenu()

        val openItem = JMenuItem(MyBundle.message("note.context.menu.open"), AllIcons.Actions.MenuOpen)
        openItem.border = itemInset
        openItem.addActionListener {
            openNoteService.open(project, note)
        }
        menu.add(openItem)

        menu.addSeparator()

        val renameItem = JMenuItem(MyBundle.message("note.context.menu.rename"), AllIcons.Actions.Edit)
        renameItem.border = itemInset
        renameItem.addActionListener {
            renameNoteService.rename(project, note)
        }
        menu.add(renameItem)

        val favoriteText = if (note.isFavorite) {
            MyBundle.message("note.context.menu.unfavorite")
        } else {
            MyBundle.message("note.context.menu.favorite")
        }
        val favoriteIcon = AllIcons.Nodes.BookmarkGroup
        val favoriteItem = JMenuItem(favoriteText, favoriteIcon)
        favoriteItem.border = itemInset
        favoriteItem.addActionListener {
            favoriteNoteService.toggleFavorite(project, note)
        }
        menu.add(favoriteItem)

        menu.addSeparator()

        val deleteItem = JMenuItem(MyBundle.message("note.context.menu.delete"), AllIcons.Actions.Cancel)
        deleteItem.border = itemInset
        deleteItem.addActionListener {
            deleteNoteService.confirmAndDelete(project, note)
        }
        menu.add(deleteItem)

        return menu
    }
}
