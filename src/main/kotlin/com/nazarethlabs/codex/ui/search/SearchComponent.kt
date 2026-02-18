package com.nazarethlabs.codex.ui.search

import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.state.NotesStateManager
import com.nazarethlabs.codex.ui.component.TextFieldComponent
import javax.swing.BoxLayout
import javax.swing.BoxLayout.Y_AXIS

class SearchComponent {
    fun build(): JBPanel<JBPanel<*>> {
        val stateManager = NotesStateManager.getInstance()
        val placeholder = MyBundle.message("search.placeholder")

        val searchField =
            TextFieldComponent()
                .build(placeholder) { filterText ->
                    stateManager.search(filterText)
                }

        val searchFieldPanel =
            JBPanel<JBPanel<*>>().apply {
                layout = BoxLayout(this, Y_AXIS)
                border = JBUI.Borders.empty(5)
                add(searchField)
            }

        val filterPanel = SearchFilterComponent().build()

        return JBPanel<JBPanel<*>>().apply {
            layout = BoxLayout(this, Y_AXIS)
            add(searchFieldPanel)
            add(filterPanel)
        }
    }
}
