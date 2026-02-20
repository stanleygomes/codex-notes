package com.nazarethlabs.codex.service.note

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class FileEditorLifecycleService {
    fun closeAndRenameFile(
        project: Project,
        filePath: String,
        newFileName: String,
    ): VirtualFile? {
        val virtualFile = findVirtualFile(filePath) ?: return null

        closeFile(project, virtualFile)
        renameFile(virtualFile, newFileName)
        refreshFile(virtualFile)

        return virtualFile
    }

    private fun findVirtualFile(filePath: String): VirtualFile? = LocalFileSystem.getInstance().findFileByPath(filePath)

    private fun closeFile(
        project: Project,
        virtualFile: VirtualFile,
    ) {
        FileEditorManager.getInstance(project).closeFile(virtualFile)
    }

    private fun renameFile(
        virtualFile: VirtualFile,
        newFileName: String,
    ) {
        ApplicationManager.getApplication().runWriteAction {
            virtualFile.rename(this, newFileName)
        }
    }

    private fun refreshFile(virtualFile: VirtualFile) {
        virtualFile.refresh(false, false)
    }
}

