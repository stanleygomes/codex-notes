package com.nazarethlabs.codex.service.note

import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.repository.NoteStorageRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.nio.file.Files

@RunWith(MockitoJUnitRunner::class)
class ImportNotesServiceTest {
    @Mock
    private lateinit var createNoteService: CreateNoteService

    @InjectMocks
    private lateinit var importNotesService: ImportNotesService

    @Mock
    private lateinit var virtualFile1: VirtualFile

    @Mock
    private lateinit var virtualFile2: VirtualFile

    @Test
    fun `should import files when valid files provided`() {
        mockStatic(NoteStorageRepository::class.java).use { mockedRepo ->
            val mockRepo = mock(NoteStorageRepository::class.java)
            mockedRepo.`when`<NoteStorageRepository> { NoteStorageRepository.getInstance() }.thenReturn(mockRepo)
            `when`(mockRepo.getAllNotes()).thenReturn(emptyList())

            val sourceDir = Files.createTempDirectory("test-import-source").toFile()
            val sourceFile1 = File(sourceDir, "note1.md")
            val sourceFile2 = File(sourceDir, "note2.md")
            sourceFile1.writeText("Note 1 content")
            sourceFile2.writeText("Note 2 content")

            `when`(createNoteService.createWithContent(null, "note1", ".md", "Note 1 content", false)).thenReturn(virtualFile1)
            `when`(createNoteService.createWithContent(null, "note2", ".md", "Note 2 content", false)).thenReturn(virtualFile2)

            val files = listOf(sourceFile1, sourceFile2)
            val result = importNotesService.importFiles(files)

            assertEquals(2, result.size)

            sourceFile1.delete()
            sourceFile2.delete()
            sourceDir.delete()
        }
    }

    @Test
    fun `should skip missing files when importing`() {
        mockStatic(NoteStorageRepository::class.java).use { mockedRepo ->
            val mockRepo = mock(NoteStorageRepository::class.java)
            mockedRepo.`when`<NoteStorageRepository> { NoteStorageRepository.getInstance() }.thenReturn(mockRepo)
            `when`(mockRepo.getAllNotes()).thenReturn(emptyList())

            val sourceDir = Files.createTempDirectory("test-import-source").toFile()
            val existingFile = File(sourceDir, "exists.md")
            existingFile.writeText("Existing note")
            val missingFile = File(sourceDir, "missing.md")

            `when`(createNoteService.createWithContent(null, "exists", ".md", "Existing note", false)).thenReturn(virtualFile1)

            val files = listOf(existingFile, missingFile)
            val result = importNotesService.importFiles(files)

            assertEquals(1, result.size)

            existingFile.delete()
            sourceDir.delete()
        }
    }

    @Test
    fun `should return empty list when no files provided`() {
        val result = importNotesService.importFiles(emptyList())

        assertEquals(0, result.size)
    }

    @Test
    fun `should resolve unique file name when duplicate exists`() {
        mockStatic(NoteStorageRepository::class.java).use { mockedRepo ->
            val mockRepo = mock(NoteStorageRepository::class.java)
            mockedRepo.`when`<NoteStorageRepository> { NoteStorageRepository.getInstance() }.thenReturn(mockRepo)
            `when`(mockRepo.getAllNotes()).thenReturn(listOf(Note(id = "1", title = "note")))

            val sourceDir = Files.createTempDirectory("test-import-source").toFile()
            val sourceFile = File(sourceDir, "note.md")
            sourceFile.writeText("New content")

            `when`(createNoteService.createWithContent(null, "note (1)", ".md", "New content", false)).thenReturn(virtualFile1)

            val files = listOf(sourceFile)
            val result = importNotesService.importFiles(files)

            assertEquals(1, result.size)

            sourceFile.delete()
            sourceDir.delete()
        }
    }

    @Test
    fun `should handle files without extension`() {
        mockStatic(NoteStorageRepository::class.java).use { mockedRepo ->
            val mockRepo = mock(NoteStorageRepository::class.java)
            mockedRepo.`when`<NoteStorageRepository> { NoteStorageRepository.getInstance() }.thenReturn(mockRepo)
            `when`(mockRepo.getAllNotes()).thenReturn(emptyList())

            val sourceDir = Files.createTempDirectory("test-import-source").toFile()
            val sourceFile = File(sourceDir, "note")
            sourceFile.writeText("Note content")

            `when`(createNoteService.createWithContent(null, "note", ".md", "Note content", false)).thenReturn(virtualFile1)

            val files = listOf(sourceFile)
            val result = importNotesService.importFiles(files)

            assertEquals(1, result.size)

            sourceFile.delete()
            sourceDir.delete()
        }
    }
}
