package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.SortTypeEnum
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NotesSortServiceTest {
    @Test
    fun `should sort notes by title alphabetically`() {
        val notes =
            listOf(
                Note(id = "1", title = "Charlie"),
                Note(id = "2", title = "Alpha"),
                Note(id = "3", title = "Bravo"),
            )

        val sorted = sortNotes(notes, SortTypeEnum.TITLE)

        assertEquals("Alpha", sorted[0].title)
        assertEquals("Bravo", sorted[1].title)
        assertEquals("Charlie", sorted[2].title)
    }

    @Test
    fun `should sort notes by title case insensitively`() {
        val notes =
            listOf(
                Note(id = "1", title = "banana"),
                Note(id = "2", title = "Apple"),
                Note(id = "3", title = "cherry"),
            )

        val sorted = sortNotes(notes, SortTypeEnum.TITLE)

        assertEquals("Apple", sorted[0].title)
        assertEquals("banana", sorted[1].title)
        assertEquals("cherry", sorted[2].title)
    }

    @Test
    fun `should sort notes by date descending`() {
        val notes =
            listOf(
                Note(id = "1", title = "Old", updatedAt = 1000L),
                Note(id = "2", title = "Newest", updatedAt = 3000L),
                Note(id = "3", title = "Middle", updatedAt = 2000L),
            )

        val sorted = sortNotes(notes, SortTypeEnum.DATE)

        assertEquals("Newest", sorted[0].title)
        assertEquals("Middle", sorted[1].title)
        assertEquals("Old", sorted[2].title)
    }

    @Test
    fun `should sort notes by favorite first then by date`() {
        val notes =
            listOf(
                Note(id = "1", title = "Not Fav Old", isFavorite = false, updatedAt = 1000L),
                Note(id = "2", title = "Fav Old", isFavorite = true, updatedAt = 1000L),
                Note(id = "3", title = "Not Fav New", isFavorite = false, updatedAt = 3000L),
                Note(id = "4", title = "Fav New", isFavorite = true, updatedAt = 3000L),
            )

        val sorted = sortNotes(notes, SortTypeEnum.FAVORITE)

        assertEquals("Fav New", sorted[0].title)
        assertEquals("Fav Old", sorted[1].title)
        assertEquals("Not Fav New", sorted[2].title)
        assertEquals("Not Fav Old", sorted[3].title)
    }

    @Test
    fun `should return empty list when no notes`() {
        val sorted = sortNotes(emptyList(), SortTypeEnum.TITLE)

        assertEquals(0, sorted.size)
    }

    @Test
    fun `should return single note unchanged`() {
        val notes = listOf(Note(id = "1", title = "Only Note"))

        val sorted = sortNotes(notes, SortTypeEnum.TITLE)

        assertEquals(1, sorted.size)
        assertEquals("Only Note", sorted[0].title)
    }

    private fun sortNotes(
        notes: List<Note>,
        sortType: SortTypeEnum,
    ): List<Note> =
        when (sortType) {
            SortTypeEnum.TITLE -> notes.sortedBy { it.title.lowercase() }
            SortTypeEnum.DATE -> notes.sortedByDescending { it.updatedAt }
            SortTypeEnum.FAVORITE ->
                notes.sortedWith(
                    compareByDescending<Note> { it.isFavorite }.thenByDescending { it.updatedAt },
                )
        }
}
