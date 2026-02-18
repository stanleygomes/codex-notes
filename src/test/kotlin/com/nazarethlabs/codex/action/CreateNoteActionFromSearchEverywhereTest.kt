package com.nazarethlabs.codex.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.editor.action.CreateNoteActionFromSearchEverywhere
import com.nazarethlabs.codex.repository.NoteStorageRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateNoteActionFromSearchEverywhereTest {
    @Mock
    private lateinit var actionEvent: AnActionEvent

    @Mock
    private lateinit var project: Project

    @Mock
    private lateinit var noteStorageRepository: NoteStorageRepository

    @Test
    fun `should not perform action when project is null`() {
        `when`(actionEvent.project).thenReturn(null)
        val createNoteActionFromSearchEverywhere = CreateNoteActionFromSearchEverywhere()
        createNoteActionFromSearchEverywhere.actionPerformed(actionEvent)
    }
}
