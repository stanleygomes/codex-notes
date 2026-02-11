package org.jetbrains.plugins.template.ui.noteslist

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.enum.SortTypeEnum
import org.jetbrains.plugins.template.listener.NoteEventListener
import org.jetbrains.plugins.template.listener.NoteListener
import org.jetbrains.plugins.template.listener.NoteListKeyListener
import org.jetbrains.plugins.template.listener.NoteListMouseListener
import org.jetbrains.plugins.template.repository.NoteStorageRepository
import java.awt.BorderLayout
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ListCellRenderer

class NotesListComponent : NoteListener {

    private lateinit var listModel: DefaultListModel<Note>
    private lateinit var noteStorage: NoteStorageRepository
    private lateinit var project: Project
    private lateinit var searchPanel: JPanel
    private var currentSortTypeEnum: SortTypeEnum = SortTypeEnum.DATE

    fun build(project: Project): JPanel {
        this.project = project

        listModel = DefaultListModel()
        noteStorage = NoteStorageRepository.getInstance(project)
        NoteEventListener.getInstance(project).addListener(this)

        refreshList()

        searchPanel = SearchComponent().build(noteStorage, listModel)
        searchPanel.isVisible = false

        val list = JList(listModel)
        list.cellRenderer = ListCellRenderer { list, value, _, isSelected, _ ->
            NoteListItemComponent()
                .build(theList = list, note = value, isSelected = isSelected)
        }

        val mouseListener = NoteListMouseListener(
            project = project,
            getSelectedValue = { list.selectedValue }
        )

        list.addMouseListener(mouseListener)

        val keyListener = NoteListKeyListener(
            project = project,
            getSelectedValue = { list.selectedValue }
        )

        list.addKeyListener(keyListener)

        val scrollPane = JScrollPane(list)

        return JPanel(BorderLayout()).apply {
            add(searchPanel, BorderLayout.NORTH)
            add(scrollPane, BorderLayout.CENTER)
        }
    }

    fun toggleSearch() {
        searchPanel.isVisible = !searchPanel.isVisible
        if (!searchPanel.isVisible) {
            refreshList()
        }
    }

    fun sortBy(sortTypeEnum: SortTypeEnum) {
        currentSortTypeEnum = sortTypeEnum
        refreshList()
    }

    private fun refreshList() {
        listModel.clear()

        val notes = when (currentSortTypeEnum) {
            SortTypeEnum.TITLE -> noteStorage.getAllNotes().sortedBy { it.title.lowercase() }
            SortTypeEnum.DATE -> noteStorage.getAllNotes().sortedByDescending { it.updatedAt }
            SortTypeEnum.FAVORITE -> noteStorage.getAllNotes().sortedWith(compareByDescending<Note> { it.isFavorite }.thenByDescending { it.updatedAt })
        }

        notes.forEach { note ->
            listModel.addElement(note)
        }
    }

    override fun onNoteCreated() {
        refreshList()
    }

    override fun onNoteUpdated() {
        refreshList()
    }

    override fun onNoteDeleted() {
        refreshList()
    }
}
