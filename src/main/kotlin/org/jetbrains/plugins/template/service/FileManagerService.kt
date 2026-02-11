package org.jetbrains.plugins.template.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File

class FileManagerService {

    fun create(project: Project) {
        val tempDir = System.getProperty("java.io.tmpdir")
        val fileName = "note_${System.currentTimeMillis()}.md"
        val file = File(tempDir, fileName)
        file.writeText("")
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file)
        if (virtualFile != null) {
            FileEditorManager.getInstance(project).openFile(virtualFile, true)
        }
    }
}
