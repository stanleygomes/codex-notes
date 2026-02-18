package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.DateFilterEnum
import com.nazarethlabs.codex.enum.NoteColorEnum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

@RunWith(MockitoJUnitRunner::class)
class NoteFilterServiceTest {
    private lateinit var noteFilterService: NoteFilterService

    @Before
    fun setUp() {
        noteFilterService = NoteFilterService()
    }

    @Test
    fun `should return all notes when no filters are active`() {
        val notes =
            listOf(
                Note(id = "1", title = "Note 1"),
                Note(id = "2", title = "Note 2"),
            )

        val result = noteFilterService.applyFilters(notes, null, false, emptySet())

        assertEquals(2, result.size)
        assertEquals("Note 1", result[0].title)
        assertEquals("Note 2", result[1].title)
    }

    @Test
    fun `should filter notes by favorite when favorite filter is active`() {
        val notes =
            listOf(
                Note(id = "1", title = "Note 1", isFavorite = true),
                Note(id = "2", title = "Note 2", isFavorite = false),
                Note(id = "3", title = "Note 3", isFavorite = true),
            )

        val result = noteFilterService.applyFilters(notes, null, true, emptySet())

        assertEquals(2, result.size)
        assertEquals("Note 1", result[0].title)
        assertEquals("Note 3", result[1].title)
    }

    @Test
    fun `should filter notes by color when color filters are active`() {
        val notes =
            listOf(
                Note(id = "1", title = "Note 1", color = NoteColorEnum.BLUE),
                Note(id = "2", title = "Note 2", color = NoteColorEnum.GREEN),
                Note(id = "3", title = "Note 3", color = NoteColorEnum.PINK),
            )

        val result = noteFilterService.applyFilters(notes, null, false, setOf(NoteColorEnum.BLUE, NoteColorEnum.PINK))

        assertEquals(2, result.size)
        assertEquals("Note 1", result[0].title)
        assertEquals("Note 3", result[1].title)
    }

    @Test
    fun `should filter notes by date when today filter is active`() {
        val now = System.currentTimeMillis()
        val yesterday = now - 2 * 24 * 60 * 60 * 1000L

        val notes =
            listOf(
                Note(id = "1", title = "Today Note", updatedAt = now),
                Note(id = "2", title = "Old Note", updatedAt = yesterday),
            )

        val result = noteFilterService.applyFilters(notes, DateFilterEnum.TODAY, false, emptySet())

        assertEquals(1, result.size)
        assertEquals("Today Note", result[0].title)
    }

    @Test
    fun `should filter notes by date when this week filter is active`() {
        val now = System.currentTimeMillis()
        val twoWeeksAgo = now - 14 * 24 * 60 * 60 * 1000L

        val notes =
            listOf(
                Note(id = "1", title = "Recent Note", updatedAt = now),
                Note(id = "2", title = "Old Note", updatedAt = twoWeeksAgo),
            )

        val result = noteFilterService.applyFilters(notes, DateFilterEnum.THIS_WEEK, false, emptySet())

        assertEquals(1, result.size)
        assertEquals("Recent Note", result[0].title)
    }

    @Test
    fun `should filter notes by date when this month filter is active`() {
        val now = System.currentTimeMillis()
        val twoMonthsAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -2) }.timeInMillis

        val notes =
            listOf(
                Note(id = "1", title = "Recent Note", updatedAt = now),
                Note(id = "2", title = "Old Note", updatedAt = twoMonthsAgo),
            )

        val result = noteFilterService.applyFilters(notes, DateFilterEnum.THIS_MONTH, false, emptySet())

        assertEquals(1, result.size)
        assertEquals("Recent Note", result[0].title)
    }

    @Test
    fun `should combine date and favorite filters`() {
        val now = System.currentTimeMillis()
        val yesterday = now - 2 * 24 * 60 * 60 * 1000L

        val notes =
            listOf(
                Note(id = "1", title = "Today Fav", updatedAt = now, isFavorite = true),
                Note(id = "2", title = "Today NoFav", updatedAt = now, isFavorite = false),
                Note(id = "3", title = "Old Fav", updatedAt = yesterday, isFavorite = true),
            )

        val result = noteFilterService.applyFilters(notes, DateFilterEnum.TODAY, true, emptySet())

        assertEquals(1, result.size)
        assertEquals("Today Fav", result[0].title)
    }

    @Test
    fun `should combine all filters together`() {
        val now = System.currentTimeMillis()
        val yesterday = now - 2 * 24 * 60 * 60 * 1000L

        val notes =
            listOf(
                Note(id = "1", title = "Match", updatedAt = now, isFavorite = true, color = NoteColorEnum.BLUE),
                Note(id = "2", title = "Wrong Color", updatedAt = now, isFavorite = true, color = NoteColorEnum.GREEN),
                Note(id = "3", title = "Not Fav", updatedAt = now, isFavorite = false, color = NoteColorEnum.BLUE),
                Note(id = "4", title = "Old", updatedAt = yesterday, isFavorite = true, color = NoteColorEnum.BLUE),
            )

        val result = noteFilterService.applyFilters(notes, DateFilterEnum.TODAY, true, setOf(NoteColorEnum.BLUE))

        assertEquals(1, result.size)
        assertEquals("Match", result[0].title)
    }

    @Test
    fun `should return empty list when no notes match combined filters`() {
        val notes =
            listOf(
                Note(id = "1", title = "Note 1", isFavorite = false, color = NoteColorEnum.BLUE),
            )

        val result = noteFilterService.applyFilters(notes, null, true, setOf(NoteColorEnum.GREEN))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should calculate start of today correctly`() {
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.FEBRUARY, 17, 15, 30, 45)
        calendar.set(Calendar.MILLISECOND, 500)

        val startOfPeriod = noteFilterService.getStartOfPeriodFromCalendar(DateFilterEnum.TODAY, calendar)

        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = startOfPeriod
        assertEquals(0, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, resultCalendar.get(Calendar.MINUTE))
        assertEquals(0, resultCalendar.get(Calendar.SECOND))
        assertEquals(0, resultCalendar.get(Calendar.MILLISECOND))
        assertEquals(17, resultCalendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `should calculate start of this month correctly`() {
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.FEBRUARY, 17, 15, 30, 45)
        calendar.set(Calendar.MILLISECOND, 500)

        val startOfPeriod = noteFilterService.getStartOfPeriodFromCalendar(DateFilterEnum.THIS_MONTH, calendar)

        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = startOfPeriod
        assertEquals(0, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, resultCalendar.get(Calendar.MINUTE))
        assertEquals(0, resultCalendar.get(Calendar.SECOND))
        assertEquals(0, resultCalendar.get(Calendar.MILLISECOND))
        assertEquals(1, resultCalendar.get(Calendar.DAY_OF_MONTH))
    }
}
