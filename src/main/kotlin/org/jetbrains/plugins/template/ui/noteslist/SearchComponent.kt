package org.jetbrains.plugins.template.ui.noteslist

import com.intellij.ui.components.JBTextField
import org.jetbrains.plugins.template.MyBundle
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.helper.SearchHelper
import org.jetbrains.plugins.template.repository.NoteStorageRepository
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import javax.swing.DefaultListModel
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class SearchComponent {

    private lateinit var searchField: JBTextField

    fun build(
        noteStorage: NoteStorageRepository,
        listModel: DefaultListModel<Note>
    ): JPanel {
        searchField = JBTextField().apply {
            emptyText.text = MyBundle.message("search.placeholder")
            document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) = filterNotes(noteStorage, listModel)
                override fun removeUpdate(e: DocumentEvent?) = filterNotes(noteStorage, listModel)
                override fun changedUpdate(e: DocumentEvent?) = filterNotes(noteStorage, listModel)
            })
        }

        return JPanel(BorderLayout()).apply {
            add(searchField, CENTER)
        }
    }

    private fun filterNotes(noteStorage: NoteStorageRepository, listModel: DefaultListModel<Note>) {
        val filterText = searchField.text ?: ""

        listModel.clear()
        val filteredNotes = SearchHelper.search(noteStorage.getAllNotes(), filterText)
        filteredNotes.forEach { note ->
            listModel.addElement(note)
        }
    }
}
