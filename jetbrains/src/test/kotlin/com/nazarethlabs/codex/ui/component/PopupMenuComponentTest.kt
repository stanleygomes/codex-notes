package com.nazarethlabs.codex.ui.component

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.swing.JPopupMenu

@RunWith(MockitoJUnitRunner::class)
class PopupMenuComponentTest {
    @Test
    fun `should create popup menu`() {
        val component = PopupMenuComponent()

        val menu = component.build()

        assertNotNull(menu)
        assertTrue(menu is JPopupMenu)
    }

    @Test
    fun `should create popup menu with border`() {
        val component = PopupMenuComponent()

        val menu = component.build()

        assertNotNull(menu.border)
    }
}
