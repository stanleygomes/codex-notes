package com.nazarethlabs.codex.editor.extension

import com.intellij.openapi.vfs.VirtualFile
import com.nazarethlabs.codex.service.settings.NotesSettingsService
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mockConstruction
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NoteFileWritingAccessExtensionTest {
    @Mock
    private lateinit var virtualFile: VirtualFile

    private lateinit var extension: NoteFileWritingAccessExtension

    @Before
    fun setUp() {
        extension = NoteFileWritingAccessExtension()
    }

    @Test
    fun `should return true when file is inside notes directory`() {
        mockConstruction(NotesSettingsService::class.java) { mock, _ ->
            `when`(mock.getNotesDirectory()).thenReturn("/home/user/.codex-notes")
        }.use {
            `when`(virtualFile.path).thenReturn("/home/user/.codex-notes/my-note.md")

            val result = extension.isWritable(virtualFile)

            assertTrue(result)
        }
    }

    @Test
    fun `should return false when file is outside notes directory`() {
        mockConstruction(NotesSettingsService::class.java) { mock, _ ->
            `when`(mock.getNotesDirectory()).thenReturn("/home/user/.codex-notes")
        }.use {
            `when`(virtualFile.path).thenReturn("/home/user/projects/some-file.kt")

            val result = extension.isWritable(virtualFile)

            assertFalse(result)
        }
    }

    @Test
    fun `should return true when file is in subdirectory of notes directory`() {
        mockConstruction(NotesSettingsService::class.java) { mock, _ ->
            `when`(mock.getNotesDirectory()).thenReturn("/home/user/.codex-notes")
        }.use {
            `when`(virtualFile.path).thenReturn("/home/user/.codex-notes/subfolder/note.md")

            val result = extension.isWritable(virtualFile)

            assertTrue(result)
        }
    }

    @Test
    fun `should return false when file is in directory with similar name prefix`() {
        mockConstruction(NotesSettingsService::class.java) { mock, _ ->
            `when`(mock.getNotesDirectory()).thenReturn("/home/user/.codex-notes")
        }.use {
            `when`(virtualFile.path).thenReturn("/home/user/.codex-notes-backup/note.md")

            val result = extension.isWritable(virtualFile)

            assertFalse(result)
        }
    }
}
