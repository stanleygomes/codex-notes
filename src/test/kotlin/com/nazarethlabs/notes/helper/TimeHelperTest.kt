package com.nazarethlabs.notes.helper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TimeHelperTest {
    @Test
    fun `should return now when less than 60 seconds ago`() {
        val now = 1000000L
        val updatedAt = now - 30000L // 30 seconds ago
        val result = TimeHelper.formatTimeAgo(updatedAt, now)
        assertEquals("now", result)
    }

    @Test
    fun `should return minutes ago when less than 60 minutes ago`() {
        val now = 1000000L
        val updatedAt = now - 120000L // 2 minutes ago
        val result = TimeHelper.formatTimeAgo(updatedAt, now)
        assertEquals("2min ago", result)
    }

    @Test
    fun `should return hours ago when less than 24 hours ago`() {
        val now = 1000000L
        val updatedAt = now - 7200000L // 2 hours ago
        val result = TimeHelper.formatTimeAgo(updatedAt, now)
        assertEquals("2h ago", result)
    }

    @Test
    fun `should return days ago when less than 30 days ago`() {
        val now = 1000000L
        val updatedAt = now - 172800000L // 2 days ago
        val result = TimeHelper.formatTimeAgo(updatedAt, now)
        assertEquals("2d ago", result)
    }

    @Test
    fun `should return a long time ago when more than 30 days ago`() {
        val now = 1000000L
        val updatedAt = now - 2592000000L // 30 days ago
        val result = TimeHelper.formatTimeAgo(updatedAt, now)
        assertEquals("a long time ago", result)
    }

    @Test
    fun `should handle boundary at 59 seconds`() {
        val now = 1000000L
        val updatedAt = now - 59000L // 59 seconds ago
        val result = TimeHelper.formatTimeAgo(updatedAt, now)
        assertEquals("now", result)
    }

    @Test
    fun `should handle boundary at 60 seconds to minutes`() {
        val now = 1000000L
        val updatedAt = now - 60000L // 60 seconds ago
        val result = TimeHelper.formatTimeAgo(updatedAt, now)
        assertEquals("1min ago", result)
    }

    @Test
    fun `should format time ago using current time when not provided`() {
        val updatedAt = System.currentTimeMillis() - 1000L // 1 second ago
        val result = TimeHelper.formatTimeAgo(updatedAt)
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }
}
