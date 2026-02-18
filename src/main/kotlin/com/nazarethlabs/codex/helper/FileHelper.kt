package com.nazarethlabs.codex.helper

import java.io.File
import java.io.FileInputStream

object FileHelper {
    fun createFile(
        dir: String,
        fileName: String,
    ): File = createFileWithContent(dir, fileName, "")

    fun createFileWithContent(
        dir: String,
        fileName: String,
        content: String,
    ): File {
        val file = File(dir, fileName)
        file.parentFile?.mkdirs()
        file.writeText(content)
        return file
    }

    fun deleteFile(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
    }

    fun renameFile(
        oldPath: String,
        newName: String,
    ): Boolean {
        val oldFile = File(oldPath)
        val parentDir = oldFile.parentFile ?: return false
        val newFile = File(parentDir, newName)
        return oldFile.exists() && oldFile.renameTo(newFile)
    }

    fun fileExists(
        parent: String,
        name: String,
    ): Boolean = File(parent, name).exists()

    fun getNewFilePath(
        oldPath: String,
        newName: String,
    ): String {
        val oldFile = File(oldPath)
        val parentDir = oldFile.parentFile ?: return newName
        return File(parentDir, newName).absolutePath
    }

    fun getParentPath(filePath: String): String {
        val file = File(filePath)
        return file.parent ?: filePath
    }

    fun getDefaultNotesDir(): String = File(System.getProperty("user.home"), ".codex-notes").absolutePath

    fun ensureDirectoryExists(path: String): File {
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun getFileName(filePath: String): String {
        val file = File(filePath)
        return file.name
    }

    fun isFile(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists() && file.isFile
    }

    fun getFileInputStream(filePath: String): FileInputStream = FileInputStream(File(filePath))

    fun getLastModified(filePath: String): Long {
        val file = File(filePath)
        return file.lastModified()
    }

    fun readText(filePath: String): String {
        val file = File(filePath)
        if (!file.exists()) return ""
        return file.readText()
    }
}
