package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.util.ui.JBUI
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.helper.OpenFolderHelper
import java.io.File
import javax.swing.JMenuItem

class MenuItemOpenFileLocationComponent {
    fun build(note: Note): JMenuItem {
        val openFileLocationItem = JMenuItem(MyBundle.message("note.context.menu.open.file.location"))
        openFileLocationItem.border = JBUI.Borders.empty(5, 10, 5, 10)

        openFileLocationItem.addActionListener {
            val parentPath = File(note.filePath).parentFile?.absolutePath
            if (parentPath != null) {
                OpenFolderHelper.openFolder(parentPath)
            }
        }

        return openFileLocationItem
    }
}
