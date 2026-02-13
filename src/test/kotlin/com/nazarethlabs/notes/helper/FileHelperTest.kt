package com.nazarethlabs.notes.helper

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
        val fileName = "test.txt"
        val file = FileHelper.createFile(tempDir.absolutePath, fileName)
        assertTrue(file.exists())
        assertEquals("", file.readText())
        file.delete()
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
    fun `should return temp directory when called`() {
        val expected = System.getProperty("java.io.tmpdir")
        val result = FileHelper.getTempDir()
        assertEquals(expected, result)
    }
}
