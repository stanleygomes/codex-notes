package org.jetbrains.plugins.template.ui.noteslist

import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import org.jetbrains.plugins.template.dto.Note
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JList

class NoteListItemComponent {

    fun build(theList: JList<out Note>?, note: Note?, isSelected: Boolean): JPanel {
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

        panel.add(leftPanel, BorderLayout.CENTER)

        return panel
    }
}
