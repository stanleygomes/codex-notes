package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.service.note.ChangeNoteColorService
import javax.swing.JMenu
import javax.swing.JMenuItem

class MenuItemColorComponent {
    private val changeNoteColorService = ChangeNoteColorService()

    fun build(note: Note): JMenu {
        val colorMenu = JMenu(MyBundle.message("note.context.menu.change.color"))
        colorMenu.icon = AllIcons.Actions.Colors
        colorMenu.border = JBUI.Borders.empty(5, 10, 5, 10)

        NoteColorEnum.entries.forEach { color ->
            val colorItem = JMenuItem(MyBundle.message(color.displayNameKey))
            colorItem.border = JBUI.Borders.empty(5, 10, 5, 10)

            colorItem.addActionListener {
                changeNoteColorService.changeColor(note, color)
            }

            colorMenu.add(colorItem)
        }

        return colorMenu
    }
}
