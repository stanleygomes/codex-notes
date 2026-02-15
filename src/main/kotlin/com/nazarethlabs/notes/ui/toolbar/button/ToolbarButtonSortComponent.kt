package com.nazarethlabs.notes.ui.toolbar.button

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.enum.SortTypeEnum
import com.nazarethlabs.notes.enum.SortTypeEnum.DATE
import com.nazarethlabs.notes.enum.SortTypeEnum.FAVORITE
import com.nazarethlabs.notes.enum.SortTypeEnum.TITLE
import com.nazarethlabs.notes.helper.MessageHelper
import com.nazarethlabs.notes.service.settings.NotesSettingsService
import com.nazarethlabs.notes.ui.component.ButtonComponent
import com.nazarethlabs.notes.ui.noteslist.NotesListComponent
import javax.swing.JButton
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class ToolbarButtonSortComponent {
    fun build(
        project: Project,
        notesListComponent: NotesListComponent,
    ): JButton {
        val sortButton =
            ButtonComponent()
                .build(
                    AllIcons.ObjectBrowser.Sorted,
                    MessageHelper.getMessage("toolbar.note.sort"),
                )

        sortButton.addActionListener { event ->
            val menu = JPopupMenu()
            menu.border = JBUI.Borders.empty(5)

            menu.add(
                createSortMenuItem("toolbar.sort.by.title", TITLE, notesListComponent),
            )
            menu.add(
                createSortMenuItem("toolbar.sort.by.date", DATE, notesListComponent),
            )
            menu.add(
                createSortMenuItem("toolbar.sort.by.favorite", FAVORITE, notesListComponent),
            )

            val component = event.source as JButton
            menu.show(component, 0, component.height)
        }

        return sortButton
    }

    private fun createSortMenuItem(
        messageKey: String,
        sortType: SortTypeEnum,
        notesListComponent: NotesListComponent,
    ): JMenuItem {
        val itemInset = JBUI.Borders.empty(5, 10, 5, 10)
        val item = JMenuItem(MessageHelper.getMessage(messageKey))
        item.border = itemInset

        item.addActionListener {
            notesListComponent.sortBy(sortType)
            NotesSettingsService().setDefaultSortType(sortType)
        }

        return item
    }
}
