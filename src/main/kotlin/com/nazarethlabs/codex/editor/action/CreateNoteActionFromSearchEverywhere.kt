package com.nazarethlabs.codex.editor.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.codex.service.note.CreateNoteService

class CreateNoteActionFromSearchEverywhere : BaseCreateNoteAction() {
    private val createNoteService = CreateNoteService()

    override fun createNote(e: AnActionEvent): VirtualFile? {
        val project = e.project ?: return null
        return createNoteService.create(project)
    }

    override fun getSuccessTitleKey(): String = "action.create.note.success.title"

    override fun getSuccessMessageKey(): String = "action.create.note.success.message"

    override fun getErrorTitleKey(): String = "action.create.note.error.title"

    override fun getErrorMessageKey(): String = "action.create.note.error.message"
}
