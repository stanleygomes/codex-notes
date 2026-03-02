package com.nazarethlabs.codex.service.note

import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.repository.NoteDatabaseManager
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files

@RunWith(MockitoJUnitRunner::class)
class NoteMigrationServiceTest {
    private lateinit var databaseManager: NoteDatabaseManager
    private lateinit var tempDir: File

    @Before
    fun setUp() {
        tempDir = Files.createTempDirectory("migration-test").toFile()
        databaseManager = NoteDatabaseManager(tempDir.absolutePath)
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `should migrate notes preserving all fields`() {
        val legacyNotes =
            listOf(
                Note(
                    id = "id-1",
                    title = "Note 1",
                    filePath = "/path/to/note1.md",
                    createdAt = 1000L,
                    updatedAt = 2000L,
                    isFavorite = true,
                    color = NoteColorEnum.BLUE,
                ),
                Note(
                    id = "id-2",
                    title = "Note 2",
                    filePath = "/path/to/note2.md",
                    createdAt = 3000L,
                    updatedAt = 4000L,
                    isFavorite = false,
                    color = NoteColorEnum.NONE,
                ),
            )

        legacyNotes.forEach { note ->
            if (!databaseManager.noteExists(note.id)) {
                databaseManager.insertNote(note)
            }
        }

        val migratedNotes = databaseManager.getAllNotes()
        assertEquals(2, migratedNotes.size)

        val note1 = migratedNotes.find { it.id == "id-1" }!!
        assertEquals("Note 1", note1.title)
        assertEquals("/path/to/note1.md", note1.filePath)
        assertEquals(1000L, note1.createdAt)
        assertEquals(2000L, note1.updatedAt)
        assertTrue(note1.isFavorite)
        assertEquals(NoteColorEnum.BLUE, note1.color)

        val note2 = migratedNotes.find { it.id == "id-2" }!!
        assertEquals("Note 2", note2.title)
        assertEquals("/path/to/note2.md", note2.filePath)
        assertEquals(3000L, note2.createdAt)
        assertEquals(4000L, note2.updatedAt)
        assertEquals(false, note2.isFavorite)
        assertEquals(NoteColorEnum.NONE, note2.color)
    }

    @Test
    fun `should not duplicate notes when migration runs twice`() {
        val note =
            Note(
                id = "id-1",
                title = "Note 1",
                filePath = "/path/to/note1.md",
                createdAt = 1000L,
                updatedAt = 2000L,
            )

        if (!databaseManager.noteExists(note.id)) {
            databaseManager.insertNote(note)
        }

        if (!databaseManager.noteExists(note.id)) {
            databaseManager.insertNote(note)
        }

        val notes = databaseManager.getAllNotes()
        assertEquals(1, notes.size)
    }

    @Test
    fun `should skip migration when no legacy notes exist`() {
        val legacyNotes = emptyList<Note>()

        legacyNotes.forEach { note ->
            if (!databaseManager.noteExists(note.id)) {
                databaseManager.insertNote(note)
            }
        }

        val notes = databaseManager.getAllNotes()
        assertTrue(notes.isEmpty())
    }
}
