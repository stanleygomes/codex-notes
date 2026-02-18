package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MenuItemDeleteComponentTest {
    @Mock
    private lateinit var project: Project

    @Test
    fun `should build delete menu item`() {
        val notes = listOf(Note(id = "1", title = "Test Note"))
        val component = MenuItemDeleteComponent()

        val menuItem = component.build(project, notes)

        assertNotNull(menuItem)
        assertNotNull(menuItem.text)
        assertTrue(menuItem.text.contains("(Delete)"))
    }

    @Test
    fun `should have action listener attached`() {
        val notes = listOf(Note(id = "1", title = "Test Note"))
        val component = MenuItemDeleteComponent()

        val menuItem = component.build(project, notes)

        assertEquals(1, menuItem.actionListeners.size)
    }

    @Test
    fun `should build delete menu item for multiple notes`() {
        val notes = listOf(
            Note(id = "1", title = "Note 1"),
            Note(id = "2", title = "Note 2"),
        )
        val component = MenuItemDeleteComponent()

        val menuItem = component.build(project, notes)

        assertNotNull(menuItem)
    }
}
