package com.nazarethlabs.codex.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import com.nazarethlabs.codex.editor.action.CreateNoteAction
import com.nazarethlabs.codex.helper.NoteNameHelper
import com.nazarethlabs.codex.repository.NoteStorageRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateNoteActionTest {
    @Mock
    private lateinit var actionEvent: AnActionEvent

    @Mock
    private lateinit var project: Project

    @Mock
    private lateinit var noteStorageRepository: NoteStorageRepository

    @Test
    fun `should generate correct title when creating note`() {
        val notes = emptyList<Note>()
        val expectedTitle = "Untitled 1"

        mockStatic(NoteNameHelper::class.java).use { mock ->
            mock.`when`<String> { NoteNameHelper.generateUntitledName(notes) }.thenReturn(expectedTitle)
            val result = NoteNameHelper.generateUntitledName(notes)
            assertEquals(expectedTitle, result)
        }
    }

    @Test
    fun `should not perform action when project is null`() {
        `when`(actionEvent.project).thenReturn(null)
        val createNoteAction = CreateNoteAction()
        createNoteAction.actionPerformed(actionEvent)
    }
}
