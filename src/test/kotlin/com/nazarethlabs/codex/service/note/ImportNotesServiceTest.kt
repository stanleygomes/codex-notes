package com.nazarethlabs.codex.service.note

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files

@RunWith(MockitoJUnitRunner::class)
class ImportNotesServiceTest {
    @InjectMocks
    private lateinit var importNotesService: ImportNotesService

    @Test
    fun `should import files when valid source folder provided`() {
        val sourceDir = Files.createTempDirectory("test-import-source").toFile()
        val destDir = Files.createTempDirectory("test-import-dest").toFile()

        val file1 = File(sourceDir, "note1.md")
        val file2 = File(sourceDir, "note2.md")
        file1.writeText("Note 1 content")
        file2.writeText("Note 2 content")

        val result = importNotesService.import(sourceDir.absolutePath, destDir.absolutePath)

        assertEquals(2, result.size)
        assertTrue(File(destDir, "note1.md").exists())
        assertTrue(File(destDir, "note2.md").exists())
        assertEquals("Note 1 content", File(destDir, "note1.md").readText())
        assertEquals("Note 2 content", File(destDir, "note2.md").readText())

        file1.delete()
        file2.delete()
        File(destDir, "note1.md").delete()
        File(destDir, "note2.md").delete()
        sourceDir.delete()
        destDir.delete()
    }

    @Test
    fun `should return empty list when source folder is empty`() {
        val sourceDir = Files.createTempDirectory("test-import-source").toFile()
        val destDir = Files.createTempDirectory("test-import-dest").toFile()

        val result = importNotesService.import(sourceDir.absolutePath, destDir.absolutePath)

        assertEquals(0, result.size)

        sourceDir.delete()
        destDir.delete()
    }

    @Test
    fun `should skip files that already exist in destination`() {
        val sourceDir = Files.createTempDirectory("test-import-source").toFile()
        val destDir = Files.createTempDirectory("test-import-dest").toFile()

        val sourceFile = File(sourceDir, "existing.md")
        sourceFile.writeText("New content")

        val existingFile = File(destDir, "existing.md")
        existingFile.writeText("Old content")

        val result = importNotesService.import(sourceDir.absolutePath, destDir.absolutePath)

        assertEquals(0, result.size)
        assertEquals("Old content", existingFile.readText())

        sourceFile.delete()
        existingFile.delete()
        sourceDir.delete()
        destDir.delete()
    }

    @Test
    fun `should skip subdirectories and only import files`() {
        val sourceDir = Files.createTempDirectory("test-import-source").toFile()
        val destDir = Files.createTempDirectory("test-import-dest").toFile()

        val file = File(sourceDir, "note.md")
        file.writeText("Note content")
        val subDir = File(sourceDir, "subfolder")
        subDir.mkdir()

        val result = importNotesService.import(sourceDir.absolutePath, destDir.absolutePath)

        assertEquals(1, result.size)
        assertEquals("note.md", result[0].name)

        file.delete()
        subDir.delete()
        File(destDir, "note.md").delete()
        sourceDir.delete()
        destDir.delete()
    }

    @Test
    fun `should return empty list when source folder does not exist`() {
        val destDir = Files.createTempDirectory("test-import-dest").toFile()
        val nonExistentPath = "/tmp/non-existent-folder-${System.currentTimeMillis()}"

        val result = importNotesService.import(nonExistentPath, destDir.absolutePath)

        assertEquals(0, result.size)

        destDir.delete()
    }
}
