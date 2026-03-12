package com.nazarethlabs.codex.helper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files
import java.util.zip.ZipFile

@RunWith(MockitoJUnitRunner::class)
class ZipHelperTest {
    @Test
    fun `should create zip file when valid files provided`() {
        val tempDir = Files.createTempDirectory("test-zip").toFile()
        val file1 = File(tempDir, "note1.md")
        val file2 = File(tempDir, "note2.md")
        file1.writeText("Content of note 1")
        file2.writeText("Content of note 2")

        val outputPath = File(tempDir, "output.zip").absolutePath
        val zipPath = ZipHelper.createZipFromFiles(listOf(file1.absolutePath, file2.absolutePath), outputPath)
        val zipFile = File(zipPath)

        assertTrue(zipFile.exists())
        ZipFile(zipFile).use { zip ->
            val entries = zip.entries().toList()
            assertEquals(2, entries.size)
            val names = entries.map { it.name }.sorted()
            assertEquals(listOf("note1.md", "note2.md"), names)
        }

        file1.delete()
        file2.delete()
        zipFile.delete()
        tempDir.delete()
    }

    @Test
    fun `should skip non-existent files when creating zip`() {
        val tempDir = Files.createTempDirectory("test-zip").toFile()
        val existingFile = File(tempDir, "exists.md")
        existingFile.writeText("I exist")
        val nonExistentFile = File(tempDir, "missing.md")

        val outputPath = File(tempDir, "output.zip").absolutePath
        val zipPath = ZipHelper.createZipFromFiles(listOf(existingFile.absolutePath, nonExistentFile.absolutePath), outputPath)
        val zipFile = File(zipPath)

        assertTrue(zipFile.exists())
        ZipFile(zipFile).use { zip ->
            val entries = zip.entries().toList()
            assertEquals(1, entries.size)
            assertEquals("exists.md", entries[0].name)
        }

        existingFile.delete()
        zipFile.delete()
        tempDir.delete()
    }

    @Test
    fun `should create empty zip when no valid files provided`() {
        val tempDir = Files.createTempDirectory("test-zip").toFile()
        val outputPath = File(tempDir, "output.zip").absolutePath
        val zipPath = ZipHelper.createZipFromFiles(emptyList(), outputPath)
        val zipFile = File(zipPath)

        assertTrue(zipFile.exists())
        ZipFile(zipFile).use { zip ->
            val entries = zip.entries().toList()
            assertEquals(0, entries.size)
        }

        zipFile.delete()
        tempDir.delete()
    }

    @Test
    fun `should create parent directories when they do not exist`() {
        val tempDir = Files.createTempDirectory("test-zip").toFile()
        val file = File(tempDir, "note.md")
        file.writeText("Content")

        val outputPath = File(tempDir, "subdir/output.zip").absolutePath
        val zipPath = ZipHelper.createZipFromFiles(listOf(file.absolutePath), outputPath)
        val zipFile = File(zipPath)

        assertTrue(zipFile.exists())
        assertTrue(zipFile.parentFile.exists())

        file.delete()
        zipFile.delete()
        zipFile.parentFile.delete()
        tempDir.delete()
    }

    @Test
    fun `should preserve file content in zip entries`() {
        val tempDir = Files.createTempDirectory("test-zip").toFile()
        val file = File(tempDir, "note.md")
        val content = "Hello, this is a note with special chars: áéíóú"
        file.writeText(content)

        val outputPath = File(tempDir, "output.zip").absolutePath
        val zipPath = ZipHelper.createZipFromFiles(listOf(file.absolutePath), outputPath)
        val zipFile = File(zipPath)

        ZipFile(zipFile).use { zip ->
            val entry = zip.entries().nextElement()
            val extractedContent = zip.getInputStream(entry).bufferedReader().readText()
            assertEquals(content, extractedContent)
        }

        file.delete()
        zipFile.delete()
        tempDir.delete()
    }
}
