package com.nazarethlabs.codex.helper

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipHelper {
    fun createZipFromFiles(
        files: List<File>,
        outputPath: String,
    ): File {
        val zipFile = File(outputPath)
        zipFile.parentFile?.mkdirs()

        ZipOutputStream(FileOutputStream(zipFile)).use { zipOut ->
            files.filter { it.exists() && it.isFile }.forEach { file ->
                FileInputStream(file).use { fis ->
                    val entry = ZipEntry(file.name)
                    zipOut.putNextEntry(entry)
                    fis.copyTo(zipOut)
                    zipOut.closeEntry()
                }
            }
        }

        return zipFile
    }
}
