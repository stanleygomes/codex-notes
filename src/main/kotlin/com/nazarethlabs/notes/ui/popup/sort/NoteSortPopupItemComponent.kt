package com.nazarethlabs.notes.ui.popup.sort

import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.enum.SortTypeEnum
import com.nazarethlabs.notes.helper.MessageHelper
import com.nazarethlabs.notes.service.settings.NotesSettingsService
import com.nazarethlabs.notes.ui.noteslist.NotesListComponent
import javax.swing.JMenuItem

class NoteSortPopupItemComponent {
    fun createSortMenuItem(
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
