package com.nazarethlabs.notes.ui.search

import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.MyBundle
import com.nazarethlabs.notes.state.NotesStateManager
import com.nazarethlabs.notes.ui.component.TextFieldComponent
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
