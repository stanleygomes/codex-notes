package com.nazarethlabs.notes.ui.toolbar.button

import com.intellij.icons.AllIcons.Actions.AddFile
import com.intellij.openapi.project.Project
import com.nazarethlabs.notes.helper.MessageHelper.getMessage
import com.nazarethlabs.notes.service.CreateNoteService
import com.nazarethlabs.notes.ui.component.ButtonComponent
import javax.swing.JButton

class ToolbarButtonCreateNoteComponent {

    fun build(project: Project): JButton {
        val createNote = ButtonComponent()
            .build(AddFile, getMessage("toolbar.note.create"))

        createNote.addActionListener {
            CreateNoteService()
                .create(project)
        }

        return createNote
    }
}
