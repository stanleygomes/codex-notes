package com.nazarethlabs.notes.ui.popup.sort

import com.nazarethlabs.notes.enum.SortTypeEnum.DATE
import com.nazarethlabs.notes.enum.SortTypeEnum.FAVORITE
import com.nazarethlabs.notes.enum.SortTypeEnum.TITLE
import com.nazarethlabs.notes.ui.component.PopupMenuComponent
import com.nazarethlabs.notes.ui.noteslist.NotesListComponent
import javax.swing.JPopupMenu

class NoteSortPopupComponent {
    private val noteSortPopupItemComponent = NoteSortPopupItemComponent()
    fun createPopupMenu(notesListComponent: NotesListComponent): JPopupMenu {
        val menu = PopupMenuComponent()
            .build()

        menu.add(
            noteSortPopupItemComponent.createSortMenuItem(
                "toolbar.sort.by.title",
                TITLE,
                notesListComponent,
            ),
        )

        menu.add(
            noteSortPopupItemComponent.createSortMenuItem(
                "toolbar.sort.by.date",
                DATE,
                notesListComponent,
            ),
        )

        menu.add(
            noteSortPopupItemComponent.createSortMenuItem(
                "toolbar.sort.by.favorite",
                FAVORITE,
                notesListComponent,
            ),
        )

        return menu
    }
}
