package org.jetbrains.plugins.template.ui.noteslist

import com.intellij.util.ui.JBUI
import org.jetbrains.plugins.template.dto.Note
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JList

class NoteListItemComponent {

    fun build(theList: JList<out Note>?, note: Note?, isSelected: Boolean): JPanel {
        if (note == null) {
            return JPanel()
        }

        val titleLabel = JLabel(note.title)
        val backgroundColor = if (isSelected) theList!!.selectionBackground else theList!!.background
        val foregroundColor = if (isSelected) theList.selectionForeground else theList.foreground
        val panel = JPanel(BorderLayout())

        panel.border = JBUI.Borders.empty(5, 10, 5, 10)
        panel.isOpaque = true
        panel.background = backgroundColor
        panel.foreground = foregroundColor
        titleLabel.foreground = foregroundColor

        panel.add(titleLabel, BorderLayout.CENTER)

        return panel
    }
}
