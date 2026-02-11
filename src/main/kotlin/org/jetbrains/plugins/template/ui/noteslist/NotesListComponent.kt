package org.jetbrains.plugins.template.ui.noteslist

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.listener.NoteListener
import org.jetbrains.plugins.template.service.NoteEventService
import org.jetbrains.plugins.template.service.NoteStorageService
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JScrollPane

class NotesListComponent : NoteListener {

    private lateinit var listModel: DefaultListModel<String>
    private lateinit var noteStorage: NoteStorageService

    fun build(project: Project): JScrollPane {
        listModel = DefaultListModel()
        noteStorage = NoteStorageService.getInstance(project)

        NoteEventService.getInstance(project).addListener(this)

        refreshList()

        val list = JList(listModel)
        return JScrollPane(list)
    }

    private fun refreshList() {
        listModel.clear()
        noteStorage.getAllNotes().forEach { note ->
            listModel.addElement(note.title)
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
