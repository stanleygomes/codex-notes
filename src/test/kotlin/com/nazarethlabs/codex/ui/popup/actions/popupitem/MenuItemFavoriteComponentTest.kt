package com.nazarethlabs.codex.ui.popup.actions.popupitem

import com.nazarethlabs.codex.dto.Note
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MenuItemFavoriteComponentTest {
    @Test
    fun `should build favorite menu item for single non-favorite note`() {
        val notes = listOf(Note(id = "1", title = "Test Note", isFavorite = false))
        val component = MenuItemFavoriteComponent()

        val menuItem = component.build(notes)

        assertNotNull(menuItem)
        assertNotNull(menuItem.text)
        assertTrue(menuItem.text.contains("(F)"))
    }

    @Test
    fun `should build unfavorite menu item for single favorite note`() {
        val notes = listOf(Note(id = "1", title = "Test Note", isFavorite = true))
        val component = MenuItemFavoriteComponent()

        val menuItem = component.build(notes)

        assertNotNull(menuItem)
        assertNotNull(menuItem.text)
    }

    @Test
    fun `should build favorite menu item for multiple notes`() {
        val notes =
            listOf(
                Note(id = "1", title = "Note 1"),
                Note(id = "2", title = "Note 2"),
            )
        val component = MenuItemFavoriteComponent()

        val menuItem = component.build(notes)

        assertNotNull(menuItem)
        assertNotNull(menuItem.text)
    }

    @Test
    fun `should have action listener attached`() {
        val notes = listOf(Note(id = "1", title = "Test Note"))
        val component = MenuItemFavoriteComponent()

        val menuItem = component.build(notes)

        assertEquals(1, menuItem.actionListeners.size)
    }
}
