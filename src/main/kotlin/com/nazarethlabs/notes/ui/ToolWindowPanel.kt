package com.nazarethlabs.notes.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.nazarethlabs.notes.listener.SearchStateListener
import com.nazarethlabs.notes.state.SearchStateManager
import com.nazarethlabs.notes.ui.noteslist.NotesListComponent
import com.nazarethlabs.notes.ui.search.SearchComponent
import com.nazarethlabs.notes.ui.toolbar.ToolbarComponent
import javax.swing.JPanel

class ToolWindowPanel : SearchStateListener {
    private lateinit var searchPanel: JPanel
    private val searchStateManager = SearchStateManager.getInstance()

    fun build(project: Project): JBPanel<JBPanel<*>> {
        val notesListComponent = NotesListComponent()
        val toolbar =
            ToolbarComponent()
                .build(project)

        searchPanel = SearchComponent().build()
        searchPanel.isVisible = false

        searchStateManager.addListener(this)

        val notesList =
            notesListComponent
                .build(project)

        return WindowContainerComponent()
            .build(toolbar, searchPanel, notesList)
    }

    override fun onSearchVisibilityChanged(isVisible: Boolean) {
        searchPanel.isVisible = isVisible
    }
}
