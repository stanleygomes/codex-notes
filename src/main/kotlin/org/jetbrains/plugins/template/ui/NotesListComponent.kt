package org.jetbrains.plugins.template.ui

import javax.swing.JList
import javax.swing.JScrollPane

class NotesListComponent {

    fun build(): JScrollPane {
        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        val list = JList(items)
        return JScrollPane(list)
    }
}
