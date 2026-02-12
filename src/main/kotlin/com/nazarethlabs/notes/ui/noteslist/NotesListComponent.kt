package com.nazarethlabs.notes.ui.noteslist

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.enum.SortTypeEnum
import com.nazarethlabs.notes.enum.SortTypeEnum.DATE
import com.nazarethlabs.notes.enum.SortTypeEnum.FAVORITE
import com.nazarethlabs.notes.enum.SortTypeEnum.TITLE
import com.nazarethlabs.notes.listener.NoteEventListener
import com.nazarethlabs.notes.listener.NoteListKeyListener
import com.nazarethlabs.notes.listener.NoteListMouseListener
import com.nazarethlabs.notes.listener.NoteListener
import com.nazarethlabs.notes.repository.NoteStorageRepository
import com.nazarethlabs.notes.service.NotesSettingsService
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.NORTH
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
    private lateinit var mainPanel: JPanel
    private lateinit var scrollPane: JScrollPane
    private lateinit var emptyStatePanel: JPanel
    private var currentSortTypeEnum: SortTypeEnum = DATE

    fun build(project: Project): JPanel {
        this.project = project

        listModel = DefaultListModel()
        noteStorage = NoteStorageRepository.getInstance()
        currentSortTypeEnum = NotesSettingsService().getDefaultSortType()
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

        scrollPane = JScrollPane(list)
        emptyStatePanel = EmptyStateComponent().build()

        mainPanel = JPanel(BorderLayout()).apply {
            add(searchPanel, NORTH)
            add(if (listModel.isEmpty) emptyStatePanel else scrollPane, CENTER)
        }

        return mainPanel
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
            TITLE -> noteStorage.getAllNotes().sortedBy { it.title.lowercase() }
            DATE -> noteStorage.getAllNotes().sortedByDescending { it.updatedAt }
            FAVORITE -> noteStorage.getAllNotes().sortedWith(compareByDescending<Note> { it.isFavorite }.thenByDescending { it.updatedAt })
        }

        notes.forEach { note ->
            listModel.addElement(note)
        }

        updateView()
    }

    private fun updateView() {
        if (::mainPanel.isInitialized) {
            mainPanel.removeAll()
            mainPanel.add(searchPanel, NORTH)
            mainPanel.add(if (listModel.isEmpty) emptyStatePanel else scrollPane, CENTER)
            mainPanel.revalidate()
            mainPanel.repaint()
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
