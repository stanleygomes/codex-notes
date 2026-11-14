package com.nazarethlabs.codex.ui.component

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.swing.UIManager

@RunWith(MockitoJUnitRunner::class)
class MenuItemComponentTest {
    @Test
    fun `should create menu item with text`() {
        val component = MenuItemComponent()

        val menuItem = component.build(text = "Test Item", action = {})

        assertEquals("Test Item", menuItem.text)
    }

    @Test
    fun `should create menu item with icon when provided`() {
        val component = MenuItemComponent()
        val icon = UIManager.getIcon("FileView.fileIcon")

        val menuItem = component.build(text = "Test Item", icon = icon, action = {})

        assertNotNull(menuItem.icon)
    }

    @Test
    fun `should create menu item without icon when not provided`() {
        val component = MenuItemComponent()

        val menuItem = component.build(text = "Test Item", action = {})

        assertEquals(null, menuItem.icon)
    }

    @Test
    fun `should have border set on menu item`() {
        val component = MenuItemComponent()

        val menuItem = component.build(text = "Test Item", action = {})

        assertNotNull(menuItem.border)
    }

    @Test
    fun `should have action listener attached`() {
        val component = MenuItemComponent()

        val menuItem = component.build(text = "Test Item", action = {})

        assertEquals(1, menuItem.actionListeners.size)
    }
}
