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

class NotesListComponent : NoteListener {

    private lateinit var listModel: DefaultListModel<String>
    private lateinit var noteStorage: NoteStorageService
    private lateinit var project: Project
    private val notesMap = mutableMapOf<String, Note>()

    fun build(project: Project): JScrollPane {
        this.project = project

        listModel = DefaultListModel()
        noteStorage = NoteStorageService.getInstance(project)
        NoteEventService.getInstance(project).addListener(this)

        refreshList()

        val list = JList(listModel)
        val mouseListener = NoteListMouseListener(
            project = project,
            notesMap = notesMap,
            getSelectedValue = { list.selectedValue }
        )

        list.addMouseListener(mouseListener)

        val keyListener = NoteListKeyListener(
            project = project,
            notesMap = notesMap,
            getSelectedValue = { list.selectedValue }
        )
        list.addKeyListener(keyListener)

        return JScrollPane(list)
    }


    private fun refreshList() {
        listModel.clear()
        notesMap.clear()
        noteStorage.getAllNotes().forEach { note ->
            listModel.addElement(note.title)
            notesMap[note.title] = note
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
