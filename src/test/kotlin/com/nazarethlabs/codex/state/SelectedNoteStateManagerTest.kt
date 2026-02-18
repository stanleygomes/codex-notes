package com.nazarethlabs.codex.state

import com.nazarethlabs.codex.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SelectedNoteStateManagerTest {
    private lateinit var selectedNoteStateManager: SelectedNoteStateManager

    @Before
    fun setUp() {
        selectedNoteStateManager = SelectedNoteStateManager()
    }

    private class TestSelectedNoteListener : SelectedNoteListener {
        var lastNotes: List<Note>? = null
        var callCount = 0

        override fun onSelectedNotesChanged(notes: List<Note>) {
            lastNotes = notes
            callCount++
        }
    }

    @Test
    fun `should start with empty selected notes`() {
        val result = selectedNoteStateManager.getSelectedNotes()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return null when no note is selected`() {
        val result = selectedNoteStateManager.getSelectedNote()

        assertNull(result)
    }

    @Test
    fun `should return false for multiple selection when empty`() {
        val result = selectedNoteStateManager.hasMultipleSelection()

        assertFalse(result)
    }

    @Test
    fun `should set and get selected notes`() {
        val notes =
            listOf(
                Note(id = "1", title = "Note 1"),
                Note(id = "2", title = "Note 2"),
            )

        selectedNoteStateManager.setSelectedNotes(notes)

        assertEquals(2, selectedNoteStateManager.getSelectedNotes().size)
        assertEquals("Note 1", selectedNoteStateManager.getSelectedNotes()[0].title)
        assertEquals("Note 2", selectedNoteStateManager.getSelectedNotes()[1].title)
    }

    @Test
    fun `should return first note when single note selected`() {
        val notes = listOf(Note(id = "1", title = "Note 1"))

        selectedNoteStateManager.setSelectedNotes(notes)

        assertEquals("Note 1", selectedNoteStateManager.getSelectedNote()?.title)
    }

    @Test
    fun `should return first note when multiple notes selected`() {
        val notes =
            listOf(
                Note(id = "1", title = "Note 1"),
                Note(id = "2", title = "Note 2"),
            )

        selectedNoteStateManager.setSelectedNotes(notes)

        assertEquals("Note 1", selectedNoteStateManager.getSelectedNote()?.title)
    }

    @Test
    fun `should return false for multiple selection when single note selected`() {
        val notes = listOf(Note(id = "1", title = "Note 1"))

        selectedNoteStateManager.setSelectedNotes(notes)

        assertFalse(selectedNoteStateManager.hasMultipleSelection())
    }

    @Test
    fun `should return true for multiple selection when two notes selected`() {
        val notes =
            listOf(
                Note(id = "1", title = "Note 1"),
                Note(id = "2", title = "Note 2"),
            )

        selectedNoteStateManager.setSelectedNotes(notes)

        assertTrue(selectedNoteStateManager.hasMultipleSelection())
    }

    @Test
    fun `should notify listeners when selected notes change`() {
        val listener = TestSelectedNoteListener()
        selectedNoteStateManager.addListener(listener)
        val notes = listOf(Note(id = "1", title = "Note 1"))

        selectedNoteStateManager.setSelectedNotes(notes)

        assertEquals(1, listener.callCount)
        assertEquals(1, listener.lastNotes?.size)
        assertEquals("Note 1", listener.lastNotes?.first()?.title)
    }

    @Test
    fun `should notify all listeners when selected notes change`() {
        val listener1 = TestSelectedNoteListener()
        val listener2 = TestSelectedNoteListener()
        selectedNoteStateManager.addListener(listener1)
        selectedNoteStateManager.addListener(listener2)
        val notes = listOf(Note(id = "1", title = "Note 1"))

        selectedNoteStateManager.setSelectedNotes(notes)

        assertEquals(1, listener1.callCount)
        assertEquals(1, listener2.callCount)
    }

    @Test
    fun `should replace selected notes when set again`() {
        val notes1 = listOf(Note(id = "1", title = "Note 1"))
        val notes2 = listOf(Note(id = "2", title = "Note 2"))

        selectedNoteStateManager.setSelectedNotes(notes1)
        selectedNoteStateManager.setSelectedNotes(notes2)

        assertEquals(1, selectedNoteStateManager.getSelectedNotes().size)
        assertEquals("Note 2", selectedNoteStateManager.getSelectedNote()?.title)
    }
}
