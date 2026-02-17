package com.nazarethlabs.codex.ui.component

import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.ListCellRenderer

class GenericJListComponent<T>(
    private val model: DefaultListModel<T>,
    private val cellRenderer: ListCellRenderer<in T>,
) {
    fun build(): JList<T> {
        val list = JList(model)
        list.cellRenderer = cellRenderer
        return list
    }
}
