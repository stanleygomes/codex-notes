package org.jetbrains.plugins.template.ui.noteslist

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.listener.NoteListener
import org.jetbrains.plugins.template.listener.NoteListKeyListener
import org.jetbrains.plugins.template.listener.NoteListMouseListener
import org.jetbrains.plugins.template.service.NoteEventService
import org.jetbrains.plugins.template.service.NoteStorageService
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JScrollPane
import javax.swing.ListCellRenderer

class NotesListComponent : NoteListener {

    private lateinit var listModel: DefaultListModel<Note>
    private lateinit var noteStorage: NoteStorageService
    private lateinit var project: Project

    fun build(project: Project): JScrollPane {
        this.project = project

        listModel = DefaultListModel()
        noteStorage = NoteStorageService.getInstance(project)
        NoteEventService.getInstance(project).addListener(this)

        refreshList()

        val list = JList(listModel)
        list.cellRenderer = object : ListCellRenderer<Note> {
            override fun getListCellRendererComponent(
                list: JList<out Note>?,
                value: Note?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): java.awt.Component {
                val backgroundColor = if (isSelected) list!!.selectionBackground else list!!.background
                val foregroundColor = if (isSelected) list.selectionForeground else list.foreground
                return NoteListItemComponent(value ?: return javax.swing.JPanel(), backgroundColor, foregroundColor)
            }
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

        return JScrollPane(list)
    }


    private fun refreshList() {
        listModel.clear()
        noteStorage.getAllNotes().forEach { note ->
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
