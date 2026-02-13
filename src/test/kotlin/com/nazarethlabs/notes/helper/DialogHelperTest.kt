package com.nazarethlabs.notes.helper

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mockStatic
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DialogHelperTest {

    @Mock
    private lateinit var project: Project

    @Test
    fun `should return YES when user confirms`() {
        mockStatic(Messages::class.java).use { mockedMessages ->
            mockedMessages.`when`<Int> { Messages.showYesNoDialog(project, "message", "title", Messages.getQuestionIcon()) }
                .thenReturn(Messages.YES)

            val result = DialogHelper.showYesNoDialog(project, "message", "title")

            assertEquals(Messages.YES, result)
        }
    }

    @Test
    fun `should return NO when user cancels`() {
        mockStatic(Messages::class.java).use { mockedMessages ->
            mockedMessages.`when`<Int> { Messages.showYesNoDialog(project, "message", "title", Messages.getQuestionIcon()) }
                .thenReturn(Messages.NO)

            val result = DialogHelper.showYesNoDialog(project, "message", "title")

            assertEquals(Messages.NO, result)
        }
    }

    @Test
    fun `should return input when user enters value`() {
        mockStatic(Messages::class.java).use { mockedMessages ->
            mockedMessages.`when`<String?> { Messages.showInputDialog(project, "message", "title", Messages.getQuestionIcon(), "initial", null) }
                .thenReturn("input")

            val result = DialogHelper.showInputDialog(project, "message", "title", "initial")

            assertEquals("input", result)
        }
    }

    @Test
    fun `should return null when user cancels input`() {
        mockStatic(Messages::class.java).use { mockedMessages ->
            mockedMessages.`when`<String?> { Messages.showInputDialog(project, "message", "title", Messages.getQuestionIcon(), "initial", null) }
                .thenReturn(null)

            val result = DialogHelper.showInputDialog(project, "message", "title", "initial")

            assertEquals(null, result)
        }
    }

    @Test
    fun `should call showWarningDialog with correct parameters`() {
        mockStatic(Messages::class.java).use { mockedMessages ->
            DialogHelper.showWarningDialog(project, "message", "title")

            mockedMessages.verify { Messages.showWarningDialog(project, "message", "title") }
        }
    }

    @Test
    fun `should call showErrorDialog with correct parameters`() {
        mockStatic(Messages::class.java).use { mockedMessages ->
            DialogHelper.showErrorDialog(project, "message", "title")

            mockedMessages.verify { Messages.showErrorDialog(project, "message", "title") }
        }
    }
}
