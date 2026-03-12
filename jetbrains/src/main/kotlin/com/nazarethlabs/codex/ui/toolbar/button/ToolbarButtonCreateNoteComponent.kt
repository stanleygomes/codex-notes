package com.nazarethlabs.codex.ui.toolbar.button

import com.intellij.icons.AllIcons.General.Add
import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.helper.MessageHelper.getMessage
import com.nazarethlabs.codex.service.note.CreateNoteService
import com.nazarethlabs.codex.ui.component.ButtonComponent
import javax.swing.JButton

class ToolbarButtonCreateNoteComponent {
    fun build(project: Project): JButton {
        val createNote =
            ButtonComponent()
                .build(Add, getMessage("toolbar.note.create"))

        createNote.addActionListener {
            CreateNoteService()
                .create(project)
        }

        return createNote
    }
}
