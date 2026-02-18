package com.nazarethlabs.codex.helper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class DateHelperTest {
    @Test
    fun `should format date with correct pattern`() {
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.FEBRUARY, 17, 14, 30, 0)
        val date = calendar.time

        val result = DateHelper.formatDate(date)

        assertEquals("17/02/2026 14:30", result)
    }

    @Test
    fun `should format date at midnight`() {
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.JANUARY, 1, 0, 0, 0)
        val date = calendar.time

        val result = DateHelper.formatDate(date)

        assertEquals("01/01/2026 00:00", result)
    }

    @Test
    fun `should format date at end of day`() {
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.DECEMBER, 31, 23, 59, 0)
        val date = calendar.time

        val result = DateHelper.formatDate(date)

        assertEquals("31/12/2026 23:59", result)
    }

    @Test
    fun `should return non empty string for any date`() {
        val date = Date()

        val result = DateHelper.formatDate(date)

        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `should use dd MM yyyy HH mm format`() {
        val expectedFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date = Date()

        val result = DateHelper.formatDate(date)
        val expected = expectedFormat.format(date)

        assertEquals(expected, result)
    }
}
