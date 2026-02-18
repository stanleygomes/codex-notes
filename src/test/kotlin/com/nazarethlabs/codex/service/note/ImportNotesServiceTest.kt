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
    fun `should import files when valid files provided`() {
        val sourceDir = Files.createTempDirectory("test-import-source").toFile()
        val targetDir = Files.createTempDirectory("test-import-target").toFile()
        val sourceFile1 = File(sourceDir, "note1.md")
        val sourceFile2 = File(sourceDir, "note2.md")
        sourceFile1.writeText("Note 1 content")
        sourceFile2.writeText("Note 2 content")

        val files = listOf(sourceFile1, sourceFile2)
        val result = importNotesService.importFiles(files, targetDir.absolutePath)

        assertEquals(2, result.size)
        assertTrue(File(targetDir, "note1.md").exists())
        assertTrue(File(targetDir, "note2.md").exists())
        assertEquals("Note 1 content", File(targetDir, "note1.md").readText())
        assertEquals("Note 2 content", File(targetDir, "note2.md").readText())

        sourceFile1.delete()
        sourceFile2.delete()
        sourceDir.delete()
        targetDir.listFiles()?.forEach { it.delete() }
        targetDir.delete()
    }

    @Test
    fun `should skip missing files when importing`() {
        val sourceDir = Files.createTempDirectory("test-import-source").toFile()
        val targetDir = Files.createTempDirectory("test-import-target").toFile()
        val existingFile = File(sourceDir, "exists.md")
        existingFile.writeText("Existing note")
        val missingFile = File(sourceDir, "missing.md")

        val files = listOf(existingFile, missingFile)
        val result = importNotesService.importFiles(files, targetDir.absolutePath)

        assertEquals(1, result.size)
        assertTrue(File(targetDir, "exists.md").exists())
        assertEquals("Existing note", File(targetDir, "exists.md").readText())

        existingFile.delete()
        sourceDir.delete()
        targetDir.listFiles()?.forEach { it.delete() }
        targetDir.delete()
    }

    @Test
    fun `should return empty list when no files provided`() {
        val targetDir = Files.createTempDirectory("test-import-target").toFile()

        val result = importNotesService.importFiles(emptyList(), targetDir.absolutePath)

        assertEquals(0, result.size)

        targetDir.delete()
    }

    @Test
    fun `should resolve unique file name when duplicate exists`() {
        val sourceDir = Files.createTempDirectory("test-import-source").toFile()
        val targetDir = Files.createTempDirectory("test-import-target").toFile()
        File(targetDir, "note.md").writeText("Existing content")

        val sourceFile = File(sourceDir, "note.md")
        sourceFile.writeText("New content")

        val files = listOf(sourceFile)
        val result = importNotesService.importFiles(files, targetDir.absolutePath)

        assertEquals(1, result.size)
        assertTrue(File(targetDir, "note.md").exists())
        assertTrue(File(targetDir, "note (1).md").exists())
        assertEquals("Existing content", File(targetDir, "note.md").readText())
        assertEquals("New content", File(targetDir, "note (1).md").readText())

        sourceFile.delete()
        sourceDir.delete()
        targetDir.listFiles()?.forEach { it.delete() }
        targetDir.delete()
    }

    @Test
    fun `should create target directory when it does not exist`() {
        val sourceDir = Files.createTempDirectory("test-import-source").toFile()
        val targetDir = File(Files.createTempDirectory("test-import-parent").toFile(), "new-dir")
        val sourceFile = File(sourceDir, "note.md")
        sourceFile.writeText("Note content")

        val files = listOf(sourceFile)
        val result = importNotesService.importFiles(files, targetDir.absolutePath)

        assertEquals(1, result.size)
        assertTrue(targetDir.exists())
        assertTrue(File(targetDir, "note.md").exists())
        assertEquals("Note content", File(targetDir, "note.md").readText())

        sourceFile.delete()
        sourceDir.delete()
        targetDir.listFiles()?.forEach { it.delete() }
        targetDir.delete()
        targetDir.parentFile.delete()
    }
}
