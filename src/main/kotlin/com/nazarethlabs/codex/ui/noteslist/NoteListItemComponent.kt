package com.nazarethlabs.codex.ui.noteslist

import com.intellij.icons.AllIcons.Nodes.Bookmark
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.ColorHelper
import com.nazarethlabs.codex.helper.TimeHelper
import java.awt.BorderLayout
import java.awt.BorderLayout.EAST
import java.awt.BorderLayout.WEST
import java.awt.FlowLayout
import java.awt.FlowLayout.LEFT
import java.awt.FlowLayout.RIGHT
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel

class NoteListItemComponent {
    private val colorHelper = ColorHelper()

    fun build(
        theList: JList<out Note>?,
        note: Note?,
        isSelected: Boolean,
    ): JPanel {
        if (note == null) {
            return JPanel()
        }

        val (backgroundColor, foregroundColor) = colorHelper.calculateColors(theList, note, isSelected)
        val panel = JPanel(BorderLayout())

        panel.border = JBUI.Borders.empty(5, 10, 5, 10)
        panel.isOpaque = true
        panel.background = backgroundColor
        panel.foreground = foregroundColor

        val leftPanel = JPanel(FlowLayout(LEFT, 5, 0))
        leftPanel.isOpaque = false

        if (note.isFavorite) {
            val starIcon = JLabel(Bookmark)
            leftPanel.add(starIcon)
        }

        val titleLabel = JLabel(note.title)
        titleLabel.foreground = foregroundColor
        leftPanel.add(titleLabel)

        panel.add(leftPanel, WEST)

        val rightPanel = JPanel(FlowLayout(RIGHT, 5, 0))
        rightPanel.isOpaque = false

        val dateLabel = JLabel(TimeHelper.formatTimeAgo(note.updatedAt))
        dateLabel.foreground = colorHelper.calculateDateLabelColor(theList, note, isSelected)
        dateLabel.font = dateLabel.font.deriveFont(dateLabel.font.size - 3f)
        rightPanel.add(dateLabel)

        panel.add(rightPanel, EAST)

        return panel
    }
}
