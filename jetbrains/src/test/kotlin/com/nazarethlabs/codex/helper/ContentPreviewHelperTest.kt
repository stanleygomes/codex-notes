package com.nazarethlabs.codex.helper

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ContentPreviewHelperTest {
    @Test
    fun `should return empty string when content is null`() {
        val result = ContentPreviewHelper.generatePreview(null)
        assertEquals("", result)
    }

    @Test
    fun `should return empty string when content is blank`() {
        val result = ContentPreviewHelper.generatePreview("   ")
        assertEquals("", result)
    }

    @Test
    fun `should return full content when shorter than max length`() {
        val content = "Short content"
        val result = ContentPreviewHelper.generatePreview(content)
        assertEquals("Short content", result)
    }

    @Test
    fun `should truncate with ellipsis when content exceeds max length`() {
        val content = "A".repeat(100)
        val result = ContentPreviewHelper.generatePreview(content, 80)
        assertEquals("A".repeat(80) + "...", result)
    }

    @Test
    fun `should replace newlines with spaces`() {
        val content = "Line one\nLine two\nLine three"
        val result = ContentPreviewHelper.generatePreview(content)
        assertEquals("Line one Line two Line three", result)
    }

    @Test
    fun `should return content at exact max length without ellipsis`() {
        val content = "A".repeat(80)
        val result = ContentPreviewHelper.generatePreview(content, 80)
        assertEquals("A".repeat(80), result)
    }

    @Test
    fun `should trim leading and trailing whitespace`() {
        val content = "   trimmed content   "
        val result = ContentPreviewHelper.generatePreview(content)
        assertEquals("trimmed content", result)
    }

    @Test
    fun `should return empty string when content is empty`() {
        val result = ContentPreviewHelper.generatePreview("")
        assertEquals("", result)
    }

    @Test
    fun `should use custom max length when provided`() {
        val content = "Hello World"
        val result = ContentPreviewHelper.generatePreview(content, 5)
        assertEquals("Hello...", result)
    }
}
