package com.nazarethlabs.codex.ui.search

import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.state.NotesStateManager
import com.nazarethlabs.codex.ui.component.TextFieldComponent
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import javax.swing.JPanel

class SearchComponent {
    fun build(): JPanel {
        val stateManager = NotesStateManager.getInstance()
        val placeholder = MyBundle.message("search.placeholder")

        val searchField =
            TextFieldComponent()
                .build(placeholder) { filterText ->
                    stateManager.search(filterText)
                }

        return JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(5)
            add(searchField, CENTER)
        }
    }
}
