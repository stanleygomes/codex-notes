package com.nazarethlabs.codex.ui.component

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.swing.UIManager

@RunWith(MockitoJUnitRunner::class)
class MenuComponentTest {
    @Test
    fun `should create menu with text`() {
        val component = MenuComponent()

        val menu = component.build(text = "Test Menu")

        assertEquals("Test Menu", menu.text)
    }

    @Test
    fun `should create menu with icon when provided`() {
        val component = MenuComponent()
        val icon = UIManager.getIcon("FileView.fileIcon")

        val menu = component.build(text = "Test Menu", icon = icon)

        assertNotNull(menu.icon)
    }

    @Test
    fun `should create menu without icon when not provided`() {
        val component = MenuComponent()

        val menu = component.build(text = "Test Menu")

        assertEquals(null, menu.icon)
    }

    @Test
    fun `should have border set on menu`() {
        val component = MenuComponent()

        val menu = component.build(text = "Test Menu")

        assertNotNull(menu.border)
    }
}
