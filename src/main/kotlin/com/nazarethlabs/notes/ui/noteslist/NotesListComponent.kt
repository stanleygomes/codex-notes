package com.nazarethlabs.notes.ui.noteslist

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.enum.SortTypeEnum
import com.nazarethlabs.notes.enum.SortTypeEnum.DATE
import com.nazarethlabs.notes.listener.NoteEventListener
import com.nazarethlabs.notes.listener.NoteListKeyListener
import com.nazarethlabs.notes.listener.NoteListMouseListener
import com.nazarethlabs.notes.listener.NoteListener
import com.nazarethlabs.notes.repository.NoteStorageRepository
import com.nazarethlabs.notes.service.note.NotesSortService
import com.nazarethlabs.notes.service.settings.NotesSettingsService
import com.nazarethlabs.notes.ui.component.EmptyStateComponent
import com.nazarethlabs.notes.ui.search.SearchComponent
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
    private lateinit var notesList: JList<Note>
    private var currentSortTypeEnum: SortTypeEnum = DATE

    fun build(project: Project): JPanel {
        this.project = project

        listModel = DefaultListModel()
        noteStorage = NoteStorageRepository.getInstance()
        currentSortTypeEnum = NotesSettingsService().getDefaultSortType()
        NoteEventListener.getInstance(project).addListener(this)

        refreshList(listModel)
        updateView()

        searchPanel = SearchComponent().build(listModel)
        searchPanel.isVisible = false

        notesList = JList(listModel)
        notesList.cellRenderer =
            ListCellRenderer { list, value, _, isSelected, _ ->
                NoteListItemComponent()
                    .build(theList = list, note = value, isSelected = isSelected)
            }

        val mouseListener =
            NoteListMouseListener(
                project = project,
                getSelectedValue = { notesList.selectedValue },
            )

        notesList.addMouseListener(mouseListener)

        val keyListener =
            NoteListKeyListener(
                project = project,
                getSelectedValue = { notesList.selectedValue },
            )

        notesList.addKeyListener(keyListener)

        scrollPane = JScrollPane(notesList)
        emptyStatePanel = EmptyStateComponent().build()

        mainPanel =
            JPanel(BorderLayout()).apply {
                add(searchPanel, NORTH)
                add(if (listModel.isEmpty) emptyStatePanel else scrollPane, CENTER)
            }

        return mainPanel
    }

    fun toggleSearch() {
        searchPanel.isVisible = !searchPanel.isVisible
        if (!searchPanel.isVisible) {
            refreshList(listModel)
            updateView()
        }
    }

    fun sortBy(sortTypeEnum: SortTypeEnum) {
        currentSortTypeEnum = sortTypeEnum
        refreshList(listModel)
        updateView()
    }

    fun getSelectedNote(): Note? = if (::notesList.isInitialized) notesList.selectedValue else null

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
        refreshList(listModel)
        updateView()
    }

    override fun onNoteUpdated() {
        refreshList(listModel)
        updateView()
    }

    override fun onNoteDeleted() {
        refreshList(listModel)
        updateView()
    }

    private fun refreshList(
        listModel: DefaultListModel<Note>,
    ) {
        listModel.clear()
        val notes = NotesSortService()
            .refreshList(currentSortTypeEnum)

        notes.forEach { note ->
            listModel.addElement(note)
        }
    }
}
