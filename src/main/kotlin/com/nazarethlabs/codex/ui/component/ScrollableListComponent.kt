package com.nazarethlabs.codex.ui.component

import javax.swing.JList
import javax.swing.JScrollPane

class ScrollableListComponent<T>(
    private val list: JList<T>,
) {
    fun build(): JScrollPane {
        val scrollPane = JScrollPane(list)
        scrollPane.border = null
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        return scrollPane
    }
}
