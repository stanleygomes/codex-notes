package com.nazarethlabs.codex.ui

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.nazarethlabs.codex.listener.SearchStateListener
import com.nazarethlabs.codex.state.SearchStateManager
import com.nazarethlabs.codex.ui.noteslist.NotesListComponent
import com.nazarethlabs.codex.ui.search.SearchComponent
import com.nazarethlabs.codex.ui.toolbar.ToolbarComponent
import javax.swing.JPanel
import javax.swing.SwingUtilities

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
        if (isVisible) {
            SwingUtilities.invokeLater {
                focusSearchField(searchPanel)
            }
        }
    }
}

internal fun focusSearchField(searchPanel: JPanel) {
    searchPanel.components.firstOrNull()?.requestFocusInWindow()
}
