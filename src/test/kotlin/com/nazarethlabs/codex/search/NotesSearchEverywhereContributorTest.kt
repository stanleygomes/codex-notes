package com.nazarethlabs.codex.search

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.util.Processor
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.repository.NoteStorageRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files

@RunWith(MockitoJUnitRunner::class)
class NotesSearchEverywhereContributorTest {
    @Mock
    private lateinit var event: AnActionEvent

    @Mock
    private lateinit var project: Project

    @Mock
    private lateinit var noteStorage: NoteStorageRepository

    @Mock
    private lateinit var progressIndicator: ProgressIndicator

    private lateinit var contributor: NotesSearchEverywhereContributor

    @Before
    fun setUp() {
        `when`(event.project).thenReturn(project)
        contributor = NotesSearchEverywhereContributor(event)
    }

    @Test
    fun `should return correct search provider id`() {
        val providerId = contributor.getSearchProviderId()
        assertEquals("NotesSearchEverywhereContributor", providerId)
    }

    @Test
    fun `should return correct group name`() {
        val groupName = contributor.getGroupName()
        assertEquals("Notes", groupName)
    }

    @Test
    fun `should return correct sort weight`() {
        val sortWeight = contributor.getSortWeight()
        assertEquals(300, sortWeight)
    }

    @Test
    fun `should show in find results`() {
        assertTrue(contributor.showInFindResults())
    }

    @Test
    fun `should return null for data for item`() {
        val note = Note(id = "1", title = "Test Note")
        val result = contributor.getDataForItem(note, "any-data-id")
        assertNull(result)
    }

    @Test
    fun `should return renderer instance`() {
        val renderer = contributor.getElementsRenderer()
        assertNotNull(renderer)
        assertTrue(renderer is NoteSearchCellRenderer)
    }

    @Test
    fun `should create contributor from factory`() {
        val factory = NotesSearchEverywhereContributorFactory()
        val result = factory.createContributor(event)
        assertNotNull(result)
        assertTrue(result is NotesSearchEverywhereContributor)
    }

    @Test
    fun `should not fetch elements when pattern is empty`() {
        val mockConsumer = mock(Processor::class.java) as Processor<Note>

        contributor.fetchElements("", progressIndicator, mockConsumer)

        verify(mockConsumer, never()).process(any())
    }

    @Test
    fun `should fetch notes matching title when pattern provided`() {
        val tempDir = Files.createTempDirectory("test-search").toFile()
        val noteFile = File(tempDir, "test.md")
        noteFile.writeText("Some content")

        val notes =
            listOf(
                Note(id = "1", title = "Kotlin Guide", filePath = noteFile.absolutePath),
                Note(id = "2", title = "Java Tutorial", filePath = noteFile.absolutePath),
            )

        val mockContributor = spy(contributor)
        doReturn(notes).`when`(mockContributor).getAllNotes()
        `when`(progressIndicator.isCanceled).thenReturn(false)

        val processedNotes = mutableListOf<Note>()
        val mockConsumer =
            Processor<Note> { note ->
                processedNotes.add(note)
                true
            }

        mockContributor.fetchElements("kotlin", progressIndicator, mockConsumer)

        assertEquals(1, processedNotes.size)
        assertEquals("Kotlin Guide", processedNotes[0].title)

        noteFile.delete()
        tempDir.delete()
    }

    @Test
    fun `should fetch notes matching content when pattern provided`() {
        val tempDir = Files.createTempDirectory("test-search").toFile()
        val noteFile1 = File(tempDir, "note1.md")
        val noteFile2 = File(tempDir, "note2.md")
        noteFile1.writeText("This note contains kotlin examples")
        noteFile2.writeText("This is about java")

        val notes =
            listOf(
                Note(id = "1", title = "Programming", filePath = noteFile1.absolutePath),
                Note(id = "2", title = "Languages", filePath = noteFile2.absolutePath),
            )

        val mockContributor = spy(contributor)
        doReturn(notes).`when`(mockContributor).getAllNotes()
        `when`(progressIndicator.isCanceled).thenReturn(false)

        val processedNotes = mutableListOf<Note>()
        val mockConsumer =
            Processor<Note> { note ->
                processedNotes.add(note)
                true
            }

        mockContributor.fetchElements("kotlin", progressIndicator, mockConsumer)

        assertEquals(1, processedNotes.size)
        assertEquals("Programming", processedNotes[0].title)

        noteFile1.delete()
        noteFile2.delete()
        tempDir.delete()
    }

    @Test
    fun `should be case insensitive when searching`() {
        val tempDir = Files.createTempDirectory("test-search").toFile()
        val noteFile = File(tempDir, "test.md")
        noteFile.writeText("Some content")

        val notes =
            listOf(
                Note(id = "1", title = "Kotlin Guide", filePath = noteFile.absolutePath),
            )

        val mockContributor = spy(contributor)
        doReturn(notes).`when`(mockContributor).getAllNotes()
        `when`(progressIndicator.isCanceled).thenReturn(false)

        val processedNotes = mutableListOf<Note>()
        val mockConsumer =
            Processor<Note> { note ->
                processedNotes.add(note)
                true
            }

        mockContributor.fetchElements("KOTLIN", progressIndicator, mockConsumer)

        assertEquals(1, processedNotes.size)

        noteFile.delete()
        tempDir.delete()
    }

    @Test
    fun `should handle missing file when searching content`() {
        val notes =
            listOf(
                Note(id = "1", title = "Test", filePath = "/nonexistent/file.md"),
            )

        val mockContributor = spy(contributor)
        doReturn(notes).`when`(mockContributor).getAllNotes()
        `when`(progressIndicator.isCanceled).thenReturn(false)

        val processedNotes = mutableListOf<Note>()
        val mockConsumer =
            Processor<Note> { note ->
                processedNotes.add(note)
                true
            }

        mockContributor.fetchElements("content", progressIndicator, mockConsumer)

        assertEquals(0, processedNotes.size)
    }

    @Test
    fun `should stop fetching when progress is canceled`() {
        val tempDir = Files.createTempDirectory("test-search").toFile()
        val noteFile = File(tempDir, "test.md")
        noteFile.writeText("Some content")

        val notes =
            listOf(
                Note(id = "1", title = "First Note", filePath = noteFile.absolutePath),
                Note(id = "2", title = "Second Note", filePath = noteFile.absolutePath),
            )

        val mockContributor = spy(contributor)
        doReturn(notes).`when`(mockContributor).getAllNotes()
        `when`(progressIndicator.isCanceled).thenReturn(true)

        val processedNotes = mutableListOf<Note>()
        val mockConsumer =
            Processor<Note> { note ->
                processedNotes.add(note)
                true
            }

        mockContributor.fetchElements("note", progressIndicator, mockConsumer)

        assertEquals(0, processedNotes.size)

        noteFile.delete()
        tempDir.delete()
    }

    private fun NotesSearchEverywhereContributor.getAllNotes(): List<Note> {
        val field = this::class.java.getDeclaredField("noteStorage")
        field.isAccessible = true
        field.set(this, noteStorage)
        return noteStorage.getAllNotes()
    }
}
