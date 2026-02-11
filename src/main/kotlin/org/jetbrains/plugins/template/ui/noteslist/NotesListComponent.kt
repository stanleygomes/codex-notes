package org.jetbrains.plugins.template.ui.noteslist

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.entity.Note
import org.jetbrains.plugins.template.listener.NoteListener
import org.jetbrains.plugins.template.service.NoteEventService
import org.jetbrains.plugins.template.service.NoteStorageService
import org.jetbrains.plugins.template.listener.NoteListMouseListener
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
