package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files
import java.util.zip.ZipFile

@RunWith(MockitoJUnitRunner::class)
class ExportNotesServiceTest {
    @InjectMocks
    private lateinit var exportNotesService: ExportNotesService

    @Test
    fun `should export notes to zip when valid notes provided`() {
        val tempDir = Files.createTempDirectory("test-export").toFile()
        val noteFile1 = File(tempDir, "note1.md")
        val noteFile2 = File(tempDir, "note2.md")
        noteFile1.writeText("Note 1 content")
        noteFile2.writeText("Note 2 content")

        val notes =
            listOf(
                Note(id = "1", title = "Note 1", filePath = noteFile1.absolutePath),
                Note(id = "2", title = "Note 2", filePath = noteFile2.absolutePath),
            )

        val outputPath = File(tempDir, "export.zip").absolutePath
        val result = exportNotesService.export(notes, outputPath)

        assertTrue(result.exists())
        ZipFile(result).use { zip ->
            val entries = zip.entries().toList()
            assertEquals(2, entries.size)
        }

        noteFile1.delete()
        noteFile2.delete()
        result.delete()
        tempDir.delete()
    }

    @Test
    fun `should skip missing note files when exporting`() {
        val tempDir = Files.createTempDirectory("test-export").toFile()
        val noteFile = File(tempDir, "exists.md")
        noteFile.writeText("Existing note")

        val notes =
            listOf(
                Note(id = "1", title = "Existing", filePath = noteFile.absolutePath),
                Note(id = "2", title = "Missing", filePath = File(tempDir, "missing.md").absolutePath),
            )

        val outputPath = File(tempDir, "export.zip").absolutePath
        val result = exportNotesService.export(notes, outputPath)

        assertTrue(result.exists())
        ZipFile(result).use { zip ->
            val entries = zip.entries().toList()
            assertEquals(1, entries.size)
            assertEquals("exists.md", entries[0].name)
        }

        noteFile.delete()
        result.delete()
        tempDir.delete()
    }

    @Test
    fun `should create empty zip when no notes provided`() {
        val tempDir = Files.createTempDirectory("test-export").toFile()
        val outputPath = File(tempDir, "export.zip").absolutePath

        val result = exportNotesService.export(emptyList(), outputPath)

        assertTrue(result.exists())
        ZipFile(result).use { zip ->
            val entries = zip.entries().toList()
            assertEquals(0, entries.size)
        }

        result.delete()
        tempDir.delete()
    }
}
