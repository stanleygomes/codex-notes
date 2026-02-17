package com.nazarethlabs.codex.ui.popup.actions.batchpopupitem

import com.intellij.icons.AllIcons
import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.service.note.ChangeNotesColorService
import javax.swing.JMenu
import javax.swing.JMenuItem

class MenuItemBatchColorComponent {
    private val changeNotesColorService = ChangeNotesColorService()

    fun build(notes: List<Note>): JMenu {
        val colorMenu =
            JMenu(
                MessageHelper.getMessage(
                    "note.context.menu.change.color.all",
                    notes.size.toString(),
                ),
            )

        colorMenu.icon = AllIcons.Actions.Colors
        colorMenu.border = JBUI.Borders.empty(5, 10, 5, 10)

        NoteColorEnum.entries.forEach { color ->
            val colorItem = JMenuItem(MessageHelper.getMessage(color.displayNameKey))
            colorItem.border = JBUI.Borders.empty(5, 10, 5, 10)

            colorItem.addActionListener {
                changeNotesColorService.changeColorAll(notes, color)
            }

            colorMenu.add(colorItem)
        }

        return colorMenu
    }
}
