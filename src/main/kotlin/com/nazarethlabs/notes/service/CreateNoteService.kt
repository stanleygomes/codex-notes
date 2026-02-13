package com.nazarethlabs.notes.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.notes.helper.FileHelper
import com.nazarethlabs.notes.helper.NoteNameHelper
import com.nazarethlabs.notes.repository.NoteStorageRepository

class CreateNoteService {
    fun create(project: Project): VirtualFile? {
        val notes =
            NoteStorageRepository
                .getInstance()
                .getAllNotes()

        val title =
            NoteNameHelper
                .generateUntitledName(notes)

        val extension =
            NotesSettingsService()
                .getDefaultFileExtension()

        val fileName = "$title$extension"
        val tempDir = FileHelper.getTempDir()
        val file = FileHelper.createFile(tempDir, fileName)

        val virtualFile =
            LocalFileSystem
                .getInstance()
                .findFileByIoFile(file)

        if (virtualFile != null) {
            NoteStorageRepository
                .getInstance()
                .addNote(title, virtualFile.path)

            FileEditorManager
                .getInstance(project)
                .openFile(virtualFile, true)
        }

        return virtualFile
    }
}
