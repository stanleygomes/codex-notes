package com.nazarethlabs.notes.ui.noteslist

import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.helper.TimeHelper
import java.awt.BorderLayout
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel

class NoteListItemComponent {
    fun build(
        theList: JList<out Note>?,
        note: Note?,
        isSelected: Boolean,
    ): JPanel {
        if (note == null) {
            return JPanel()
        }

        val backgroundColor = if (isSelected) theList!!.selectionBackground else theList!!.background
        val foregroundColor = if (isSelected) theList.selectionForeground else theList.foreground
        val panel = JPanel(BorderLayout())

        panel.border = JBUI.Borders.empty(5, 10, 5, 10)
        panel.isOpaque = true
        panel.background = backgroundColor
        panel.foreground = foregroundColor

        val leftPanel = JPanel(FlowLayout(FlowLayout.LEFT, 5, 0))
        leftPanel.isOpaque = false

        if (note.isFavorite) {
            val starIcon = JLabel(AllIcons.Nodes.Bookmark)
            leftPanel.add(starIcon)
        }

        val titleLabel = JLabel(note.title)
        titleLabel.foreground = foregroundColor
        leftPanel.add(titleLabel)

        panel.add(leftPanel, BorderLayout.WEST)

        val rightPanel = JPanel(FlowLayout(FlowLayout.RIGHT, 5, 0))
        rightPanel.isOpaque = false

        val dateLabel = JLabel(TimeHelper.formatTimeAgo(note.updatedAt))
        dateLabel.foreground = Color.GRAY
        dateLabel.font = dateLabel.font.deriveFont(dateLabel.font.size - 3f)
        rightPanel.add(dateLabel)

        panel.add(rightPanel, BorderLayout.EAST)

        return panel
    }
}
