package com.nazarethlabs.codex.helper

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files

@RunWith(MockitoJUnitRunner::class)
class OpenFolderHelperTest {
    @Test
    fun `should create directory when folder does not exist`() {
        val tempDir = Files.createTempDirectory("test").toFile()
        val nonExistentDir = File(tempDir, "nonexistent")

        try {
            assertFalse(nonExistentDir.exists())

            OpenFolderHelper.openFolder(nonExistentDir.absolutePath)

            assertTrue(nonExistentDir.exists())
        } finally {
            nonExistentDir.delete()
            tempDir.delete()
        }
    }

    @Test
    fun `should return true when opening existing folder`() {
        val tempDir = Files.createTempDirectory("test").toFile()

        try {
            val result = OpenFolderHelper.openFolder(tempDir.absolutePath)

            assertTrue(result)
        } finally {
            tempDir.delete()
        }
    }
}
