package com.nazarethlabs.codex.ui

import org.junit.Assert.assertTrue
import org.junit.Test
import java.awt.Component
import javax.swing.JPanel

class ToolWindowPanelTest {
    @Test
    fun `should focus first search component when search panel has components`() {
        val searchPanel = JPanel()
        val focusableComponent = FocusableComponent()
        searchPanel.add(focusableComponent)
        searchPanel.add(FocusableComponent())

        focusSearchField(searchPanel)

        assertTrue(focusableComponent.focusRequested)
    }

    @Test
    fun `should not fail when search panel has no components`() {
        val searchPanel = JPanel()

        focusSearchField(searchPanel)

        assertTrue(searchPanel.components.isEmpty())
    }

    private class FocusableComponent : Component() {
        var focusRequested = false

        override fun requestFocusInWindow(): Boolean {
            focusRequested = true
            return true
        }
    }
}
