package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.intellij.openapi.project.Project
import com.nazarethlabs.codex.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MenuItemDuplicateComponentTest {
    @Mock
    private lateinit var project: Project

    @Test
    fun `should build duplicate menu item for single note`() {
        val notes = listOf(Note(id = "1", title = "Test Note"))
        val component = MenuItemDuplicateComponent()

        val menuItem = component.build(project, notes)

        assertNotNull(menuItem)
        assertNotNull(menuItem.text)
    }

    @Test
    fun `should enable duplicate for single note selection`() {
        val notes = listOf(Note(id = "1", title = "Test Note"))
        val component = MenuItemDuplicateComponent()

        val menuItem = component.build(project, notes)

        assertTrue(menuItem.isEnabled)
    }

    @Test
    fun `should disable duplicate for multiple note selection`() {
        val notes =
            listOf(
                Note(id = "1", title = "Note 1"),
                Note(id = "2", title = "Note 2"),
            )
        val component = MenuItemDuplicateComponent()

        val menuItem = component.build(project, notes)

        assertFalse(menuItem.isEnabled)
    }

    @Test
    fun `should have action listener attached`() {
        val notes = listOf(Note(id = "1", title = "Test Note"))
        val component = MenuItemDuplicateComponent()

        val menuItem = component.build(project, notes)

        assertEquals(1, menuItem.actionListeners.size)
    }
}
