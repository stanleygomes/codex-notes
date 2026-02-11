package org.jetbrains.plugins.template.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.plugins.template.helper.FileHelper
import org.jetbrains.plugins.template.helper.NoteNameHelper
import org.jetbrains.plugins.template.repository.NoteStorageRepository

class CreateNoteService {

    fun create(project: Project): VirtualFile? {
        val notes = NoteStorageRepository
            .getInstance(project)
            .getAllNotes()

        val title = NoteNameHelper
            .generateUntitledName(notes)

        val fileName = "$title.md"
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
