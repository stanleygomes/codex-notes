package org.jetbrains.plugins.template.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.plugins.template.dto.Note
import java.io.File

class NoteFileService {

    fun create(project: Project): VirtualFile? {
        val timestamp = System.currentTimeMillis()
        val fileName = "note_$timestamp.md"
        val tempDir = System.getProperty("java.io.tmpdir")
        val file = File(tempDir, fileName)
        file.writeText("")

        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file)

        if (virtualFile != null) {
            NoteStorageService.getInstance(project).addNote(project, fileName, virtualFile.path)
            FileEditorManager.getInstance(project).openFile(virtualFile, true)
        }

        return virtualFile
    }

    fun openNote(project: Project, note: Note) {
        val virtualFile = LocalFileSystem.getInstance().findFileByPath(note.filePath)

        if (virtualFile != null) {
            FileEditorManager.getInstance(project).openFile(virtualFile, true)
        }
    }
}

