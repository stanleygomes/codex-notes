package com.nazarethlabs.notes.ui.search

import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.MyBundle
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.helper.SearchHelper
import com.nazarethlabs.notes.repository.NoteStorageRepository
import java.awt.BorderLayout
import javax.swing.DefaultListModel
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class SearchComponent {
    private lateinit var searchField: JBTextField

    fun build(
        noteStorage: NoteStorageRepository,
        listModel: DefaultListModel<Note>,
    ): JPanel {
        searchField =
            JBTextField().apply {
                emptyText.text = MyBundle.message("search.placeholder")
                document.addDocumentListener(
                    object : DocumentListener {
                        override fun insertUpdate(e: DocumentEvent?) = filterNotes(noteStorage, listModel)

                        override fun removeUpdate(e: DocumentEvent?) = filterNotes(noteStorage, listModel)

                        override fun changedUpdate(e: DocumentEvent?) = filterNotes(noteStorage, listModel)
                    },
                )
            }

        return JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(5)
            add(searchField, BorderLayout.CENTER)
        }
    }

    private fun filterNotes(
        noteStorage: NoteStorageRepository,
        listModel: DefaultListModel<Note>,
    ) {
        val filterText = searchField.text ?: ""

        listModel.clear()
        val filteredNotes = SearchHelper.search(noteStorage.getAllNotes(), filterText)
        filteredNotes.forEach { note ->
            listModel.addElement(note)
        }
    }
}
