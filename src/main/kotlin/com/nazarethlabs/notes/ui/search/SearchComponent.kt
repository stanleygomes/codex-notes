package com.nazarethlabs.notes.ui.search

import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.MyBundle
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.service.note.SearchNoteService
import com.nazarethlabs.notes.ui.component.TextFieldComponent
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import javax.swing.DefaultListModel
import javax.swing.JPanel

class SearchComponent {
    fun build(
        listModel: DefaultListModel<Note>,
    ): JPanel {
        val placeholder = MyBundle.message("search.placeholder")
        val searchField = TextFieldComponent()
            .build(placeholder) { filterText ->
                val filteredNotes = SearchNoteService()
                    .filterNotes(filterText)

                listModel.clear()

                filteredNotes.forEach {
                    listModel.addElement(it)
                }
            }

        return JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(5)
            add(searchField, CENTER)
        }
    }
}
