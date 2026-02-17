package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files

@RunWith(MockitoJUnitRunner::class)
class NoteContentIndexServiceTest {
    private lateinit var service: NoteContentIndexService
    private lateinit var tempDir: File

    @Before
    fun setUp() {
        service = NoteContentIndexService()
        tempDir = Files.createTempDirectory("test-content-index").toFile()
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `should return file content when file exists`() {
        val noteFile = File(tempDir, "note.md")
        noteFile.writeText("Test content")

        val note = Note(id = "1", title = "Note 1", filePath = noteFile.absolutePath)

        val content = service.getContent(note)

        assertEquals("Test content", content)
    }

    @Test
    fun `should return null when file does not exist`() {
        val note = Note(id = "1", title = "Note 1", filePath = "${tempDir.absolutePath}/nonexistent.md")

        val content = service.getContent(note)

        assertNull(content)
    }

    @Test
    fun `should return cached content when file not modified`() {
        val noteFile = File(tempDir, "note.md")
        noteFile.writeText("Original content")

        val note = Note(id = "1", title = "Note 1", filePath = noteFile.absolutePath)

        val content1 = service.getContent(note)
        val content2 = service.getContent(note)

        assertEquals("Original content", content1)
        assertEquals("Original content", content2)
    }

    @Test
    fun `should return updated content when file modified`() {
        val noteFile = File(tempDir, "note.md")
        noteFile.writeText("Original content")
        val originalTimestamp = noteFile.lastModified()

        val note = Note(id = "1", title = "Note 1", filePath = noteFile.absolutePath)

        val content1 = service.getContent(note)
        assertEquals("Original content", content1)

        Thread.sleep(50)
        noteFile.writeText("Updated content")
        noteFile.setLastModified(originalTimestamp + 1000)

        val content2 = service.getContent(note)
        assertEquals("Updated content", content2)
    }

    @Test
    fun `should return content map for multiple notes`() {
        val noteFile1 = File(tempDir, "note1.md")
        val noteFile2 = File(tempDir, "note2.md")
        noteFile1.writeText("Content 1")
        noteFile2.writeText("Content 2")

        val notes =
            listOf(
                Note(id = "1", title = "Note 1", filePath = noteFile1.absolutePath),
                Note(id = "2", title = "Note 2", filePath = noteFile2.absolutePath),
            )

        val contentMap = service.getContentForSearch(notes)

        assertEquals(2, contentMap.size)
        assertEquals("Content 1", contentMap["1"])
        assertEquals("Content 2", contentMap["2"])
    }

    @Test
    fun `should return empty string for missing files in content map`() {
        val noteFile = File(tempDir, "note.md")
        noteFile.writeText("Existing content")

        val notes =
            listOf(
                Note(id = "1", title = "Existing", filePath = noteFile.absolutePath),
                Note(id = "2", title = "Missing", filePath = "${tempDir.absolutePath}/missing.md"),
            )

        val contentMap = service.getContentForSearch(notes)

        assertEquals("Existing content", contentMap["1"])
        assertEquals("", contentMap["2"])
    }

    @Test
    fun `should remove cache entry when invalidated`() {
        val noteFile = File(tempDir, "note.md")
        noteFile.writeText("Original content")

        val note = Note(id = "1", title = "Note 1", filePath = noteFile.absolutePath)

        service.getContent(note)
        service.invalidateCache("1")

        noteFile.writeText("New content")

        val content = service.getContent(note)
        assertEquals("New content", content)
    }

    @Test
    fun `should clear all cache when invalidate all called`() {
        val noteFile1 = File(tempDir, "note1.md")
        val noteFile2 = File(tempDir, "note2.md")
        noteFile1.writeText("Content 1")
        noteFile2.writeText("Content 2")

        val notes =
            listOf(
                Note(id = "1", title = "Note 1", filePath = noteFile1.absolutePath),
                Note(id = "2", title = "Note 2", filePath = noteFile2.absolutePath),
            )

        service.getContentForSearch(notes)
        service.invalidateAllCache()

        noteFile1.writeText("New Content 1")
        noteFile2.writeText("New Content 2")

        val contentMap = service.getContentForSearch(notes)

        assertEquals("New Content 1", contentMap["1"])
        assertEquals("New Content 2", contentMap["2"])
    }

    @Test
    fun `should return null when file is directory`() {
        val subDir = File(tempDir, "subdir")
        subDir.mkdirs()

        val note = Note(id = "1", title = "Note 1", filePath = subDir.absolutePath)

        val content = service.getContent(note)

        assertNull(content)
    }

    @Test
    fun `should handle empty file`() {
        val noteFile = File(tempDir, "empty.md")
        noteFile.writeText("")

        val note = Note(id = "1", title = "Empty Note", filePath = noteFile.absolutePath)

        val content = service.getContent(note)

        assertEquals("", content)
    }

    @Test
    fun `should handle special characters in content`() {
        val noteFile = File(tempDir, "special.md")
        noteFile.writeText("Special chars: ñ é ü ç 日本語")

        val note = Note(id = "1", title = "Special Note", filePath = noteFile.absolutePath)

        val content = service.getContent(note)

        assertTrue(content!!.contains("ñ"))
        assertTrue(content.contains("日本語"))
    }
}
