package com.nazarethlabs.codex.helper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files

@RunWith(MockitoJUnitRunner::class)
class FileHelperTest {
    @Test
    fun `should create file when directory and name provided`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val notesDir = File(tempDir, "notes")
        val fileName = "test.txt"
        val file = FileHelper.createFile(notesDir.absolutePath, fileName)
        assertTrue(file.exists())
        assertEquals("", file.readText())
        file.delete()
        notesDir.delete()
        tempDir.delete()
    }

    @Test
    fun `should delete file when file exists`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val file = File(tempDir, "test.txt")
        file.writeText("content")
        FileHelper.deleteFile(file.absolutePath)
        assertFalse(file.exists())
        tempDir.delete()
    }

    @Test
    fun `should not delete when file does not exist`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val filePath = File(tempDir, "nonexistent.txt").absolutePath
        FileHelper.deleteFile(filePath)
        // Nothing to assert, just that no exception
        tempDir.delete()
    }

    @Test
    fun `should rename file when old file exists`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val oldFile = File(tempDir, "old.txt")
        oldFile.writeText("content")
        val newName = "new.txt"
        val result = FileHelper.renameFile(oldFile.absolutePath, newName)
        assertTrue(result)
        val newFile = File(tempDir, newName)
        assertTrue(newFile.exists())
        assertEquals("content", newFile.readText())
        newFile.delete()
        tempDir.delete()
    }

    @Test
    fun `should return false when old file does not exist for renaming`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val oldPath = File(tempDir, "nonexistent.txt").absolutePath
        val newName = "new.txt"
        val result = FileHelper.renameFile(oldPath, newName)
        assertFalse(result)
        tempDir.delete()
    }

    @Test
    fun `should return true when file exists`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val file = File(tempDir, "test.txt")
        file.writeText("")
        val result = FileHelper.fileExists(tempDir.absolutePath, "test.txt")
        assertTrue(result)
        file.delete()
        tempDir.delete()
    }

    @Test
    fun `should return false when file does not exist`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val result = FileHelper.fileExists(tempDir.absolutePath, "nonexistent.txt")
        assertFalse(result)
        tempDir.delete()
    }

    @Test
    fun `should return new path when old path and new name provided`() {
        val oldPath = "/some/dir/old.txt"
        val newName = "new.txt"
        val expected = "/some/dir/new.txt"
        val result = FileHelper.getNewFilePath(oldPath, newName)
        assertEquals(expected, result)
    }

    @Test
    fun `should return parent path when file path provided`() {
        val filePath = "/some/dir/file.txt"
        val expected = "/some/dir"
        val result = FileHelper.getParentPath(filePath)
        assertEquals(expected, result)
    }

    @Test
    fun `should create file with content when directory name and content provided`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val notesDir = File(tempDir, "notes")
        val fileName = "test.txt"
        val content = "Hello, World!"
        val file = FileHelper.createFileWithContent(notesDir.absolutePath, fileName, content)
        assertTrue(file.exists())
        assertEquals(content, file.readText())
        file.delete()
        notesDir.delete()
        tempDir.delete()
    }

    @Test
    fun `should return default notes directory when called`() {
        val expected = File(System.getProperty("user.home"), ".codex-notes").absolutePath
        val result = FileHelper.getDefaultNotesDir()
        assertEquals(expected, result)
    }

    @Test
    fun `should return unchanged title when filename is already valid`() {
        val result = FileHelper.sanitizeFileName("My Note")
        assertEquals("My Note", result)
    }

    @Test
    fun `should remove emoji from filename`() {
        val result = FileHelper.sanitizeFileName("My Note 🎉")
        assertEquals("My Note", result)
    }

    @Test
    fun `should remove slashes from filename`() {
        val result = FileHelper.sanitizeFileName("path/to/note")
        assertEquals("pathtonote", result)
    }

    @Test
    fun `should preserve spaces when removing slashes from filename`() {
        val result = FileHelper.sanitizeFileName("path / to / note")
        assertEquals("path to note", result)
    }

    @Test
    fun `should remove backslashes from filename`() {
        val result = FileHelper.sanitizeFileName("path\\note")
        assertEquals("pathnote", result)
    }

    @Test
    fun `should remove colon from filename`() {
        val result = FileHelper.sanitizeFileName("note: title")
        assertEquals("note title", result)
    }

    @Test
    fun `should remove invalid special characters from filename`() {
        val result = FileHelper.sanitizeFileName("note*?\"<>|")
        assertEquals("note", result)
    }

    @Test
    fun `should normalize multiple spaces to single space`() {
        val result = FileHelper.sanitizeFileName("my   note")
        assertEquals("my note", result)
    }

    @Test
    fun `should trim leading and trailing spaces`() {
        val result = FileHelper.sanitizeFileName("  my note  ")
        assertEquals("my note", result)
    }

    @Test
    fun `should return untitled when title results in empty string after sanitization`() {
        val result = FileHelper.sanitizeFileName("🎉🚀")
        assertEquals("untitled", result)
    }

    @Test
    fun `should return untitled when title is empty`() {
        val result = FileHelper.sanitizeFileName("")
        assertEquals("untitled", result)
    }
}
