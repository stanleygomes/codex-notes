package com.nazarethlabs.notes.helper

import org.junit.Assert.assertEquals
import org.junit.Test

class FileExtensionHelperTest {
    @Test
    fun `should return normalized extension when input is valid without dot`() {
        val result = FileExtensionHelper.normalizeExtension("md")

        assertEquals(".md", result)
    }

    @Test
    fun `should return normalized extension when input already has dot`() {
        val result = FileExtensionHelper.normalizeExtension(".txt")

        assertEquals(".txt", result)
    }

    @Test
    fun `should trim spaces from input`() {
        val result = FileExtensionHelper.normalizeExtension(" txt ")

        assertEquals(".txt", result)
    }

    @Test
    fun `should remove invalid characters from input`() {
        val result = FileExtensionHelper.normalizeExtension("t@xt!")

        assertEquals(".txt", result)
    }

    @Test
    fun `should return default extension when input is empty`() {
        val result = FileExtensionHelper.normalizeExtension("")

        assertEquals(".md", result)
    }

    @Test
    fun `should return default extension when input has only invalid characters`() {
        val result = FileExtensionHelper.normalizeExtension("@#$%")

        assertEquals(".md", result)
    }

    @Test
    fun `should handle input with mix of valid and invalid characters`() {
        val result = FileExtensionHelper.normalizeExtension("doc123!")

        assertEquals(".doc123", result)
    }
}
