package org.jetbrains.plugins.template.ui.noteslist

import com.intellij.util.ui.JBUI
import org.jetbrains.plugins.template.dto.Note
import java.awt.Color
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.BorderLayout

class NoteListItemComponent(note: Note, backgroundColor: Color, foregroundColor: Color) : JPanel(BorderLayout()) {

    val titleLabel = JLabel(note.title)

    init {
        border = JBUI.Borders.empty(5, 10, 5, 10)
        isOpaque = true
        background = backgroundColor
        foreground = foregroundColor
        titleLabel.foreground = foregroundColor
        add(titleLabel, BorderLayout.CENTER)
    }
}
