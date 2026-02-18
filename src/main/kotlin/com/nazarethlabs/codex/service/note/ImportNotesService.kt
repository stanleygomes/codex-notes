package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.helper.FileHelper
import java.io.File

class ImportNotesService {
    fun importFiles(
        files: List<File>,
        notesDirectory: String,
    ): List<File> {
        FileHelper.ensureDirectoryExists(notesDirectory)

        val importedFiles = mutableListOf<File>()
        for (file in files) {
            if (file.exists() && file.isFile) {
                val content = file.readText()
                val fileName = resolveUniqueFileName(notesDirectory, file.name)
                val createdFile = FileHelper.createFileWithContent(notesDirectory, fileName, content)
                importedFiles.add(createdFile)
            }
        }

        return importedFiles
    }

    private fun resolveUniqueFileName(
        directory: String,
        originalName: String,
    ): String {
        if (!FileHelper.fileExists(directory, originalName)) {
            return originalName
        }

        val nameWithoutExtension = originalName.substringBeforeLast(".")
        val extension = if (originalName.contains(".")) ".${originalName.substringAfterLast(".")}" else ""

        var counter = 1
        var candidate: String
        do {
            candidate = "$nameWithoutExtension ($counter)$extension"
            counter++
        } while (FileHelper.fileExists(directory, candidate))

        return candidate
    }
}
