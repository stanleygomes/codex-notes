package org.jetbrains.plugins.template.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import org.jetbrains.plugins.template.dto.Note
import org.jetbrains.plugins.template.repository.NoteStorageRepository

class OpenNoteService {

    fun open(project: Project, note: Note) {
        val virtualFile = LocalFileSystem
            .getInstance()
            .findFileByPath(note.filePath)

        if (virtualFile != null) {
            FileEditorManager
                .getInstance(project)
                .openFile(virtualFile, true)

            NoteStorageRepository
                .getInstance(project)
                .updateNote(project, note.id)
        }
    }
}
