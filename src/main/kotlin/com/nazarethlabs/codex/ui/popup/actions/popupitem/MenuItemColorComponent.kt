package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.icons.AllIcons
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.service.note.ChangeNoteColorService
import com.nazarethlabs.codex.ui.component.MenuComponent
import com.nazarethlabs.codex.ui.component.MenuItemComponent
import javax.swing.JMenu

class MenuItemColorComponent {
    private val changeNoteColorService = ChangeNoteColorService()
    private val menuItemComponent = MenuItemComponent()
    private val menuComponent = MenuComponent()

    fun build(note: Note): JMenu {
        val colorMenu =
            menuComponent.build(
                text = MyBundle.message("note.context.menu.change.color"),
                icon = AllIcons.Actions.Colors,
            )

        NoteColorEnum.entries.forEach { color ->
            val colorItem =
                menuItemComponent.build(
                    text = MyBundle.message(color.displayNameKey),
                    action = { changeNoteColorService.changeColor(note, color) },
                )

            colorMenu.add(colorItem)
        }

        return colorMenu
    }
}
