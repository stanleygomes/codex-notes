package com.nazarethlabs.notes.ui.noteslist

import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.listener.NoteListKeyListener
import com.nazarethlabs.notes.listener.NoteListMouseListener
import com.nazarethlabs.notes.listener.NotesStateListener
import com.nazarethlabs.notes.state.NotesStateManager
import com.nazarethlabs.notes.state.SelectedNoteStateManager
import com.nazarethlabs.notes.ui.component.EmptyStateComponent
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ListCellRenderer

class NotesListComponent : NotesStateListener {
    private lateinit var listModel: DefaultListModel<Note>
    private lateinit var project: Project
    private lateinit var stateManager: NotesStateManager
    private val selectedNoteStateManager = SelectedNoteStateManager.getInstance()
    private lateinit var mainPanel: JPanel
    private lateinit var scrollPane: JScrollPane
    private lateinit var emptyStatePanel: JPanel
    private lateinit var notesList: JList<Note>

    fun build(project: Project): JPanel {
        this.project = project
        this.stateManager = NotesStateManager.getInstance()

        listModel = DefaultListModel()
        stateManager.addListener(this)

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

        notesList.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                selectedNoteStateManager.setSelectedNote(notesList.selectedValue)
            }
        }

        scrollPane = JScrollPane(notesList)
        emptyStatePanel = EmptyStateComponent().build()

        mainPanel =
            JPanel(BorderLayout()).apply {
                add(if (listModel.isEmpty) emptyStatePanel else scrollPane, CENTER)
            }

        return mainPanel
    }


    override fun onNotesStateChanged(notes: List<Note>) {
        listModel.clear()
        notes.forEach { note ->
            listModel.addElement(note)
        }
        updateView()
    }

    private fun updateView() {
        if (::mainPanel.isInitialized) {
            mainPanel.removeAll()
            mainPanel.add(if (listModel.isEmpty) emptyStatePanel else scrollPane, CENTER)
            mainPanel.revalidate()
            mainPanel.repaint()
        }
    }
}
