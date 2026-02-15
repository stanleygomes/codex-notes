package com.nazarethlabs.notes.service.note

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.notes.helper.FileHelper
import com.nazarethlabs.notes.helper.NoteNameHelper
import com.nazarethlabs.notes.repository.NoteStorageRepository
import com.nazarethlabs.notes.service.settings.NotesSettingsService
import java.io.File

class CreateNoteService {
    fun create(project: Project): VirtualFile? {
        val title = generateTitle()
        val extension = getExtension()
        val fileName = createFileName(title, extension)
        val file = createTempFile(fileName)
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file)

        if (virtualFile != null) {
            addNoteAndOpen(project, title, virtualFile)
        }

        return virtualFile
    }

    private fun generateTitle(): String {
        val notes = NoteStorageRepository.getInstance().getAllNotes()
        return NoteNameHelper.generateUntitledName(notes)
    }

    private fun getExtension(): String = NotesSettingsService().getDefaultFileExtension()

    private fun createFileName(
        title: String,
        extension: String,
    ): String = "$title$extension"

    private fun createTempFile(fileName: String): File {
        val tempDir = FileHelper.getTempDir()
        return FileHelper.createFile(tempDir, fileName)
    }

    private fun addNoteAndOpen(
        project: Project,
        title: String,
        virtualFile: VirtualFile,
    ) {
        NoteStorageRepository.getInstance().addNote(title, virtualFile.path)
        FileEditorManager.getInstance(project).openFile(virtualFile, true)
    }
}
