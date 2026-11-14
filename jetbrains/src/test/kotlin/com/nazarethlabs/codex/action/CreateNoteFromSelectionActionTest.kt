package com.nazarethlabs.codex.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.editor.action.CreateNoteFromSelectionAction
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateNoteFromSelectionActionTest {
    @Mock
    private lateinit var actionEvent: AnActionEvent

    @Mock
    private lateinit var project: Project

    @Mock
    private lateinit var editor: Editor

    @Mock
    private lateinit var selectionModel: SelectionModel

    @Mock
    private lateinit var presentation: Presentation

    @Test
    fun `should be invisible when no text is selected`() {
        `when`(actionEvent.getData(CommonDataKeys.EDITOR)).thenReturn(editor)
        `when`(actionEvent.presentation).thenReturn(presentation)
        `when`(editor.selectionModel).thenReturn(selectionModel)
        `when`(selectionModel.hasSelection()).thenReturn(false)

        val action = CreateNoteFromSelectionAction()
        action.update(actionEvent)

        assertFalse(actionEvent.presentation.isEnabledAndVisible)
    }

    @Test
    fun `should be invisible when editor is null`() {
        `when`(actionEvent.getData(CommonDataKeys.EDITOR)).thenReturn(null)
        `when`(actionEvent.presentation).thenReturn(presentation)

        val action = CreateNoteFromSelectionAction()
        action.update(actionEvent)

        assertFalse(actionEvent.presentation.isEnabledAndVisible)
    }

    @Test
    fun `should not perform action when project is null`() {
        `when`(actionEvent.project).thenReturn(null)

        val action = CreateNoteFromSelectionAction()
        action.actionPerformed(actionEvent)
    }
}
