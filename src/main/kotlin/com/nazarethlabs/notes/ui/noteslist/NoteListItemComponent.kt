package com.nazarethlabs.notes.ui.noteslist

import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.enum.NoteColorEnum
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

        val backgroundColor =
            if (isSelected) {
                theList!!.selectionBackground
            } else {
                if (note.color != NoteColorEnum.NONE) note.color.color else theList!!.background
            }

        val foregroundColor =
            if (isSelected) {
                theList!!.selectionForeground
            } else {
                val bgColor = if (note.color != NoteColorEnum.NONE) note.color.color else theList!!.background
                if (isBackgroundTooLight(bgColor)) Color.BLACK else theList!!.foreground
            }
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
        // Adjust date label color based on background for better contrast
        dateLabel.foreground =
            if (isSelected) {
                // When selected, use a slightly dimmed version of selection foreground
                theList!!.selectionForeground.darker()
            } else {
                val bgColor = if (note.color != NoteColorEnum.NONE) note.color.color else theList!!.background
                if (isBackgroundTooLight(bgColor)) {
                    // Dark background for date when main background is light
                    Color(64, 64, 64) // Dark gray
                } else {
                    // Light gray for date when main background is dark
                    Color(128, 128, 128)
                }
            }
        dateLabel.font = dateLabel.font.deriveFont(dateLabel.font.size - 3f)
        rightPanel.add(dateLabel)

        panel.add(rightPanel, BorderLayout.EAST)

        return panel
    }

    private fun isBackgroundTooLight(color: Color): Boolean {
        // Calculate the brightness of the color using the luminance formula
        val brightness = (color.red * 0.299 + color.green * 0.587 + color.blue * 0.114)
        // If brightness is greater than 186, the color is considered too light
        return brightness > 186
    }
}
