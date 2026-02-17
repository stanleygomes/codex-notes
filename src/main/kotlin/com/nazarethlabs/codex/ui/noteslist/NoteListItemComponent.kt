package com.nazarethlabs.codex.ui.noteslist

import com.intellij.icons.AllIcons.Nodes.Bookmark
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum.NONE
import com.nazarethlabs.codex.helper.ColorHelper
import com.nazarethlabs.codex.helper.TimeHelper
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.EAST
import java.awt.BorderLayout.NORTH
import java.awt.BorderLayout.WEST
import java.awt.Dimension
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
        contentPreview: String = "",
    ): JPanel {
        if (note == null) {
            return JPanel()
        }

        val (backgroundColor, foregroundColor) = colorHelper.calculateColors(theList, isSelected)
        val secondaryColor = colorHelper.calculateSecondaryColor(theList, isSelected)

        val panel = JPanel(BorderLayout())
        panel.border = JBUI.Borders.empty(5, 10, 5, 10)
        panel.isOpaque = true
        panel.background = backgroundColor
        panel.foreground = foregroundColor

        if (note.color != NONE) {
            val colorStripe = JPanel()
            colorStripe.preferredSize = Dimension(4, 0)
            colorStripe.background = note.color.color
            colorStripe.isOpaque = true
            panel.add(colorStripe, WEST)
        }

        val contentPanel = JPanel(BorderLayout())
        contentPanel.isOpaque = false
        contentPanel.border = if (note.color != NONE) JBUI.Borders.emptyLeft(6) else JBUI.Borders.empty()

        val topRow = JPanel(BorderLayout())
        topRow.isOpaque = false

        val leftPanel = JPanel(FlowLayout(LEFT, 5, 0))
        leftPanel.isOpaque = false

        if (note.isFavorite) {
            val starIcon = JLabel(Bookmark)
            leftPanel.add(starIcon)
        }

        val titleLabel = JLabel(note.title)
        titleLabel.foreground = foregroundColor
        titleLabel.font = titleLabel.font.deriveFont(titleLabel.font.size * 1.15f)
        leftPanel.add(titleLabel)

        topRow.add(leftPanel, WEST)

        val rightPanel = JPanel(FlowLayout(RIGHT, 5, 0))
        rightPanel.isOpaque = false

        val dateLabel = JLabel(TimeHelper.formatTimeAgo(note.updatedAt))
        dateLabel.foreground = secondaryColor
        dateLabel.font = dateLabel.font.deriveFont(dateLabel.font.size - 3f)
        rightPanel.add(dateLabel)

        topRow.add(rightPanel, EAST)

        contentPanel.add(topRow, NORTH)

        if (contentPreview.isNotEmpty()) {
            val previewLabel = JLabel(contentPreview)
            previewLabel.foreground = secondaryColor
            previewLabel.font = previewLabel.font.deriveFont(previewLabel.font.size - 1f)
            previewLabel.border = JBUI.Borders.empty(2, 5, 0, 5)
            contentPanel.add(previewLabel, CENTER)
        }

        panel.add(contentPanel, CENTER)

        return panel
    }
}
