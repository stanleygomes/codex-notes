package com.nazarethlabs.codex.service.note

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.repository.NoteStorageRepository

class OpenNoteService {
    fun open(
        project: Project,
        note: Note,
    ) {
        val virtualFile = findVirtualFile(note.filePath)

        if (virtualFile != null) {
            openFile(project, virtualFile)
            updateNote(note)
        }
    }

    private fun findVirtualFile(filePath: String): VirtualFile? = LocalFileSystem.getInstance().findFileByPath(filePath)

    private fun openFile(
        project: Project,
        virtualFile: VirtualFile,
    ) {
        FileEditorManager.getInstance(project).openFile(virtualFile, true)
    }

    private fun updateNote(note: Note) {
        NoteStorageRepository.getInstance().updateNote(note.id)
    }
}
