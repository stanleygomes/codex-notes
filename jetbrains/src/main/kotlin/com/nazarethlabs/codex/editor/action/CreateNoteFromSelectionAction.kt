package com.nazarethlabs.codex.editor.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.codex.MyBundle
import com.nazarethlabs.codex.service.note.CreateNoteService
import com.nazarethlabs.codex.service.settings.NotesSettingsService

class CreateNoteFromSelectionAction : BaseCreateNoteAction() {
    private val createNoteService = CreateNoteService()
    private val notesSettingsService = NotesSettingsService()

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val hasSelection = editor?.selectionModel?.hasSelection() ?: false
        e.presentation.isEnabledAndVisible = hasSelection
    }

    override fun createNote(e: AnActionEvent): VirtualFile? {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return null
        val selectedText = editor.selectionModel.selectedText ?: return null

        val title = createNoteService.generateTitle()
        val extension = notesSettingsService.getDefaultFileExtension()
        val header = MyBundle.message("action.create.note.from.selection.content.header")
        val content = "$header$selectedText"
        return createNoteService.createWithContent(e.project!!, title, extension, content)
    }

    override fun getSuccessTitleKey(): String = "action.create.note.from.selection.success.title"

    override fun getSuccessMessageKey(): String = "action.create.note.from.selection.success.message"

    override fun getErrorTitleKey(): String = "action.create.note.from.selection.error.title"

    override fun getErrorMessageKey(): String = "action.create.note.from.selection.error.message"
}
