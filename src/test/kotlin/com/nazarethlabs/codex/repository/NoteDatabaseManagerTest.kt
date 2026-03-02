package com.nazarethlabs.codex.repository

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files

@RunWith(MockitoJUnitRunner::class)
class NoteDatabaseManagerTest {
    private lateinit var databaseManager: NoteDatabaseManager
    private lateinit var tempDir: File

    @Before
    fun setUp() {
        tempDir = Files.createTempDirectory("notes-test").toFile()
        databaseManager = NoteDatabaseManager(tempDir.absolutePath)
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `should insert and retrieve a note`() {
        val note =
            Note(
                id = "test-id",
                title = "Test Note",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )

        databaseManager.insertNote(note)

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertEquals("test-id", notes[0].id)
        assertEquals("Test Note", notes[0].title)
        assertEquals("/path/to/note.md", notes[0].filePath)
        assertEquals(1000L, notes[0].createdAt)
        assertEquals(2000L, notes[0].updatedAt)
        assertFalse(notes[0].isFavorite)
        assertEquals(NoteColorEnum.NONE, notes[0].color)
    }

    @Test
    fun `should insert note with all fields`() {
        val note =
            Note(
                id = "test-id",
                title = "Favorite Note",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
                isFavorite = true,
                color = NoteColorEnum.BLUE,
            )

        databaseManager.insertNote(note)

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertTrue(notes[0].isFavorite)
        assertEquals(NoteColorEnum.BLUE, notes[0].color)
    }

    @Test
    fun `should return empty list when no notes exist`() {
        val notes = databaseManager.getAllNotes()

        assertTrue(notes.isEmpty())
    }

    @Test
    fun `should update note title when title is provided`() {
        val note =
            Note(
                id = "test-id",
                title = "Original Title",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        databaseManager.insertNote(note)

        databaseManager.updateNote("test-id", "Updated Title")

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertEquals("Updated Title", notes[0].title)
        assertTrue(notes[0].updatedAt > 2000L)
    }

    @Test
    fun `should update only updatedAt when title is null`() {
        val note =
            Note(
                id = "test-id",
                title = "Original Title",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        databaseManager.insertNote(note)

        databaseManager.updateNote("test-id", null)

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertEquals("Original Title", notes[0].title)
        assertEquals("/path/to/note.md", notes[0].filePath)
        assertTrue(notes[0].updatedAt > 2000L)
    }

    @Test
    fun `should update filePath when filePath is provided`() {
        val note =
            Note(
                id = "test-id",
                title = "Test Note",
                filePath = "/path/to/old-note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        databaseManager.insertNote(note)

        databaseManager.updateNote("test-id", null, "/path/to/new-note.md")

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertEquals("Test Note", notes[0].title)
        assertEquals("/path/to/new-note.md", notes[0].filePath)
        assertTrue(notes[0].updatedAt > 2000L)
    }

    @Test
    fun `should update both title and filePath when both are provided`() {
        val note =
            Note(
                id = "test-id",
                title = "Original Title",
                filePath = "/path/to/old-note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        databaseManager.insertNote(note)

        databaseManager.updateNote("test-id", "New Title", "/path/to/new-note.md")

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertEquals("New Title", notes[0].title)
        assertEquals("/path/to/new-note.md", notes[0].filePath)
        assertTrue(notes[0].updatedAt > 2000L)
    }

    @Test
    fun `should toggle favorite from false to true`() {
        val note =
            Note(
                id = "test-id",
                title = "Test Note",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
                isFavorite = false,
            )
        databaseManager.insertNote(note)

        databaseManager.toggleFavorite("test-id")

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertTrue(notes[0].isFavorite)
    }

    @Test
    fun `should toggle favorite from true to false`() {
        val note =
            Note(
                id = "test-id",
                title = "Test Note",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
                isFavorite = true,
            )
        databaseManager.insertNote(note)

        databaseManager.toggleFavorite("test-id")

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertFalse(notes[0].isFavorite)
    }

    @Test
    fun `should change note color`() {
        val note =
            Note(
                id = "test-id",
                title = "Test Note",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        databaseManager.insertNote(note)

        databaseManager.changeColor("test-id", NoteColorEnum.GREEN)

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertEquals(NoteColorEnum.GREEN, notes[0].color)
        assertTrue(notes[0].updatedAt > 2000L)
    }

    @Test
    fun `should delete a note`() {
        val note =
            Note(
                id = "test-id",
                title = "Test Note",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        databaseManager.insertNote(note)

        databaseManager.deleteNote("test-id")

        val notes = databaseManager.getAllNotes()
        assertTrue(notes.isEmpty())
    }

    @Test
    fun `should check if note exists when it does`() {
        val note =
            Note(
                id = "test-id",
                title = "Test Note",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        databaseManager.insertNote(note)

        assertTrue(databaseManager.noteExists("test-id"))
    }

    @Test
    fun `should check if note exists when it does not`() {
        assertFalse(databaseManager.noteExists("nonexistent-id"))
    }

    @Test
    fun `should handle multiple notes`() {
        val note1 =
            Note(
                id = "id-1",
                title = "Note 1",
                filePath = "/path/to/note1.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        val note2 =
            Note(
                id = "id-2",
                title = "Note 2",
                filePath = "/path/to/note2.md",
                createdAt = 3000L,
                updatedAt = 4000L,
                isFavorite = true,
                color = NoteColorEnum.RED,
            )

        databaseManager.insertNote(note1)
        databaseManager.insertNote(note2)

        val notes = databaseManager.getAllNotes()
        assertEquals(2, notes.size)
    }

    @Test
    fun `should not delete other notes when deleting specific note`() {
        val note1 =
            Note(
                id = "id-1",
                title = "Note 1",
                filePath = "/path/to/note1.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        val note2 =
            Note(
                id = "id-2",
                title = "Note 2",
                filePath = "/path/to/note2.md",
                createdAt = 3000L,
                updatedAt = 4000L,
            )

        databaseManager.insertNote(note1)
        databaseManager.insertNote(note2)

        databaseManager.deleteNote("id-1")

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
        assertEquals("id-2", notes[0].id)
    }

    @Test
    fun `should create database directory if it does not exist`() {
        val nestedDir = File(tempDir, "nested/data")
        val manager = NoteDatabaseManager(nestedDir.absolutePath)

        val note =
            Note(
                id = "test-id",
                title = "Test Note",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        manager.insertNote(note)

        assertTrue(nestedDir.exists())
        assertEquals(1, manager.getAllNotes().size)
    }

    @Test
    fun `should persist notes across manager instances`() {
        val note =
            Note(
                id = "test-id",
                title = "Persistent Note",
                filePath = "/path/to/note.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )
        databaseManager.insertNote(note)

        val newManager = NoteDatabaseManager(tempDir.absolutePath)
        val notes = newManager.getAllNotes()

        assertEquals(1, notes.size)
        assertEquals("Persistent Note", notes[0].title)
    }
}
