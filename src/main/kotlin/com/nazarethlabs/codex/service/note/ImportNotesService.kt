package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.helper.FileHelper
import java.io.File

class ImportNotesService {
    fun import(
        sourceFolderPath: String,
        destinationFolderPath: String,
    ): List<File> {
        val sourceFolder = File(sourceFolderPath)
        val files = sourceFolder.listFiles()?.filter { it.isFile } ?: emptyList()

        if (files.isEmpty()) {
            return emptyList()
        }

        FileHelper.ensureDirectoryExists(destinationFolderPath)

        val importedFiles = mutableListOf<File>()
        for (file in files) {
            val destinationFile = File(destinationFolderPath, file.name)
            if (!destinationFile.exists()) {
                file.copyTo(destinationFile)
                importedFiles.add(destinationFile)
            }
        }

        return importedFiles
    }
}
