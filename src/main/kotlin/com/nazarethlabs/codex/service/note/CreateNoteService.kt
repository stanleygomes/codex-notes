package com.nazarethlabs.codex.service.note

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.codex.helper.DialogHelper
import com.nazarethlabs.codex.helper.FileHelper
import com.nazarethlabs.codex.helper.MessageHelper
import com.nazarethlabs.codex.helper.NoteNameHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import java.io.File

class CreateNoteService {
    fun create(project: Project): VirtualFile? {
        val title = generateTitle()
        val extension = getExtension()
        val fileName = createFileName(title, extension)
        val file = createTempFile(fileName)

        if (file == null) {
            notifyCreateFailed(project)
            return null
        }

        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)

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

    private fun createTempFile(fileName: String): File? {
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

    private fun notifyCreateFailed(project: Project) {
        DialogHelper.showErrorDialog(
            project,
            MessageHelper.getMessage("error.note.create.failed.message"),
            MessageHelper.getMessage("error.note.create.failed.title"),
        )
    }
}
