package com.nazarethlabs.codex.helper

import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipHelper {
    fun createZipFromFiles(
        filePaths: List<String>,
        outputPath: String,
    ): String {
        val parentPath = FileHelper.getParentPath(outputPath)
        FileHelper.ensureDirectoryExists(parentPath)

        ZipOutputStream(FileOutputStream(outputPath)).use { zipOut ->
            filePaths.filter { FileHelper.isFile(it) }.forEach { filePath ->
                FileHelper.getFileInputStream(filePath).use { fis ->
                    val entry = ZipEntry(FileHelper.getFileName(filePath))
                    zipOut.putNextEntry(entry)
                    fis.copyTo(zipOut)
                    zipOut.closeEntry()
                }
            }
        }

        return outputPath
    }
}
