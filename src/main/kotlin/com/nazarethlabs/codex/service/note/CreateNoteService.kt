package com.nazarethlabs.codex.service.note

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.helper.NoteNameHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import java.io.File

class CreateNoteService {
    fun create(project: Project): VirtualFile? {
        val title = generateTitle()
        val extension = getExtension()
        return createNoteFileAndOpen(project, title, extension, "")
    }

    fun createWithContent(
        project: Project,
        title: String,
        extension: String,
        content: String,
    ): VirtualFile? = createNoteFileAndOpen(project, title, extension, content)

    private fun createNoteFileAndOpen(
        project: Project,
        title: String,
        extension: String,
        content: String,
    ): VirtualFile? {
        val fileName = createFileName(title, extension)
        val file = createNoteFile(fileName, content)
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)

        if (virtualFile != null) {
            addNoteAndOpen(project, title, virtualFile)
        }

        return virtualFile
    }

    fun generateTitle(): String {
        val notes = NoteStorageRepository.getInstance().getAllNotes()
        return NoteNameHelper.generateUntitledName(notes)
    }

    private fun getExtension(): String = NotesSettingsService().getDefaultFileExtension()

    private fun createFileName(
        title: String,
        extension: String,
    ): String = "$title$extension"

    private fun createNoteFile(
        fileName: String,
        content: String,
    ): File {
        val notesDirectory = NotesSettingsService().getNotesDirectory()
        return FileHelper.createFileWithContent(notesDirectory, fileName, content)
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
