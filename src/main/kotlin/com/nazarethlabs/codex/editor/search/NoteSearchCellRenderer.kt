package com.nazarethlabs.codex.editor.search

import com.intellij.ui.SimpleColoredComponent
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.SimpleTextAttributes.GRAYED_ATTRIBUTES
import com.intellij.ui.SimpleTextAttributes.REGULAR_ATTRIBUTES
import com.intellij.ui.SimpleTextAttributes.SELECTED_SIMPLE_CELL_ATTRIBUTES
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.DateHelper
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.Component
import java.util.Date
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

class NoteSearchCellRenderer : ListCellRenderer<Note> {
    private val dateFormat = DateHelper.dateTimeFormat

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
                SELECTED_SIMPLE_CELL_ATTRIBUTES
            } else {
                REGULAR_ATTRIBUTES
            },
        )

        component.append(
            " - ${dateFormat.format(Date(value.updatedAt))}",
            if (isSelected) {
                SELECTED_SIMPLE_CELL_ATTRIBUTES
            } else {
                GRAYED_ATTRIBUTES
            },
        )

        if (isSelected) {
            component.background = list?.selectionBackground
            component.foreground = list?.selectionForeground
            component.isOpaque = true
        }

        panel.add(component, CENTER)
        return panel
    }
}
