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
        project: Project?,
        title: String,
        extension: String,
        content: String,
        openNote: Boolean = true,
    ): VirtualFile? = createNoteFileAndOpen(project, title, extension, content, openNote)

    private fun createNoteFileAndOpen(
        project: Project?,
        title: String,
        extension: String,
        content: String,
        openNote: Boolean = true,
    ): VirtualFile? {
        val fileName = createFileName(title, extension)
        val file = createNoteFile(fileName, content)
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)

        if (virtualFile != null) {
            addNote(title, virtualFile, project, openNote)
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
    ): String = "${FileHelper.sanitizeFileName(title)}$extension"

    private fun createNoteFile(
        fileName: String,
        content: String,
    ): File {
        val notesDirectory = NotesSettingsService().getNotesDirectory()
        return FileHelper.createFileWithContent(notesDirectory, fileName, content)
    }

    private fun addNote(
        title: String,
        virtualFile: VirtualFile,
        project: Project?,
        openNote: Boolean,
    ) {
        NoteStorageRepository.getInstance().addNote(title, virtualFile.path)
        if (openNote) {
            project?.let { proj ->
                FileEditorManager.getInstance(proj).openFile(virtualFile, true)
            }
        }
    }
}
