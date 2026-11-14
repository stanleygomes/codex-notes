package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.nazarethlabs.codex.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MenuItemOpenFileLocationComponentTest {
    @Test
    fun `should build open file location menu item when note has valid file path`() {
        val note = Note(id = "1", title = "Test Note", filePath = "/path/to/notes/test.md")
        val component = MenuItemOpenFileLocationComponent()

        val menuItem = component.build(listOf(note))

        assertNotNull(menuItem)
        assertNotNull(menuItem.text)
    }

    @Test
    fun `should build menu item with correct border when called`() {
        val note = Note(id = "1", title = "Test Note", filePath = "/path/to/notes/test.md")
        val component = MenuItemOpenFileLocationComponent()

        val menuItem = component.build(listOf(note))

        assertNotNull(menuItem.border)
    }

    @Test
    fun `should have action listener attached when menu item is built`() {
        val note = Note(id = "1", title = "Test Note", filePath = "/path/to/notes/test.md")
        val component = MenuItemOpenFileLocationComponent()

        val menuItem = component.build(listOf(note))

        assertEquals(1, menuItem.actionListeners.size)
    }
}
