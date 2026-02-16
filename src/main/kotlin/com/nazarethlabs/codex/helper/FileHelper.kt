package com.nazarethlabs.codex.helper

import java.io.File
import java.io.IOException

object FileHelper {
    fun createFile(
        dir: String,
        fileName: String,
    ): File? =
        try {
            val file = File(dir, fileName)
            file.writeText("")
            file
        } catch (_: IOException) {
            null
        } catch (_: SecurityException) {
            null
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
        val newFile = File(oldFile.parent, newName)
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
        return File(oldFile.parent, newName).absolutePath
    }

    fun getParentPath(filePath: String): String {
        val file = File(filePath)
        return file.parent ?: filePath
    }

    fun getTempDir(): String = System.getProperty("java.io.tmpdir")
}
