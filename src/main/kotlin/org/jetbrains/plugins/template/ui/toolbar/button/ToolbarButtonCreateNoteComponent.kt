package org.jetbrains.plugins.template.ui.toolbar.button

import com.intellij.icons.AllIcons.Actions.AddFile
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.template.helper.MessageHelper.getMessage
import org.jetbrains.plugins.template.service.CreateNoteService
import org.jetbrains.plugins.template.ui.component.ButtonComponent
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
