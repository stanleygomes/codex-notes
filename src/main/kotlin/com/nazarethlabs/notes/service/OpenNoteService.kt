package com.nazarethlabs.notes.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.nazarethlabs.notes.dto.Note
import com.nazarethlabs.notes.repository.NoteStorageRepository

class OpenNoteService {
    fun open(
        project: Project,
        note: Note,
    ) {
        val virtualFile =
            LocalFileSystem
                .getInstance()
                .findFileByPath(note.filePath)

        if (virtualFile != null) {
            FileEditorManager
                .getInstance(project)
                .openFile(virtualFile, true)

            NoteStorageRepository
                .getInstance()
                .updateNote(note.id)
        }
    }
}
