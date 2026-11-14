package com.nazarethlabs.codex.ui.toolbar.button

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ToolbarButtonSearchComponentTest {
    @Test
    fun `should build search button`() {
        val component = ToolbarButtonSearchComponent()

        val button = component.build()

        assertNotNull(button)
        assertNotNull(button.icon)
    }

    @Test
    fun `should have action listener attached`() {
        val component = ToolbarButtonSearchComponent()

        val button = component.build()

        assertNotNull(button.actionListeners)
        assertTrue(button.actionListeners.isNotEmpty())
    }
}
