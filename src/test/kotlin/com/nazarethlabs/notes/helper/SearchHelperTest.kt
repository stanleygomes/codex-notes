package com.nazarethlabs.notes.helper

import com.nazarethlabs.notes.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchHelperTest {
    @Test
    fun `should return all notes when query is blank`() {
        val notes =
            listOf(
                Note(title = "Note 1"),
                Note(title = "Note 2"),
            )
        val result = SearchHelper.search(notes, "")
        assertEquals(notes, result)
    }

    @Test
    fun `should return all notes when query is whitespace`() {
        val notes =
            listOf(
                Note(title = "Note 1"),
                Note(title = "Note 2"),
            )
        val result = SearchHelper.search(notes, "   ")
        assertEquals(notes, result)
    }

    @Test
    fun `should return empty list when no notes match query`() {
        val notes =
            listOf(
                Note(title = "Note 1"),
                Note(title = "Note 2"),
            )
        val result = SearchHelper.search(notes, "nonexistent")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return notes sorted by relevance when query matches`() {
        val notes =
            listOf(
                Note(title = "Apple Pie"),
                Note(title = "Banana"),
                Note(title = "Apple Juice"),
                Note(title = "Cherry"),
            )
        val result = SearchHelper.search(notes, "apple")
        assertEquals(2, result.size)
        assertEquals("Apple Pie", result[0].title) // both have same score, order as in original list
        assertEquals("Apple Juice", result[1].title)
    }

    @Test
    fun `should handle multiple search terms`() {
        val notes =
            listOf(
                Note(title = "Apple Pie Recipe"),
                Note(title = "Banana Smoothie"),
                Note(title = "Cherry Pie"),
            )
        val result = SearchHelper.search(notes, "pie recipe")
        assertEquals(2, result.size)
        assertEquals("Apple Pie Recipe", result[0].title)
        assertEquals("Cherry Pie", result[1].title)
    }

    @Test
    fun `should be case insensitive`() {
        val notes =
            listOf(
                Note(title = "Apple Pie"),
                Note(title = "banana"),
            )
        val result = SearchHelper.search(notes, "APPLE")
        assertEquals(1, result.size)
        assertEquals("Apple Pie", result[0].title)
    }

    @Test
    fun `should score exact word matches higher`() {
        val notes =
            listOf(
                Note(title = "Apple Pie"),
                Note(title = "Pie Recipe"),
                Note(title = "Apple"),
            )
        val result = SearchHelper.search(notes, "apple")
        assertEquals(2, result.size)
        assertEquals("Apple", result[0].title) // exact match
        assertEquals("Apple Pie", result[1].title)
    }

    @Test
    fun `should handle query with special characters in title splitting`() {
        val notes =
            listOf(
                Note(title = "Apple-Pie Recipe"),
                Note(title = "Apple.Pie"),
            )
        val result = SearchHelper.search(notes, "pie")
        assertEquals(2, result.size)
        // Both should match, order based on score
    }
}
