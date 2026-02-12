package com.nazarethlabs.notes.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.notes.helper.FileHelper
import com.nazarethlabs.notes.helper.NoteNameHelper
import com.nazarethlabs.notes.repository.NoteStorageRepository
import com.nazarethlabs.notes.repository.NotesSettingsRepository

class CreateNoteService {

    fun create(project: Project): VirtualFile? {
        val notes = NoteStorageRepository
            .getInstance(project)
            .getAllNotes()

        val title = NoteNameHelper
            .generateUntitledName(notes)

        val extension = NotesSettingsRepository
            .getInstance(project)
            .getDefaultFileExtension()

        val fileName = "$title$extension"
        val tempDir = FileHelper.getTempDir()
        val file = FileHelper.createFile(tempDir, fileName)

        val virtualFile = LocalFileSystem
            .getInstance()
            .findFileByIoFile(file)

        if (virtualFile != null) {
            NoteStorageRepository
                .getInstance(project)
                .addNote(project, title, virtualFile.path)

            FileEditorManager
                .getInstance(project)
                .openFile(virtualFile, true)
        }

        return virtualFile
    }
}
