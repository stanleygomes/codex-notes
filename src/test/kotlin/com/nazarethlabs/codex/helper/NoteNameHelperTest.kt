package com.nazarethlabs.codex.helper

import com.nazarethlabs.codex.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NoteNameHelperTest {
    @Test
    fun `should return untitled 1 when no notes provided`() {
        val notes = emptyList<Note>()
        val result = NoteNameHelper.generateUntitledName(notes)
        assertEquals("Untitled 1", result)
    }

    @Test
    fun `should return untitled 1 when no untitled notes exist`() {
        val notes =
            listOf(
                Note(title = "Note 1"),
                Note(title = "Another Note"),
            )
        val result = NoteNameHelper.generateUntitledName(notes)
        assertEquals("Untitled 1", result)
    }

    @Test
    fun `should return next untitled number when untitled notes exist`() {
        val notes =
            listOf(
                Note(title = "Untitled 1"),
                Note(title = "Untitled 2"),
                Note(title = "Note 3"),
            )
        val result = NoteNameHelper.generateUntitledName(notes)
        assertEquals("Untitled 3", result)
    }

    @Test
    fun `should ignore invalid untitled titles and return correct next number`() {
        val notes =
            listOf(
                Note(title = "Untitled 1"),
                Note(title = "Untitled abc"),
                Note(title = "Untitled 2"),
            )
        val result = NoteNameHelper.generateUntitledName(notes)
        assertEquals("Untitled 3", result)
    }

    @Test
    fun `should be case sensitive and ignore lowercase untitled`() {
        val notes =
            listOf(
                Note(title = "Untitled 1"),
                Note(title = "untitled 2"),
            )
        val result = NoteNameHelper.generateUntitledName(notes)
        assertEquals("Untitled 2", result)
    }

    @Test
    fun `should return base title 2 when no duplicates exist`() {
        val notes = emptyList<Note>()
        val result = NoteNameHelper.generateDuplicateName("My Note", notes)
        assertEquals("My Note 2", result)
    }

    @Test
    fun `should return base title 2 when only original exists`() {
        val notes =
            listOf(
                Note(title = "My Note"),
            )
        val result = NoteNameHelper.generateDuplicateName("My Note", notes)
        assertEquals("My Note 2", result)
    }

    @Test
    fun `should return next number when duplicates already exist`() {
        val notes =
            listOf(
                Note(title = "My Note"),
                Note(title = "My Note 2"),
                Note(title = "My Note 3"),
            )
        val result = NoteNameHelper.generateDuplicateName("My Note", notes)
        assertEquals("My Note 4", result)
    }

    @Test
    fun `should return max plus one when sequential gaps exist in duplicates`() {
        val notes =
            listOf(
                Note(title = "My Note"),
                Note(title = "My Note 2"),
                Note(title = "My Note 5"),
            )
        val result = NoteNameHelper.generateDuplicateName("My Note", notes)
        assertEquals("My Note 6", result)
    }
}
