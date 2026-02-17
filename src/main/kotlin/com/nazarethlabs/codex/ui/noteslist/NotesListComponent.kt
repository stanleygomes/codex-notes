package com.nazarethlabs.codex.ui.noteslist

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.listener.NoteListKeyListener
import com.nazarethlabs.codex.listener.NoteListMouseListener
import com.nazarethlabs.codex.listener.NotesStateListener
import com.nazarethlabs.codex.state.NotesStateManager
import com.nazarethlabs.codex.state.SelectedNoteStateManager
import com.nazarethlabs.codex.ui.component.EmptyStateComponent
import com.nazarethlabs.codex.ui.component.GenericJListComponent
import com.nazarethlabs.codex.ui.component.ScrollableJListComponent
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ListCellRenderer
import javax.swing.ListSelectionModel

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

        val cellRenderer =
            ListCellRenderer { list, value, _, isSelected, _ ->
                NoteListItemComponent()
                    .build(theList = list, note = value, isSelected = isSelected)
            }

        notesList = GenericJListComponent(listModel, cellRenderer).build()
        notesList.selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION

        val mouseListener =
            NoteListMouseListener(
                project = project,
                getSelectedValues = { notesList.selectedValuesList },
            )

        notesList.addMouseListener(mouseListener)

        val keyListener =
            NoteListKeyListener(
                project = project,
                getSelectedValues = { notesList.selectedValuesList },
            )

        notesList.addKeyListener(keyListener)

        notesList.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                selectedNoteStateManager.setSelectedNotes(notesList.selectedValuesList)
            }
        }

        scrollPane = ScrollableJListComponent(notesList).build()
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
