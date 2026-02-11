package org.jetbrains.plugins.template.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.plugins.template.dto.Note
import java.io.File

class NoteFileService {

    fun create(project: Project): VirtualFile? {
        val title = generateUntitledName(project)
        val fileName = "$title.md"
        val tempDir = System.getProperty("java.io.tmpdir")
        val file = File(tempDir, fileName)
        file.writeText("")

        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file)

        if (virtualFile != null) {
            NoteStorageService.getInstance(project).addNote(project, title, virtualFile.path)
            FileEditorManager.getInstance(project).openFile(virtualFile, true)
        }

        return virtualFile
    }

    private fun generateUntitledName(project: Project): String {
        val notes = NoteStorageService.getInstance(project).getAllNotes()

        val untitledNumbers = notes.mapNotNull { note ->
            if (note.title.startsWith("Untitled ")) {
                note.title.substringAfter("Untitled ").toIntOrNull()
            } else null
        }

        val nextNumber = (untitledNumbers.maxOrNull() ?: 0) + 1
        return "Untitled $nextNumber"
    }

    fun openNote(project: Project, note: Note) {
        val virtualFile = LocalFileSystem.getInstance().findFileByPath(note.filePath)

        if (virtualFile != null) {
            FileEditorManager.getInstance(project).openFile(virtualFile, true)
        }
    }
}
