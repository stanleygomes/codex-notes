package com.nazarethlabs.codex.search

import com.intellij.ui.SimpleColoredComponent
import com.intellij.ui.SimpleTextAttributes
import com.nazarethlabs.codex.dto.Note
import java.awt.BorderLayout
import java.awt.Component
import java.text.SimpleDateFormat
import java.util.Date
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

class NoteSearchCellRenderer : ListCellRenderer<Note> {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

    override fun getListCellRendererComponent(
        list: JList<out Note>?,
        value: Note?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean,
    ): Component {
        val panel = JPanel(BorderLayout())

        if (value == null) return panel

        val component = SimpleColoredComponent()

        val prefix = if (value.isFavorite) "⭐ " else ""

        component.append(
            "$prefix${value.title}",
            if (isSelected) {
                SimpleTextAttributes.SELECTED_SIMPLE_CELL_ATTRIBUTES
            } else {
                SimpleTextAttributes.REGULAR_ATTRIBUTES
            },
        )

        component.append(
            " - ${dateFormat.format(Date(value.updatedAt))}",
            if (isSelected) {
                SimpleTextAttributes.SELECTED_SIMPLE_CELL_ATTRIBUTES
            } else {
                SimpleTextAttributes.GRAYED_ATTRIBUTES
            },
        )

        if (isSelected) {
            component.background = list?.selectionBackground
            component.foreground = list?.selectionForeground
            component.isOpaque = true
        }

        panel.add(component, BorderLayout.CENTER)
        return panel
    }
}
