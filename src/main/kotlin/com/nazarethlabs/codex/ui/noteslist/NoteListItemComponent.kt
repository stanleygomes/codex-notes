package com.nazarethlabs.codex.ui.noteslist

import com.intellij.icons.AllIcons.Nodes.Bookmark
import com.intellij.ui.JBColor
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

        val panel = renderMainPanel(isSelected, backgroundColor, foregroundColor)

        renderColorStripe(panel, note)

        val contentPanel = JPanel(BorderLayout())
        contentPanel.isOpaque = false
        contentPanel.border = if (note.color != NONE) JBUI.Borders.emptyLeft(6) else JBUI.Borders.empty()

        renderTopRow(contentPanel, note, foregroundColor, secondaryColor)
        renderContentPreview(contentPanel, contentPreview, secondaryColor)

        panel.add(contentPanel, CENTER)

        return panel
    }

    private fun renderMainPanel(
        isSelected: Boolean,
        backgroundColor: java.awt.Color,
        foregroundColor: java.awt.Color,
    ): JPanel {
        val panel = JPanel(BorderLayout())
        panel.border =
            if (isSelected) {
                JBUI.Borders.empty(5, 0, 0, 0)
            } else {
                JBUI.Borders.compound(
                    JBUI.Borders.empty(5, 0, 0, 0),
                    JBUI.Borders.customLine(JBColor.border(), 0, 0, 1, 0),
                )
            }
        panel.isOpaque = true
        panel.background = backgroundColor
        panel.foreground = foregroundColor
        return panel
    }

    private fun renderColorStripe(
        panel: JPanel,
        note: Note,
    ) {
        if (note.color != NONE) {
            val colorStripe = JPanel()
            colorStripe.preferredSize = Dimension(4, 0)
            colorStripe.background = note.color.color
            colorStripe.isOpaque = true
            panel.add(colorStripe, WEST)
        }
    }

    private fun renderTopRow(
        contentPanel: JPanel,
        note: Note,
        foregroundColor: java.awt.Color,
        secondaryColor: java.awt.Color,
    ) {
        val topRow = JPanel(BorderLayout())
        topRow.isOpaque = false

        val leftPanel = JPanel(FlowLayout(LEFT, 5, 0))
        leftPanel.isOpaque = false
        renderTitle(leftPanel, note, foregroundColor)
        topRow.add(leftPanel, WEST)

        val rightPanel = JPanel(FlowLayout(RIGHT, 5, 0))
        rightPanel.isOpaque = false
        renderDate(rightPanel, note, secondaryColor)
        topRow.add(rightPanel, EAST)

        contentPanel.add(topRow, NORTH)
    }

    private fun renderTitle(
        leftPanel: JPanel,
        note: Note,
        foregroundColor: java.awt.Color,
    ) {
        if (note.isFavorite) {
            val starIcon = JLabel(Bookmark)
            leftPanel.add(starIcon)
        }

        val titleLabel = JLabel(note.title)
        titleLabel.foreground = foregroundColor
        leftPanel.add(titleLabel)
    }

    private fun renderDate(
        rightPanel: JPanel,
        note: Note,
        secondaryColor: java.awt.Color,
    ) {
        val dateLabel = JLabel(TimeHelper.formatTimeAgo(note.updatedAt))
        dateLabel.foreground = secondaryColor
        dateLabel.font = dateLabel.font.deriveFont(dateLabel.font.size - 3f)
        dateLabel.border = JBUI.Borders.empty(1, 0, 0, 0)
        rightPanel.add(dateLabel)
    }

    private fun renderContentPreview(
        contentPanel: JPanel,
        contentPreview: String,
        secondaryColor: java.awt.Color,
    ) {
        if (contentPreview.isNotEmpty()) {
            val previewLabel = JLabel(contentPreview)
            previewLabel.foreground = secondaryColor
            previewLabel.font = previewLabel.font.deriveFont(java.awt.Font.BOLD, previewLabel.font.size - 3f)
            previewLabel.border = JBUI.Borders.empty(2, 5, 10, 5)
            contentPanel.add(previewLabel, CENTER)
        } else {
            val placeholderLabel = JLabel("")
            placeholderLabel.border = JBUI.Borders.empty(2, 5, 10, 5)
            contentPanel.add(placeholderLabel, CENTER)
        }
    }
}
