package com.nazarethlabs.codex.service.settings

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files

@RunWith(MockitoJUnitRunner::class)
class OpenNotesFolderServiceTest {
    @Test
    fun `should create directory when folder does not exist`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val nonExistentDir = File(tempDir, "nonexistent")

        assertFalse(nonExistentDir.exists())

        val service = OpenNotesFolderService()
        service.openFolder(nonExistentDir.absolutePath)

        assertTrue(nonExistentDir.exists())

        nonExistentDir.delete()
        tempDir.delete()
    }

    @Test
    fun `should return true when opening existing folder`() {
        val tempDir = Files.createTempDirectory("test").toFile()

        val service = OpenNotesFolderService()
        val result = service.openFolder(tempDir.absolutePath)

        assertTrue(result)

        tempDir.delete()
    }
}
